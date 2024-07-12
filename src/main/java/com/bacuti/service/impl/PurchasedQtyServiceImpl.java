package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonConstants;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.DateUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.*;
import com.bacuti.repository.*;
import com.bacuti.service.PurchasedQtyService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.PurchasedQtyDTO;
import com.bacuti.service.mapper.*;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PurchasedQtyServiceImpl implements PurchasedQtyService {

    private static final String ENTITY_NAME = "PurchasedQtyService";

    private final Logger log = LoggerFactory.getLogger(PurchasedQtyServiceImpl.class);

    @Autowired
    PurchasedQtyRepository purchasedQtyRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    ItemSupplierRepository itemSupplierRepository;


    @Autowired
    public PurchasedQtyServiceImpl(PurchasedQtyRepository purchasedQtyRepository, ItemRepository itemRepository, UnitOfMeasureRepository unitOfMeasureRepository, ItemSupplierRepository itemSupplierRepository) {
        this.purchasedQtyRepository = purchasedQtyRepository;
        this.itemRepository = itemRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.itemSupplierRepository = itemSupplierRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PurchasedQtyDTO savePurchasedQty(PurchasedQtyDTO purchasedQtyDTO) {

        PurchasedQty purchasedQty = PurchasedQtyMapper.INSTANCE.purchasedQtyDTOToPurchasedQty(purchasedQtyDTO);
        validatePurchasedQty(purchasedQty);
        log.debug(" purchasedQty to save ::" + purchasedQty.toString());
        PurchasedQtyMapper.INSTANCE.updateAuditColumns(purchasedQty);
        PurchasedQty savedPurchasedQty = purchasedQtyRepository.save(purchasedQty);
        return PurchasedQtyMapper.INSTANCE.purchasedQtyToPurchasedQtyDTO(savedPurchasedQty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePurchasedQtyById(Long id) {
        if (!purchasedQtyRepository.existsById(id)) {
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        purchasedQtyRepository.deleteById(id);
    }

    /**
     * Validates the given PurchasedQtyDTO to ensure all required fields are present and valid.
     *
     * @param purchasedQty - PurchasedQty object to validate.
     */
    private void validatePurchasedQty(PurchasedQty purchasedQty) {
        //Set login company details
        purchasedQty.setCompany(getCurrentUserCompany());

        if (Objects.nonNull(purchasedQty.getItem()) && Objects.nonNull(purchasedQty.getItem().getItemName())) {
            Item item = itemRepository.findByItemNameIgnoreCase(purchasedQty.getItem().getItemName());
            if (Objects.isNull(item)) {
                item = new Item();
                item.setCompany(getCurrentUserCompany());
                ItemMapper.INSTANCE.updateAuditColumns(purchasedQty.getItem());
                item = itemRepository.save(purchasedQty.getItem());
            }
            purchasedQty.setItem(item);
        }
        if (Objects.nonNull(purchasedQty.getUnitOfMeasure()) && Objects.nonNull(purchasedQty.getUnitOfMeasure().getName())) {
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByName(purchasedQty.getUnitOfMeasure().getName());
            if (Objects.isNull(unitOfMeasure)) {
                UnitofMeasureMapper.INSTANCE.updateAuditColumns(purchasedQty.getUnitOfMeasure());
                unitOfMeasure = unitOfMeasureRepository.save(purchasedQty.getUnitOfMeasure());
            }
            purchasedQty.setUnitOfMeasure(unitOfMeasure);
        }
        if (Objects.nonNull(purchasedQty.getItemSupplier()) && Objects.nonNull(purchasedQty.getItemSupplier().getSupplierOwnItem())) {
            ItemSupplier itemSupplier = itemSupplierRepository.findBySupplierOwnItem(purchasedQty.getItemSupplier().getSupplierOwnItem());
            if (Objects.isNull(itemSupplier)) {
                itemSupplier = new ItemSupplier();
                itemSupplier.setCompany(getCurrentUserCompany());
                ItemSupplierMapper.INSTANCE.updateAuditColumns(purchasedQty.getItemSupplier());
                itemSupplier = itemSupplierRepository.save(purchasedQty.getItemSupplier());
            }
            purchasedQty.setItemSupplier(itemSupplier);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PurchasedQtyDTO> getAllPurchasedQties(Pageable pageable) {
        Page<PurchasedQty> pagePurchasedQty = purchasedQtyRepository.findAll(pageable);
        return pagePurchasedQty.map(PurchasedQtyMapper.INSTANCE::purchasedQtyToPurchasedQtyDTO);
    }


    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<PurchasedQty> purchasedQtyList = new ArrayList<>();
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.PURCHASED_QUANTITY_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            PurchasedQty purchasedQty = parseRow(row, errorDTOs, rowIndex);
            if (CollectionUtils.isEmpty(errorDTOs)) {
                try {
                    validatePurchasedQty(purchasedQty);
                    PurchasedQtyMapper.INSTANCE.updateAuditColumns(purchasedQty);
                    purchasedQtyList.add(purchasedQty);
                }
                catch (Exception e){
                    errorDTOs.add(FileValidationUtil.generateErrorDTO(CommonConstants.PURCHASEDQTY_SAVE_ERROR,rowIndex,0));
                }
            } else {
                errorDetailDTOS.addAll(errorDTOs);
            }
        }

        if (!CollectionUtils.isEmpty(purchasedQtyList))
            saveEntitiesInBatches(purchasedQtyList);

        return errorDetailDTOS;
    }

    private PurchasedQty parseRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        PurchasedQty purchaseQty = new PurchasedQty();
        log.debug("row " + rowIndex);

        // Validate Purchase Date
        Cell cell0 = row.getCell(0);
        Optional<String> purchaseDateError = FileValidationUtil.validateDate(cell0, Boolean.TRUE);
        if (purchaseDateError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Purchase Date: " + purchaseDateError.get(), rowIndex, 0));
        } else {
            LocalDate purchaseQuantityDate = DateUtil.convertToLocalDateFromString(cell0.getStringCellValue());
            if (Objects.nonNull(purchaseQuantityDate)) {
                purchaseQty.setPurchaseDate(purchaseQuantityDate);
            } else {
                errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Production Date: " + "Not a valid Date Format, expected format is DD/MMM/YYYY", rowIndex, 0));
            }
        }

        // Validate Item purchased
        Cell cell1 = row.getCell(1);
        Optional<String> itemNameError = FileValidationUtil.validateString(cell1, 1000, Boolean.TRUE);
        if (itemNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Purchased: " + itemNameError.get(), rowIndex, 1));
        } else if (Objects.nonNull(cell1) && cell1.getCellType() != CellType.BLANK) {
            Item item = new Item();
            item.setItemName(cell1.getStringCellValue());
            purchaseQty.setItem(item);
        }

        // Validate Supplier
        Cell cell2 = row.getCell(2);
        Optional<String> itemSupplierError = FileValidationUtil.validateString(cell2, 1000, Boolean.TRUE);
        if (itemSupplierError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Supplier: " + itemSupplierError.get(), rowIndex, 2));
        } else if (Objects.nonNull(cell2) && cell2.getCellType() != CellType.BLANK) {
            ItemSupplier itemSupplier = new ItemSupplier();
            itemSupplier.setSupplierOwnItem(cell2.getStringCellValue());
            purchaseQty.setItemSupplier(itemSupplier);
        }

        // Validate Quantity
        Cell cell3 = row.getCell(3);
        Optional<String> quantityError = FileValidationUtil.validateDouble(cell3, 0, 1000000000, Boolean.TRUE, 8);
        if (quantityError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Quantity: " + quantityError.get(), rowIndex, 3));
        } else {
            purchaseQty.setQuantity(BigDecimal.valueOf(cell3.getNumericCellValue()));
        }

        // Validate Unit of Measure name
        Cell cell4 = row.getCell(4);
        Optional<String> uomNameError = FileValidationUtil.validateString(cell4, 1000, Boolean.TRUE);
        if (uomNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("UOM: " + uomNameError.get(), rowIndex, 4));
        } else if (Objects.nonNull(cell4) && cell4.getCellType() != CellType.BLANK) {
            UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure.setName(cell4.getStringCellValue());
            purchaseQty.setUnitOfMeasure(unitOfMeasure);
        }
        log.debug(" purchaseQty :: " + purchaseQty.toString());
        return purchaseQty;
    }


    private Company getCurrentUserCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyRepository.findByName(Objects.nonNull(userMetaData) ? userMetaData.get("companyName") : "");
    }

    /**
     * Method used to save the entries by batch wise
     *
     * @param purchasedQtyList - List of purchase qty list
     */
    public void saveEntitiesInBatches(List<PurchasedQty> purchasedQtyList) {
        log.debug("saveEntitiesInBatches called");
        for (int i = 0; i < purchasedQtyList.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, purchasedQtyList.size());
            List<PurchasedQty> batchList = purchasedQtyList.subList(i, endIndex);
            purchasedQtyRepository.saveAll(batchList);
            purchasedQtyRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
