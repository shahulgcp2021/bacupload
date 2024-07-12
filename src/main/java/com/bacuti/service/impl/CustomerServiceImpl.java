package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Customer;
import com.bacuti.repository.CustomerRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.CustomerService;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.CustomerDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.CustomerMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Handel business logic and service api related to customer.
 * And maintain the create and update database calls.
 *
 * @author Premkalyan Sekar
 * @since 03/07/2023
 * @version 1.0
 */
@Service("CustomerService")
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.debug("REST request to save Customer");
        // Validating the input params
        validateCustomer(customerDTO);
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
        customerMapper.updateAuditColumns(customer);
        log.debug("Created Customer Entity");
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    /**
     * Method used to validate the input customer params.
     * @param customerDTO - Input customerDTO
     */
    private void validateCustomer(CustomerDTO customerDTO) {
        log.debug("Validate Customer");
        if (StringUtils.isEmpty(customerDTO.getCustomerName()))
            throw new BusinessException("CustomerName should not be Empty", HttpStatus.NOT_FOUND.value());

        if (StringUtils.isEmpty(customerDTO.getDescription()))
            throw new BusinessException("Description should not be null", HttpStatus.NOT_FOUND.value());

        if (Objects.nonNull(customerDTO.getId()) && !customerRepository.existsById(customerDTO.getId()))
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());

        Optional<Customer> savedCustomer = customerRepository.findByCustomerNameIgnoreCase(customerDTO.getCustomerName());
        if (savedCustomer.isPresent() && (Objects.isNull(customerDTO.getId()) || !savedCustomer.get().getId().equals(customerDTO.getId())))
            throw new BusinessException("Customer Name Already Exists", HttpStatus.NOT_FOUND.value());
    }

    /**
     * Gets the loggedIn company.
     *
     * @return loggedIn company.
     */
    private CompanyDTO getLoggedInCompany() {
        log.debug("REST request to get Logged In Company");
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyService.findByName(userMetaData.get("companyName"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CustomerDTO> getCustomers(int pageNo, int pageSize, String sortBy, String sortDirection, String customerName) {
        log.debug("REST request to get customers");
        Page<Customer> pageSite;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(customerName)) {
            pageSite = customerRepository.findAll(pageable);
        } else {
            pageSite = customerRepository.findByCustomerNameContaining(customerName, pageable);
        }
        log.debug("Customer details in pageSite");
        return pageSite.map(customerMapper::customerToCustomerDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCustomerById(Long id) {
        log.debug("REST request to delete Customer");
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());
        }
        customerRepository.deleteById(id);
    }

    /**
     * Method used to process the input sheet and save the entity
     *
     * @param sheet - Input sheet from upload
     * @return - Returns the error detail dto
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<Customer> customerList = new ArrayList<>();
        log.debug("REST request to validate and Save Customer");
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();

        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.CUSTOMER_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            Customer customer = validateRow(row, errorDTOs, rowIndex);

            if (errorDTOs.isEmpty()) {
                customerRepository.findByCustomerNameIgnoreCase(customer.getCustomerName()).ifPresent(customerDtl ->
                    customer.setId(customerDtl.getId()));
                customer.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
                customerMapper.updateAuditColumns(customer);
                customerList.add(customer);
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        log.debug("Customer details in sheet");

        if (!CollectionUtils.isEmpty(customerList))
            saveEntitiesInBatches(customerList);
        return errorDetailDTOS;
    }

    /**
     * Method used to validate the input row by cell wise.
     *
     * @param row - Row from the sheet
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @return - Returns the customer entity
     */
    private Customer validateRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        Customer customer = new Customer();

        validateAndSetField(row, errorDetailDTOS, rowIndex, 0, customer::setCustomerName, "Company Name", Boolean.TRUE);
        validateAndSetField(row, errorDetailDTOS, rowIndex, 1, customer::setDescription, "Description", Boolean.TRUE);
        validateAndSetField(row, errorDetailDTOS, rowIndex, 2, customer::setWebsite, "Website", Boolean.FALSE);
        validateAndSetField(row, errorDetailDTOS, rowIndex, 3, customer::setCountry, "Country", Boolean.FALSE);
        validateAndSetField(row, errorDetailDTOS, rowIndex, 4, customer::setSustainabilityContactName, "Contact Name", Boolean.FALSE);
        //Email validation
        Cell cell5 = row.getCell(5);
        Optional<String> error = FileValidationUtil.validateEmail(cell5, 255, Boolean.FALSE);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Contact Email : " + error.get(), rowIndex, 5));
        } else if (Objects.nonNull(cell5)) {
            customer.setSustainabilityContactEmail(cell5.getStringCellValue());
        }
        return customer;
    }

    /**
     * Method used to validate the each cell and add the error message
     *
     * @param row - Row from the file
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @param cellIndex - Cell index of each row
     * @param setter - Setter key to set the value
     * @param fieldName - Field name to check
     * @param mandate - Mandatory check for field
     */
    private void validateAndSetField(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, int cellIndex,
                                     Consumer<String> setter, String fieldName, Boolean mandate) {
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateString(cell, 255, mandate);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + error.get(), rowIndex, cellIndex));
        } else if (Objects.nonNull(cell)) {
            setter.accept(cell.getStringCellValue());
        }
    }

    /**
     * Method used to save the entries by batch wise
     *
     * @param customersList - List of customer entity
     */
    public void saveEntitiesInBatches(List<Customer> customersList) {
        for (int i = 0; i < customersList.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, customersList.size());
            List<Customer> batchList = customersList.subList(i, endIndex);
            customerRepository.saveAll(batchList);
            customerRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
