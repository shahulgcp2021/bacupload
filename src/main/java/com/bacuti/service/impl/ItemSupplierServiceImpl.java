package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Item;
import com.bacuti.domain.ItemSupplier;
import com.bacuti.domain.Supplier;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.ItemRepository;
import com.bacuti.repository.ItemSupplierRepository;
import com.bacuti.repository.SupplierRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.ItemSupplierService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.ItemSupplierDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.ItemMapper;
import com.bacuti.service.mapper.ItemSupplierMapper;
import com.bacuti.service.mapper.SupplierMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
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
import com.bacuti.domain.Company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service("ItemSupplierService")
public class ItemSupplierServiceImpl implements ItemSupplierService {

    private final Logger log = LoggerFactory.getLogger(ItemSupplierServiceImpl.class);

    @Autowired
    ItemSupplierMapper itemSupplierMapper;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ItemSupplierRepository itemSupplierRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemSupplierDTO createItemSupplier(ItemSupplierDTO itemSupplierDTO) {
        log.debug("REST request to save ItemSupplier");
        // Validating the input params
        validateItemSupplier(itemSupplierDTO);
        ItemSupplier itemSupplier = itemSupplierMapper.itemSupplierDTOToItemSupplier(itemSupplierDTO);
        // Check item exist or create new item
        Item item = findOrCreateItem(itemSupplier.getItem());
        itemSupplier.setItem(item);
        // Check Supplier exist or create a new item.
        Supplier supplier = findOrCreateSupplier(itemSupplier.getSupplier());
        itemSupplier.setSupplier(supplier);

        checkForExistingItemSupplier(itemSupplierDTO, itemSupplier);
        itemSupplier.setCompany(getLoggedInCompany());
        itemSupplierMapper.updateAuditColumns(itemSupplier);
        ItemSupplier itemServiceObj = itemSupplierRepository.save(itemSupplier);
        log.debug("ItemSupplier Created");
        return itemSupplierMapper.itemSupplierToItemSupplierDTO(itemServiceObj);
    }

    /**
     * Method used to validate the input item supplier params.
     * @param itemSupplierDTO - Input supplierDTO
     */
    private void validateItemSupplier(ItemSupplierDTO itemSupplierDTO) {
        log.debug("validateItemSupplier called");
        if (Objects.isNull(itemSupplierDTO.getSupplierOwnItem()) || StringUtils.isBlank(itemSupplierDTO.getSupplierOwnItem()))
            throw new BusinessException("Supplier_Own_Item should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(itemSupplierDTO.getSupplierMix()))
            throw new BusinessException("Supplier_Mix should not be null", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(itemSupplierDTO.getItem()) || Objects.isNull(itemSupplierDTO.getItem().getItemName())
                || StringUtils.isBlank(itemSupplierDTO.getItem().getItemName()))
            throw new BusinessException("Item_Name should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(itemSupplierDTO.getSupplier()) || Objects.isNull(itemSupplierDTO.getSupplier().getSupplierName())
                || StringUtils.isBlank(itemSupplierDTO.getSupplier().getSupplierName()))
            throw new BusinessException("Supplier_Name should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.nonNull(itemSupplierDTO.getId()) && !itemSupplierRepository.existsById(itemSupplierDTO.getId()))
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());
    }

    /**
     * Find or create the Item entity.
     * @param itemEntity - Item Entity
     * @return - Item Object
     */
    private Item findOrCreateItem(Item itemEntity) {
        if (Objects.nonNull(itemEntity) && StringUtils.isNotBlank(itemEntity.getItemName())) {
            Item item = itemRepository.findByItemNameIgnoreCase(itemEntity.getItemName());

            if (Objects.isNull(item)) {
                item = new Item();
                item.setItemName(itemEntity.getItemName());
                item.setCompany(getLoggedInCompany());
                itemMapper.updateAuditColumns(item);
                item = itemRepository.save(item);
            }
            return item;
        }
        return null;
    }

    /**
     * Find or create the supplier entity
     * @param supplierEntity - Supplier Entity
     * @return - Supplier Object
     */
    private Supplier findOrCreateSupplier(Supplier supplierEntity) {
        if (Objects.nonNull(supplierEntity) && StringUtils.isNotBlank(supplierEntity.getSupplierName())) {
            Supplier supplier = supplierRepository.findBySupplierNameIgnoreCase(supplierEntity.getSupplierName());

            if (Objects.isNull(supplier)) {
                supplier = new Supplier();
                supplier.setSupplierName(supplierEntity.getSupplierName());
                supplier.setCompany(getLoggedInCompany());
                supplierMapper.updateAuditColumns(supplier);
                supplier = supplierRepository.save(supplier);
            }
            return supplier;
        }
        return null;
    }

    /**
     * Method used to check item and supplier exist.
     * @param itemSupplierDTO - Item Supplier DTO.
     * @param itemSupplier - Item supplier object.
     */
    private void checkForExistingItemSupplier(ItemSupplierDTO itemSupplierDTO, ItemSupplier itemSupplier) {
        log.debug("checkForExistingItemSupplier called");
        ItemSupplier existingItemSupplier = itemSupplierRepository.findBySupplierIdAndItemId(
            itemSupplier.getSupplier().getId(), itemSupplier.getItem().getId());

        if (Objects.nonNull(existingItemSupplier)) {
            boolean differentId = Objects.nonNull(itemSupplierDTO.getId()) && !Objects.equals(existingItemSupplier.getId(), itemSupplierDTO.getId());
            boolean idIsNull = Objects.isNull(itemSupplierDTO.getId());

            if (differentId || idIsNull) {
                throw new BusinessException("Supplier Mix with the same Item Supplied(" + itemSupplierDTO.getItem().getItemName()
                    + ") and Supplier Name(" + itemSupplierDTO.getSupplier().getSupplierName() + ") name already exists.",
                    HttpStatus.BAD_REQUEST.value());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteItemSupplierById(Long id) {
        log.debug("deleteItemSupplierById called");
        if (!itemSupplierRepository.existsById(id)) {
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());
        }
        itemSupplierRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ItemSupplierDTO> getItemSupplier(int pageNo, int pageSize, String sortBy, String sortDirection, String supplierOwnItem) {
        log.debug("getItemSupplier called");
        Page<ItemSupplier> pageSite;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(supplierOwnItem)) {
            pageSite = itemSupplierRepository.findAll(pageable);
        } else {
            pageSite = itemSupplierRepository.findBySupplierOwnItemContaining(supplierOwnItem, pageable);
        }
        log.debug("GetItemSupplier called with supplierOwnItem");
        return pageSite.map(itemSupplierMapper::itemSupplierToItemSupplierDTO);
    }

    /**
     * Gets the loggedIn company.
     *
     * @return loggedIn company.
     */
    private Company getLoggedInCompany() {
        log.debug("REST request to get Logged In Company");
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return CompanyMapper.INSTANCE.companyDTOToCompany(companyService.findByName(userMetaData.get("companyName")));
    }

    /**
     * Method used to process the input sheet and save the entity
     *
     * @param sheet - Input sheet from upload
     * @return - Returns the error detail dto
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ItemSupplier> itemSupplierList = new ArrayList<>();
        log.debug("REST request to validate And Save XSSFSheet");
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();

        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.ITEM_SUPPLIER_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            ItemSupplier itemSupplier = validateRow(row, errorDTOs, rowIndex);

            if (errorDTOs.isEmpty()) {
                createParentAndChildEntity(itemSupplier);
                itemSupplier.setCompany(getLoggedInCompany());
                ItemSupplier existingItemSupplier = itemSupplierRepository.findBySupplierIdAndItemId(
                    itemSupplier.getSupplier().getId(), itemSupplier.getItem().getId());

                if (Objects.nonNull(existingItemSupplier))
                    itemSupplier.setId(existingItemSupplier.getId());
                itemSupplierMapper.updateAuditColumns(itemSupplier);
                itemSupplierList.add(itemSupplier);
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        saveEntitiesInBatches(itemSupplierList);
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
    private ItemSupplier validateRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        log.debug("validateRow called");
        ItemSupplier itemSupplier = new ItemSupplier();

        // Validate item name
        Cell cell0 = row.getCell(0);
        Optional<String> errorItemName = FileValidationUtil.validateString(cell0, 1000, Boolean.TRUE);
        if (errorItemName.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Supplied: " + errorItemName.get(), rowIndex, 0));
        } else if(Objects.nonNull(cell0)) {
            Item item = new Item();
            item.setItemName(cell0.getStringCellValue());
            itemSupplier.setItem(item);
        }

        // Validate Supplier
        Cell cell1 = row.getCell(1);
        Optional<String> errorSupplier = FileValidationUtil.validateString(cell1, 255, Boolean.TRUE);
        if (errorSupplier.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Supplier Name: " + errorSupplier.get(), rowIndex, 1));
        } else if(Objects.nonNull(cell1)) {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(cell1.getStringCellValue());
            itemSupplier.setSupplier(supplier);
        }
        validateAndSetString(row, errorDetailDTOS, rowIndex, 2, itemSupplier::setSupplierOwnItem, "Supplier Own Item", Boolean.TRUE);
        validateAndSetDouble(row, 3, itemSupplier::setSupplierMix, "Supplier Mix:", errorDetailDTOS);
        return itemSupplier;
    }

    /**
     * Method used to validate each cell and add the error message
     *
     * @param row - Row from the file
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @param cellIndex - Cell index of each row
     * @param setter - Setter key to set the value
     * @param fieldName - Field name to check
     * @param mandate - Mandatory check for field
     */
    private void validateAndSetString(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, int cellIndex,
                                      Consumer<String> setter, String fieldName, Boolean mandate) {
        log.debug("REST request to validate Row : {}", rowIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateString(cell, 255, mandate);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + error.get(), rowIndex, cellIndex));
        } else {
            setter.accept(Objects.nonNull(cell) ? cell.getStringCellValue() : null);
        }
    }


    /**
     * Method to validate cell value and set it to a BigDecimal field or add error to list
     *
     * @param row - Row from the file
     * @param cellIndex - Cell index of each row
     * @param setter - Setter key to set the value
     * @param errorMessagePrefix - Field name to check
     * @param errorDetailDTOS - To add the error details if error exist.
     */
    private void validateAndSetDouble(Row row, int cellIndex, Consumer<BigDecimal> setter, String errorMessagePrefix, List<ErrorDetailDTO> errorDetailDTOS) {
        log.debug("REST request to validate Row : {}", cellIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateDouble(cell, -1000000, 1000000, false, 2);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(errorMessagePrefix + error.get(), row.getRowNum(), cellIndex));
        } else {
            setter.accept(Objects.nonNull(cell) ? BigDecimal.valueOf(cell.getNumericCellValue()) : null);
        }
    }


    /**
     * Method used to create the child or parent entity for current entity.
     *
     * @param itemSupplier - Item Supplier entity
     */
    private void createParentAndChildEntity(ItemSupplier itemSupplier) {
        log.debug("REST request to create ParentAndChildEntity");
        //Check Supplier exist or create new entity
        if (Objects.nonNull(itemSupplier.getSupplier()))
            itemSupplier.setSupplier(findOrCreateSupplier(itemSupplier.getSupplier()));

        //Check Item exist or create new entity
        if (Objects.nonNull(itemSupplier.getItem()))
            itemSupplier.setItem(findOrCreateItem(itemSupplier.getItem()));
    }

    /**
     * Method used to save the entries by batch wise
     *
     * @param itemSupplierList - List of item supplier entity
     */
    public void saveEntitiesInBatches(List<ItemSupplier> itemSupplierList) {
        log.debug("saveEntitiesInBatches called");
        for (int i = 0; i < itemSupplierList.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, itemSupplierList.size());
            List<ItemSupplier> batchList = itemSupplierList.subList(i, endIndex);
            itemSupplierRepository.saveAll(batchList);
            itemSupplierRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
