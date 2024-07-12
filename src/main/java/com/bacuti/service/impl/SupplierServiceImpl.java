package com.bacuti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.commons.collections4.CollectionUtils;
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
import org.springframework.transaction.annotation.Transactional;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Supplier;
import com.bacuti.repository.SupplierRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.SupplierService;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.SupplierDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.SupplierMapper;
import com.bacuti.web.rest.SupplierResource;

/**
 * Implementation base class for {@link SupplierService}
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * {@inheritDoc}
     */
    @Override
    public SupplierDTO save(SupplierDTO supplierDto) {
        log.debug("REST request to save Supplier : {}", supplierDto);
        if (Objects.nonNull(supplierDto.getId())) {
            throw new BusinessException("A new supplier cannot already have an ID", HttpStatus.BAD_REQUEST.value());
        }
        validate(supplierDto);
        Supplier supplier = supplierRepository.findBySupplierNameIgnoreCase(supplierDto.getSupplierName());
        if (Objects.nonNull(supplier))
            throw new BusinessException("Supplier name already exists", HttpStatus.PRECONDITION_FAILED.value());
        CompanyDTO companyDTO = getLoggedInUserCompany();
        Supplier supplierToBeSaved = supplierMapper.toEntity(supplierDto);
        supplierToBeSaved.setCompany(CompanyMapper.INSTANCE.companyDTOToCompany(companyDTO));
        supplierMapper.updateAuditColumns(supplierToBeSaved);
        return supplierMapper.toDto(supplierRepository.save(supplierToBeSaved));
    }

    /**
     * Gets the current loggedIn company.
     *
     * @return current loggedIn company.
     */
    private CompanyDTO getLoggedInUserCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        if (Objects.isNull(userMetaData))
            throw new BusinessException("User company not found in header", HttpStatus.BAD_REQUEST.value());
        return companyService.findByName(userMetaData.get("companyName"));
    }

    /**
     * validates the given supplier dto.
     *
     * @param supplierDto to be validated.
     */
    private void validate(SupplierDTO supplierDto) {
        if (Objects.isNull(supplierDto.getSupplierName()) || supplierDto.getSupplierName().isEmpty()) {
            throw new BusinessException("Supplier name is empty", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.isNull(supplierDto.getDescription()) || supplierDto.getDescription().isEmpty())
            throw new BusinessException("Description is empty", HttpStatus.PRECONDITION_FAILED.value());
        if (Objects.nonNull(supplierDto.getSupplierName()) && supplierDto.getSupplierName().length() > 255) {
            throw new BusinessException("Supplier name must not exceed 255 characters",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getDescription()) && supplierDto.getDescription().length() > 255) {
            throw new BusinessException("Description must not exceed 255 characters",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getCategory()) && supplierDto.getCategory().length() > 255) {
            throw new BusinessException("Category must not exceed 255 characters",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getSustainabilityContactEmail())
            && supplierDto.getSustainabilityContactEmail().length() > 255) {
            throw new BusinessException("Sustainability Contact Email must not exceed 255 characters",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getSustainabilityContactName())
            && supplierDto.getSustainabilityContactName().length() > 255) {
            throw new BusinessException("Sustainability Contact Name must not exceed 255 characters",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getWebsite()) && supplierDto.getWebsite().length() > 255) {
            throw new BusinessException("Website must not exceed 255 characters", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (!FileValidationUtil.isValidEmail(supplierDto.getSustainabilityContactEmail())) {
            throw new BusinessException("Invalid website", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.nonNull(supplierDto.getCountry()) && supplierDto.getCountry().length() > 255) {
            throw new BusinessException("Country must not exceed 255 characters", HttpStatus.PRECONDITION_FAILED.value());
        }
    }

    /**
     * validates the supplier id.
     *
     * @param supplierId to be validated.
     */
    private void validateId(Long supplierId) {
        if (Objects.isNull(supplierId)) {
            throw new BusinessException("Invalid id", HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SupplierDTO update(Long id, SupplierDTO supplierDto) {
        log.debug("REST request to update Supplier : {}, {}", id, supplierDto);
        if (!Objects.equals(id, supplierDto.getId()))
            throw new BusinessException("Invalid ID", HttpStatus.PRECONDITION_FAILED.value());
        validateId(supplierDto.getId());
        validate(supplierDto);
        Supplier supplier = supplierRepository.findBySupplierNameIgnoreCase(supplierDto.getSupplierName());
        if (Objects.nonNull(supplier) && !supplier.getId().equals(id))
            throw new BusinessException("Supplier name already exists", HttpStatus.PRECONDITION_FAILED.value());
        Supplier existingSupplier = supplierRepository.findById(id).orElseThrow(() ->
            new BusinessException("Supplier not found", HttpStatus.PRECONDITION_FAILED.value()));
        existingSupplier.setCategory(supplierDto.getCategory());
        existingSupplier.setSupplierName(supplierDto.getSupplierName());
        existingSupplier.setDescription(supplierDto.getDescription());
        existingSupplier.setWebsite(supplierDto.getWebsite());
        existingSupplier.setCountry(supplierDto.getCountry());
        existingSupplier.setSustainabilityContactEmail(supplierDto.getSustainabilityContactEmail());
        existingSupplier.setSustainabilityContactName(supplierDto.getSustainabilityContactName());
        supplierMapper.updateAuditColumns(existingSupplier);
        return supplierMapper.toDto(supplierRepository.save(existingSupplier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SupplierDTO findById(Long id) {
        log.debug("REST request to get Supplier : {}", id);
        validateId(id);
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
            new BusinessException("Supplier Not Found", HttpStatus.BAD_REQUEST.value()));
        return supplierMapper.toDto(supplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SupplierDTO> getAllSuppliersPaged(int start, int limit, String sortBy, String sortingOrder,
                                                  String searchKey) {
        log.debug("REST request to get all Suppliers");
        Page<Supplier> suppliers;
        Sort sort = Sort.by(Sort.Direction.fromString(sortingOrder), sortBy);
        Pageable pageable = PageRequest.of(start, limit, sort);
        if (Objects.isNull(searchKey) || searchKey.isEmpty()) {
            suppliers = supplierRepository.findAll(pageable);
        } else {
            suppliers = supplierRepository.findBySupplierNameLikeIgnoreCase(searchKey, pageable);
        }
        return suppliers.map(supplierMapper::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SupplierDTO> findAll() {
        return supplierMapper.mapAllToDto(supplierRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        validateId(id);
        supplierRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> fetchAllSupplierNames() {
        return supplierRepository.fetchAllSupplierNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        List<Supplier> suppliersToBeSaved = new ArrayList<>();
        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.SUPPLIER_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            Supplier supplier = validateRow(row, errorDTOs, rowIndex);
            if (CollectionUtils.isEmpty(errorDTOs)) {
                supplier.setCompany(CompanyMapper.INSTANCE.companyDTOToCompany(getLoggedInUserCompany()));
                supplierMapper.updateAuditColumns(supplier);
                suppliersToBeSaved.add(supplier);
            } else {
                errorDetailDTOS.addAll(errorDTOs);
            }
        }
        saveAllInBatches(suppliersToBeSaved);
        supplierRepository.saveAll(suppliersToBeSaved);
        return errorDetailDTOS;
    }

    /**
     * saves list of Supplier in batches.
     *
     * @param suppliersToBeSaved list of suppliers.
     */
    @Transactional
    private void saveAllInBatches(List<Supplier> suppliersToBeSaved) {
        for (int i = 0; i < suppliersToBeSaved.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, suppliersToBeSaved.size());
            List<Supplier> batchList = suppliersToBeSaved.subList(i, endIndex);
            supplierRepository.saveAll(batchList);
            supplierRepository.flush();
        }
    }

    /**
     * Validates the supplier in the current row.
     *
     * @param row             in the sheet.
     * @param errorDetailDTOS list of errors to be added.
     * @param rowIndex        index of the current row.
     * @return validated supplier of the current row.
     */
    private Supplier validateRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        Supplier supplier;
        Cell cell0 = row.getCell(0);
        Optional<String> supplierNameError = FileValidationUtil.validateString(cell0, 255, Boolean.TRUE);
        if (supplierNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Supplier Name: "
                + supplierNameError.get(), rowIndex, 0));
            return null;
        } else {
            supplier = supplierRepository.findBySupplierNameIgnoreCase(cell0.getStringCellValue());
            if (Objects.isNull(supplier)) {
                supplier = new Supplier();
                supplier.setSupplierName(cell0.getStringCellValue());
            }
        }
        validateAndSetCell(row, errorDetailDTOS, rowIndex, 1, supplier::setDescription, "Description",
            1000, Boolean.TRUE);
        validateAndSetCell(row, errorDetailDTOS, rowIndex, 2, supplier::setCategory, "Category",
            255, Boolean.FALSE);
        validateAndSetCell(row, errorDetailDTOS, rowIndex, 3, supplier::setWebsite, "Website",
            255, Boolean.TRUE);
        validateAndSetCell(row, errorDetailDTOS, rowIndex, 4, supplier::setCountry, "Country",
            255, Boolean.FALSE);
        validateAndSetCell(row, errorDetailDTOS, rowIndex, 5, supplier::setSustainabilityContactName,
            "Sustainability Contact Name", 255, Boolean.FALSE);
        Cell cell = row.getCell(6);
        Optional<String> cellError = FileValidationUtil.validateEmail(cell, 255, false);
        if (cellError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Sustainability Contact Email" + ": "
                + cellError.get(), rowIndex, 6));
        } else if (errorDetailDTOS.isEmpty()) {
            supplier.setSustainabilityContactEmail(cell.getStringCellValue());
        }
        return supplier;
    }

    /**
     * validates the value of the cell in a row.
     *
     * @param row             - current cell of the row.
     * @param errorDetailDTOS - list of errors to be added.
     * @param rowIndex        - index of the current row.
     * @param cellIndex       - index of the cell.
     * @param fieldSetter     - setter method of the respective cell.
     * @param fieldName       - name of the field.
     * @param maxLength       - length of the cell for validation.
     * @param isMandatory     - boolean to specify the cell is mandatory or not.
     */
    private void validateAndSetCell(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, int cellIndex,
                                    Consumer<String> fieldSetter, String fieldName, int maxLength, Boolean isMandatory) {
        Cell cell = row.getCell(cellIndex);
        Optional<String> cellError = FileValidationUtil.validateString(cell, maxLength, isMandatory);
        if (cellError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + cellError.get(),
                rowIndex, cellIndex));
        } else if (errorDetailDTOS.isEmpty()) {
            fieldSetter.accept(cell.getStringCellValue());
        }
    }
}
