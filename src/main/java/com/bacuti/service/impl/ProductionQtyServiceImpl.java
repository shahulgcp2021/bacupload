package com.bacuti.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonConstants;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.DateUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Company;
import com.bacuti.domain.Item;
import com.bacuti.domain.ProductionQty;
import com.bacuti.domain.Site;
import com.bacuti.domain.UnitOfMeasure;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.ItemRepository;
import com.bacuti.repository.ProductionQtyRepository;
import com.bacuti.repository.SiteRepository;
import com.bacuti.repository.UnitOfMeasureRepository;
import com.bacuti.service.ProductionQtyService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.ProductionQtyDTO;
import com.bacuti.service.mapper.ItemMapper;
import com.bacuti.service.mapper.ProductionQtyMapper;
import com.bacuti.service.mapper.SiteMapper;
import com.bacuti.service.mapper.UnitofMeasureMapper;

@Service
public class ProductionQtyServiceImpl implements ProductionQtyService {


    private static final String ENTITY_NAME = "ProductionQtyService";

    private final Logger log = LoggerFactory.getLogger(ProductionQtyServiceImpl.class);

    @Autowired
    private ProductionQtyRepository productionQtyRepository;

    @Autowired
    private ProductionQtyMapper productionQtyMapper;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    public ProductionQtyServiceImpl(ProductionQtyRepository productionQtyRepository,
                                    SiteRepository siteRepository,
                                    ItemRepository itemRepository,
                                    UnitOfMeasureRepository unitOfMeasureRepository,
                                    CompanyRepository companyRepository) {
        this.productionQtyRepository = productionQtyRepository;
        this.productionQtyMapper = ProductionQtyMapper.INSTANCE;
        this.siteRepository = siteRepository;
        this.itemRepository = itemRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductionQtyDTO saveProductionQty(ProductionQtyDTO productionQtyDTO) {
        ProductionQty productionQty = productionQtyMapper.productionQtyDTOToProductionQty(productionQtyDTO);
        validateProductionQty(productionQty);
        log.debug(" productionQty to save ::" + productionQty.toString());
        productionQtyMapper.updateAuditColumns(productionQty);
        ProductionQty savedProductionQty = productionQtyRepository.save(productionQty);
        return productionQtyMapper.productionQtyToProductionQtyDTO(savedProductionQty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProductionQtyById(Long id) {
        if (!productionQtyRepository.existsById(id)) {
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        productionQtyRepository.deleteById(id);
    }

    /**
     * Validates the given ProductionQtyDTO to ensure all required fields are present and valid.
     *
     * @param productionQty - ProductionQty object to validate.
     */
    private void validateProductionQty(ProductionQty productionQty) {
        Company company = getCurrentUserCompany();
        productionQty.setCompany(company);
        if (Objects.nonNull(productionQty.getProduct()) && Objects.nonNull(productionQty.getProduct().getItemName())) {
            Item item = itemRepository.findByItemNameIgnoreCase(productionQty.getProduct().getItemName());
            if (Objects.isNull(item)) {
                ItemMapper.INSTANCE.updateAuditColumns(productionQty.getProduct());
                item = itemRepository.save(productionQty.getProduct());
            }
            productionQty.setProduct(item);
        }

        if (Objects.nonNull(productionQty.getSite()) && Objects.nonNull(productionQty.getSite().getSiteName())) {
            Site site = siteRepository.findBySiteNameIgnoreCase(productionQty.getSite().getSiteName());
            if (Objects.isNull(site)) {
                SiteMapper.INSTANCE.updateAuditColumns(productionQty.getSite());
                site = siteRepository.save(productionQty.getSite());
            }
            productionQty.setSite(site);
        }

        if (Objects.nonNull(productionQty.getUnitOfMeasure()) && Objects.nonNull(productionQty.getUnitOfMeasure().getName())) {
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByName(productionQty.getUnitOfMeasure().getName());
            if (Objects.isNull(unitOfMeasure)) {
                UnitofMeasureMapper.INSTANCE.updateAuditColumns(productionQty.getUnitOfMeasure());
                unitOfMeasure = unitOfMeasureRepository.save(productionQty.getUnitOfMeasure());
            }
            productionQty.setUnitOfMeasure(unitOfMeasure);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ProductionQtyDTO> getAllProductionQties(Pageable pageable) {
        Page<ProductionQty> pageProductionQty = productionQtyRepository.findAll(pageable);
        return pageProductionQty.map(productionQtyMapper::productionQtyToProductionQtyDTO);
    }

    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ProductionQty> productionQuantities = new ArrayList<>();
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<ErrorDetailDTO>();
        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.PRODUCTION_QUANTITY_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            ProductionQty productionQty = parseRow(row, errorDTOs, rowIndex);
            if (CollectionUtils.isEmpty(errorDTOs)) {
                try {
                    validateProductionQty(productionQty);
                    productionQtyMapper.updateAuditColumns(productionQty);
                    productionQuantities.add(productionQty);
                } catch (Exception e) {
                    errorDTOs.add(FileValidationUtil.generateErrorDTO(CommonConstants.PRODUCTIONQTY_SAVE_ERROR, rowIndex, 0));
                }
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        saveEntitiesInBatches(productionQuantities);
        return errorDetailDTOS;
    }

    private ProductionQty parseRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        ProductionQty productionQty = new ProductionQty();
        log.debug("row " + rowIndex);
        Cell cell0 = row.getCell(0);
        //Validate Production Date
        Optional<String> productionDateError = FileValidationUtil.validateDate(cell0, Boolean.TRUE);
        if (productionDateError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Production Date: " + productionDateError.get(), rowIndex, 0));
        } else {
            LocalDate productionQuantityDate = DateUtil.convertToLocalDateFromString(cell0.getStringCellValue());
            if (Objects.nonNull(productionQuantityDate)) {
                productionQty.setProductionnDate(productionQuantityDate);
            } else {
                errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Production Date: " + "Not a valid Date Format, expected format is DD/MMM/YYYY", rowIndex, 0));
            }
        }
        // Validate Site name
        Cell cell1 = row.getCell(1);
        Optional<String> siteError = FileValidationUtil.validateString(cell1, 1000, Boolean.FALSE);
        if (siteError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Site name: " + siteError.get(), rowIndex, 1));
        else if (Objects.nonNull(cell1) && cell1.getCellType() != CellType.BLANK) {
            Site site = new Site();
            site.setSiteName(cell1.getStringCellValue());
            productionQty.setSite(site);
        }
        // Validate item name
        Cell cell2 = row.getCell(2);
        Optional<String> itemNameError = FileValidationUtil.validateString(cell2, 1000, Boolean.FALSE);
        if (itemNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item name: " + itemNameError.get(), rowIndex, 2));
        } else if (Objects.nonNull(cell2) && cell1.getCellType() != CellType.BLANK) {
            Item item = new Item();
            item.setItemName(cell2.getStringCellValue());
            productionQty.setProduct(item);
        }

        // Validate quantity
        Cell cell3 = row.getCell(3);
        Optional<String> quantityError = FileValidationUtil.validateInteger(cell3, 1, 1000000000, Boolean.TRUE);
        if (quantityError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Production Quantity: " + quantityError.get(), rowIndex, 3));
        } else {
            productionQty.setQuantity(BigDecimal.valueOf(cell3.getNumericCellValue()));
        }

        // Validate unit of measure name
        Cell cell4 = row.getCell(4);
        Optional<String> uomNameError = FileValidationUtil.validateString(cell4, 1000, Boolean.FALSE);
        if (uomNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("UOM: " + uomNameError.get(), rowIndex, 4));
        } else if (Objects.nonNull(cell4) && cell1.getCellType() != CellType.BLANK) {
            UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure.setName(cell4.getStringCellValue());
            productionQty.setUnitOfMeasure(unitOfMeasure);
        }

        // Validate heat number
        Cell cell5 = row.getCell(5);
        Optional<String> heatNumberError = FileValidationUtil.validateInteger(cell5, 1, 10000, Boolean.FALSE);
        if (heatNumberError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Heat Number: " + heatNumberError.get(), rowIndex, 5));
        } else {
            productionQty.setHeatNumber(Objects.nonNull(cell5) ? String.valueOf(cell5.getNumericCellValue()) : null);
        }
        log.debug(" productionQty :: " + productionQty.toString());
        return productionQty;
    }


    private Company getCurrentUserCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyRepository.findByName(userMetaData.get("companyName"));
    }

    /**
     * Method used to save the entries by batch wise.
     *
     * @param productionQuantities - List of Production Quantity entity
     */
    public void saveEntitiesInBatches(List<ProductionQty> productionQuantities) {
        log.debug("Request to save production quantity in batches");
        for (int i = 0; i < productionQuantities.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, productionQuantities.size());
            List<ProductionQty> batchList = productionQuantities.subList(i, endIndex);
            productionQtyRepository.saveAll(batchList);
            productionQtyRepository.flush();
        }
    }
}

