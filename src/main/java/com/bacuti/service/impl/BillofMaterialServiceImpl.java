package com.bacuti.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.BillofMaterial;
import com.bacuti.domain.Company;
import com.bacuti.domain.Item;
import com.bacuti.domain.UnitOfMeasure;
import com.bacuti.enumeration.BillOfMaterialSheet;
import com.bacuti.repository.BillofMaterialRepository;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.ItemRepository;
import com.bacuti.repository.UnitOfMeasureRepository;
import com.bacuti.service.BillofMaterialService;
import com.bacuti.service.CompanyService;
import com.bacuti.service.dto.BillofMaterialDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.mapper.BillofMaterialMapper;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.ItemMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

@Service
@Transactional
public class BillofMaterialServiceImpl implements BillofMaterialService {
    private final Logger log = LoggerFactory.getLogger(com.bacuti.service.BillofMaterialService.class);

    @Autowired
    BillofMaterialRepository billofMaterialRepository;

    @Autowired
    BillofMaterialMapper billofMaterialMapper;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ItemMapper itemMapper;

    public BillofMaterialServiceImpl(BillofMaterialRepository billofMaterialRepository, BillofMaterialMapper billofMaterialMapper, ItemRepository itemRepository, EntityManager em, CompanyRepository companyRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.billofMaterialRepository = billofMaterialRepository;
        this.billofMaterialMapper = billofMaterialMapper;
        this.itemRepository = itemRepository;
        this.companyRepository = companyRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    public BillofMaterialDTO saveBillofMaterial(BillofMaterialDTO billofMaterialDTO) {
        log.debug("REST request to save Bill of Material: {}", billofMaterialDTO);
        // Validating the input params
        validateBOM(billofMaterialDTO);
        BillofMaterial billofMaterial = billofMaterialMapper.billofMaterialDTOToBillofMaterial(billofMaterialDTO);
        createParentAndChildEntity(billofMaterial);
        billofMaterial.setCompany(getLoggedInCompany());
        // Update audit columns
        billofMaterialMapper.updateAuditColumns(billofMaterial);
        BillofMaterial savedBillofMaterial = billofMaterialRepository.save(billofMaterial);
        log.info("Saved Bill of Material: {}", savedBillofMaterial);
        return billofMaterialMapper.billofMaterialToBillofMaterialDTO(savedBillofMaterial);
    }

    @Override
    public Page<BillofMaterialDTO> getBillofMaterials(int pageNo, int pageSize, String sortBy, String sortDirection, String productName) {
        log.debug("REST request to get Bill of Materials - pageNo: {}, pageSize: {}, sortBy: {}, sortDirection: {}, productName: {}", pageNo, pageSize, sortBy, sortDirection, productName);
        Page<BillofMaterial> pageSite;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(productName)) {
            pageSite = billofMaterialRepository.findAll(pageable);
        } else {
            pageSite = billofMaterialRepository.findByProductItemNameContaining(productName, pageable);
        }

        log.info("Retrieved Bill of Materials: {}", pageSite.getContent());
        return pageSite.map(billofMaterialMapper::billofMaterialToBillofMaterialDTO);
    }

    @Override
    public BillofMaterialDTO findById(Long id) {
        log.debug("REST request to find Bill of Material by id: {}", id);
        BillofMaterial dbBOM = billofMaterialRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value()));
        log.info("Found Bill of Material: {}", dbBOM);
        return billofMaterialMapper.billofMaterialToBillofMaterialDTO(dbBOM);
    }

    @Override
    public Boolean deleteById(Long id) {
        log.debug("REST request to delete Bill of Material by id: {}", id);
        boolean exists = billofMaterialRepository.existsById(id);
        if (!exists) {
            log.warn("Attempted to delete non-existing Bill of Material with id: {}", id);
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        billofMaterialRepository.deleteById(id);
        log.info("Deleted Bill of Material with id: {}", id);
        return true;
    }

    /**
     * Method used to validate the input bill of material params.
     * @param billofMaterialDTO - Input Bill of material DTO
     */
    private void validateBOM(BillofMaterialDTO billofMaterialDTO) {
        log.debug("Validating Bill of Material: {}", billofMaterialDTO);
        if (Objects.isNull(billofMaterialDTO.getProduct()) || StringUtils.isEmpty(billofMaterialDTO.getProduct().getItemName())) {
            log.error("Product validation failed: Product should not be null or Empty");
            throw new BusinessException("Product should not be null or Empty", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.isNull(billofMaterialDTO.getComponent()) || StringUtils.isEmpty(billofMaterialDTO.getComponent().getItemName())) {
            log.error("Component validation failed: Component should not be null or Empty");
            throw new BusinessException("Component should not be null or Empty", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.isNull(billofMaterialDTO.getUnitOfMeasure()) || (Objects.isNull(billofMaterialDTO.getUnitOfMeasure().getId()) && Objects.isNull(billofMaterialDTO.getUnitOfMeasure().getName()))) {
            log.error("UOM validation failed: UOM should not be null or Empty");
            throw new BusinessException("UOM should not be null or Empty", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.isNull(billofMaterialDTO.getQuantity())) {
            log.error("Quantity validation failed: Quantity should not be null or Empty");
            throw new BusinessException("Quantity should not be null or Empty", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.isNull(billofMaterialDTO.getYieldFactor())) {
            log.error("Yield Factor validation failed: Yield Factor should not be null or Empty");
            throw new BusinessException("Yield Factor should not be null or Empty", HttpStatus.NOT_FOUND.value());
        }

        if (Objects.nonNull(billofMaterialDTO.getId()) && billofMaterialRepository.findById(billofMaterialDTO.getId()).isEmpty()) {
            log.error("Bill of Material not found for id: {}", billofMaterialDTO.getId());
            throw new BusinessException("Bill of Material not found", HttpStatus.NOT_FOUND.value());
        }

        Optional<BillofMaterial> savedBillofMaterial = billofMaterialRepository.findByProductAndComponentItemNames(billofMaterialDTO.getProduct().getItemName(), billofMaterialDTO.getComponent().getItemName());

        if (savedBillofMaterial.isPresent()) {
            if (Objects.isNull(billofMaterialDTO.getId()) || !billofMaterialDTO.getId().equals(savedBillofMaterial.get().getId())) {
                log.error("Duplicate Bill of Material found for product: {} and component: {}", billofMaterialDTO.getProduct().getItemName(), billofMaterialDTO.getComponent().getItemName());
                throw new BusinessException(
                    "Bill of Material with the same product (" + billofMaterialDTO.getProduct().getItemName() +
                        ") and component (" + billofMaterialDTO.getComponent().getItemName() +
                        ") names already exists.", HttpStatus.BAD_REQUEST.value()
                );
            }
        }
    }

    /**
     * Method used to create the child or parent entity for current entity.
     *
     * @param billofMaterial - Bill of material entity
     */
    private void createParentAndChildEntity(BillofMaterial billofMaterial) {
        log.debug("Creating Parent and Child Entities for Bill of Material: {}", billofMaterial);
        //Check Product exist or create new entity
        if (Objects.nonNull(billofMaterial.getProduct())) {
            billofMaterial.setProduct(findOrCreateItem(billofMaterial.getProduct()));
        }

        //Check Component exist or create new entity
        if (Objects.nonNull(billofMaterial.getComponent())) {
            billofMaterial.setComponent(findOrCreateItem(billofMaterial.getComponent()));
        }

        //Set Unit of measure in bill of material
        if (Objects.nonNull(billofMaterial.getUnitOfMeasure().getId())) {
            unitOfMeasureRepository.findById(billofMaterial.getUnitOfMeasure().getId())
                .ifPresent(billofMaterial::setUnitOfMeasure);
        }
        else if (Objects.nonNull(billofMaterial.getUnitOfMeasure().getName())) {
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByName(billofMaterial.getUnitOfMeasure().getName());
            if (nonNull(unitOfMeasure)) {
                billofMaterial.setUnitOfMeasure(unitOfMeasure);
            }
        }
        log.info("Created Parent and Child Entities for Bill of Material: {}", billofMaterial);
    }

    /**
     * Find or create the Item entity.
     * @param itemEntity - Item Entity
     * @return - Item Object
     */
    private Item findOrCreateItem(Item itemEntity) {
        log.debug("Finding or Creating Item: {}", itemEntity);
        if (Objects.nonNull(itemEntity) && StringUtils.isNotBlank(itemEntity.getItemName())) {
            Item item = itemRepository.findByItemNameIgnoreCase(itemEntity.getItemName());

            if (Objects.isNull(item)) {
                log.debug("Item not found, creating new item: {}", itemEntity.getItemName());
                item = new Item();
                item.setItemName(itemEntity.getItemName());
                item.setCompany(getLoggedInCompany());
                itemMapper.updateAuditColumns(item);
                item = itemRepository.save(item);
                log.info("Created new item: {}", item);
            } else {
                log.debug("Found existing item: {}", item);
            }
            return item;
        }
        log.warn("Item entity is null or has no name: {}", itemEntity);
        return null;
    }

    /**
     * Gets the loggedIn company.
     *
     * @return loggedIn company.
     */
    private Company getLoggedInCompany() {
        log.debug("Getting Logged In Company");
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        Company company = CompanyMapper.INSTANCE.companyDTOToCompany(companyService.findByName(userMetaData.get("companyName")));
        log.debug("Logged In Company: {}", company);
        return company;
    }

    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        log.debug("Validating and Saving Bill of Materials from sheet");
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        List<BillofMaterial> toSaveBOM = new ArrayList<>();
        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            log.debug("Processing row: {}", rowIndex);
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.BILL_OF_MATERIAL_TOTAL_COLUMNS);
            if (emptyRow) {
                log.debug("Empty row encountered, stopping processing at row: {}", rowIndex);
                break;
            }
            BillofMaterial billofMaterial = parseRow(row, errorDTOs, rowIndex);
            if (errorDTOs.isEmpty()) {
                log.debug("No errors found for row: {}, saving Bill of Material: {}", rowIndex, billofMaterial);
                createParentAndChildEntity(billofMaterial);

                billofMaterial.setCompany(getLoggedInCompany());
                billofMaterialMapper.updateAuditColumns(billofMaterial);
                toSaveBOM.add(billofMaterial);
            } else {
                log.warn("Errors found in row: {}: {}", rowIndex, errorDTOs);
                errorDetailDTOS.addAll(errorDTOs);
            }
        }
        saveEntitiesInBatches(toSaveBOM);
        log.debug("Completed processing sheet with errors: {}", errorDetailDTOS);
        return errorDetailDTOS;
    }

    private BillofMaterial parseRow(Row row, List<ErrorDetailDTO> errorDTOs, int rowIndex) {
        log.debug("Parsing row: {}", rowIndex);
        //Validate Bill of materials
        BillofMaterial billofMaterial = new BillofMaterial();
        for (int columnIndex = 0; columnIndex < Constants.BILL_OF_MATERIAL_TOTAL_COLUMNS; columnIndex++) {

            //getting the respected column name enum by the index
            BillOfMaterialSheet column = BillOfMaterialSheet.getByIndex(columnIndex);
            if (isNull(column)) {
                continue;
            }
            Cell cell = row.getCell(columnIndex);

            //getting the max length of a cell by its enum
            Integer maxLength = column.maxLength;
            switch (column) {
                case Product -> {
                    Optional<String> productError = FileValidationUtil.validateString(cell, maxLength, Boolean.TRUE);
                    if (productError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + productError.get(), rowIndex, columnIndex));
                    } else if (nonNull(cell)) {
                        Item item = new Item();
                        item.setItemName(cell.getStringCellValue());
                        billofMaterial.setProduct(item);
                    }
                }
                case Component -> {
                    Optional<String> componentError = FileValidationUtil.validateString(cell, maxLength, Boolean.TRUE);
                    if (componentError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + componentError.get(), rowIndex, columnIndex));

                    } else if (nonNull(cell)) {
                        Item item = new Item();
                        item.setItemName(cell.getStringCellValue());
                        billofMaterial.setComponent(item);
                    }
                }
                case Quantity -> {
                    Optional<String> quantityError = FileValidationUtil.validateDouble(cell, 0, maxLength, Boolean.TRUE, column.precision);

                    if (quantityError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + quantityError.get(), rowIndex, columnIndex));
                    } else if (nonNull(cell)) {
                        billofMaterial.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()));
                    }
                }
                case UnitOfMeasure -> {
                    Optional<String> uomNameError = FileValidationUtil.validateString(cell, maxLength, Boolean.TRUE);
                    if (uomNameError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + uomNameError.get(), rowIndex, columnIndex));
                    } else if (Objects.nonNull(cell)) {
                        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
                        unitOfMeasure.setName(cell.getStringCellValue());
                        billofMaterial.setUnitOfMeasure(unitOfMeasure);
                    }
                }
                case Yield -> {
                    Optional<String> yieldNameError = FileValidationUtil.validateDouble(cell, 0, maxLength, Boolean.TRUE, column.precision);
                    if (yieldNameError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + yieldNameError.get(), rowIndex, columnIndex));
                    } else if (Objects.nonNull(cell)) {
                        billofMaterial.setYieldFactor(BigDecimal.valueOf(cell.getNumericCellValue()));
                    }
                }
            }
        }
        log.debug("Parsed Bill of Material DTO: {}", billofMaterial);
        return billofMaterial;
    }

    public void saveEntitiesInBatches(List<BillofMaterial> billofMaterials) {
        log.debug("saveEntitiesInBatches called");
        for (int i = 0; i < billofMaterials.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, billofMaterials.size());
            List<BillofMaterial> batchList = billofMaterials.subList(i, endIndex);
            billofMaterialRepository.saveAll(batchList);
            billofMaterialRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
