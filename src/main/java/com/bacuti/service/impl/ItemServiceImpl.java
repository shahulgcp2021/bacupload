package com.bacuti.service.impl;


import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Company;
import com.bacuti.domain.DefaultAverageEF;
import com.bacuti.domain.Item;
import com.bacuti.domain.UnitOfMeasure;
import com.bacuti.domain.enumeration.AggregatedGoodsCategory;
import com.bacuti.domain.enumeration.ItemType;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.DefaultAverageEFRepository;
import com.bacuti.repository.ItemRepository;
import com.bacuti.repository.UnitOfMeasureRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.ItemService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.ItemDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.ItemMapper;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
public class ItemServiceImpl implements ItemService {

    private static final String ENTITY_NAME = "uploadServiceItem";

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private final CompanyService companyService;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    private DefaultAverageEFRepository defaultAverageEFRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public ItemServiceImpl(ItemRepository itemRepository, CompanyService companyService, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.companyService = companyService;
        this.itemMapper = itemMapper;
    }

    /**
     * {@inheritDoc}
     */
    public ItemDTO saveItem(ItemDTO itemDTO) {
        if (itemDTO.getId() != null) {
            // Check if the Item exists when id is provided
            itemRepository.findById(itemDTO.getId())
                .orElseThrow(() -> new BusinessException("Item Not found", HttpStatus.BAD_REQUEST.value()));
        }
        if (itemRepository.existsByItemName(itemDTO.getItemName()) && ((itemDTO.getId() == null) || !Objects.equals(itemRepository.getIdByItemName(itemDTO.getItemName()), itemDTO.getId()))) {
            // Check if Item name already exists when creating and updating a Item
            throw new BusinessException("Item with the name '" + itemDTO.getItemName() + "' already exists.", HttpStatus.BAD_REQUEST.value());
        }

        validateItemDTO(itemDTO);
        Item itemObject = itemMapper.itemDTOToItem(itemDTO);
        // Update audit columns
        itemMapper.updateAuditColumns(itemObject);
        Item itemObj = itemRepository.save(itemObject);
        return itemMapper.itemToItemDTO(itemObj);
    }

    @Override
    public ItemDTO saveItemForOtherEntities(ItemDTO itemDTO) {
        Item item = itemMapper.itemDTOToItem(itemDTO);
        item.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany(companyService)));
        itemMapper.updateAuditColumns(item);
        return itemMapper.itemToItemDTO(itemRepository.save(item));
    }

    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= Constants.ITEM_TOTAL_COLUMNS; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.ITEM_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Item item = parseRow(row, errorDTOs, rowIndex);
            log.debug("The error dto size is {}", errorDTOs.size());
            if (errorDTOs.size() == 0) {
                ItemMapper.INSTANCE.updateAuditColumns(item);
                try {
                    Item dbItem = itemRepository.findByItemNameIgnoreCase(item.getItemName());
                    if (dbItem != null) item.setId(dbItem.getId());
                    itemRepository.save(item);
                } catch (Exception e) {
                    errorDTOs.add(new ErrorDetailDTO(rowIndex, 0, "DB error while saving Item - " + e.getMessage()));
                }
            } else {
                errorDetailDTOS.addAll(errorDTOs);
            }
        }
        return errorDetailDTOS;
    }

    private boolean isRowEmpty(Row row) {
        for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }



private Item parseRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
    Item item = new Item();
    log.debug("row " + rowIndex);


    //Validate Item Name
    Cell cell0 = row.getCell(0);
    Optional<String> errorString = FileValidationUtil.validateString(cell0, 1000, Boolean.TRUE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Name:" + errorString.get(), rowIndex, 0));
    else
        item.setItemName(cell0.getStringCellValue());

    // Validate Item Description
    Cell cell1 = row.getCell(1);
    errorString = FileValidationUtil.validateString(cell1, 1000, Boolean.TRUE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Description:" + errorString.get(), rowIndex, 1));
    else
        item.setDescription(cell1.getStringCellValue());

    // Validate item name
    Cell cell2 = row.getCell(2);

    errorString = FileValidationUtil.validateString(cell2, 255, Boolean.TRUE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Type:" + errorString.get(), rowIndex, 2));
    else if (!(cell2.getCellType() == CellType.BLANK)){
        List<ItemType> itemTypes = Arrays.stream(ItemType.values())
            .filter(itemType -> itemType.getDisplayName().equalsIgnoreCase(cell2.getStringCellValue())).collect(Collectors.toList());
        if (!itemTypes.isEmpty()) {
            item.setItemType(itemTypes.get(0));
        } else {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Type: Not valid", rowIndex, 2));
        }
    }

    // Validate Item Category
    Cell cell3 = row.getCell(3);
    errorString = FileValidationUtil.validateString(cell3, 255, Boolean.FALSE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Item Category:" + errorString.get(), rowIndex, 3));
    else if(Objects.nonNull(cell3))
        item.setItemCategory(cell3.getStringCellValue());


    // Validate unit of measure name
    Cell cell4 = row.getCell(4);
    errorString = FileValidationUtil.validateString(cell4, 255, Boolean.FALSE);
    if (errorString.isPresent()) {
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("UOM: " + errorString.get(), rowIndex, 4));
    } else if (Objects.nonNull(cell4)) {

        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByValueIgnoreCase(cell4.getStringCellValue());
        if (Objects.isNull(unitOfMeasure)) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("UOM: " + "unitOfMeasure name not found", rowIndex, 4));
        } else {
            item.setUnitOfMeasure(item.getUnitOfMeasure());
        }

    }

    // Validate Purchased Item
    Cell cell5 =row.getCell(5);
    errorString = FileValidationUtil.validateBoolean(cell5,  Boolean.TRUE);
    if (errorString.isPresent()) {
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Purchased Item: " + errorString.get(), rowIndex, 5));
    } else if (!(cell5.getCellType() == CellType.BLANK)){
        item.setPurchasedItem(FileValidationUtil.convertNumericToBoolean(cell5));
    }

    // Validate Supplier Emissions Multiplier
    Cell cell6= row.getCell(6);
    errorString = FileValidationUtil.validateDouble(cell6, 0, 999999999, Boolean.FALSE,2);
    if (errorString.isPresent()) {
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Supplier Emissions Multiplier: " + errorString.get(), rowIndex, 6));
    } else if(Objects.nonNull(cell6)){
        item.setSupplierEmissionMultipler(BigDecimal.valueOf(cell6.getNumericCellValue()));
    }

    // Validate CBAM Impacted
    Cell cell7 =row.getCell(7);
    errorString = FileValidationUtil.validateBoolean(cell7,  Boolean.FALSE);
    if (errorString.isPresent()) {
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("CBAM Impacted: " + errorString.get(), rowIndex, 7));
    }
    else if (Objects.nonNull(cell7) &&!(cell7.getCellType() == CellType.BLANK)){
        item.setCbamImpacted(FileValidationUtil.convertNumericToBoolean(cell7));
    }

    // Validate CN Code
    Cell cell8=row.getCell(8);
    errorString = FileValidationUtil.validateString(cell8, 255, Boolean.FALSE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("CN Code:" + errorString.get(), rowIndex, 8));
    else if(Objects.nonNull(cell8))
        item.setCnCode(cell8.getStringCellValue());

    // Validate CN Name
    Cell cell9=row.getCell(9);
    errorString = FileValidationUtil.validateString(cell9, 255, Boolean.FALSE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("CN Name:" + errorString.get(), rowIndex, 9));
    else if(Objects.nonNull(cell9))
        item.setCnName(cell9.getStringCellValue());

    // Validate PercentMn
    Cell cell10=row.getCell(10);
    errorString = FileValidationUtil.validateDouble(cell10, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentMn:" + errorString.get(), rowIndex, 10));
    else if(Objects.nonNull(cell10))
        item.setPercentMn(BigDecimal.valueOf(cell10.getNumericCellValue()));

    // Validate percentCr
    Cell cell11=row.getCell(11);
    errorString = FileValidationUtil.validateDouble(cell11, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentCr:" + errorString.get(), rowIndex, 11));
    else if(Objects.nonNull(cell11))
        item.setPercentCr(BigDecimal.valueOf(cell11.getNumericCellValue()));


    // Validate PercentNi
    Cell cell12=row.getCell(12);
    errorString = FileValidationUtil.validateDouble(cell12, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentNi:" + errorString.get(), rowIndex, 12));
    else if(Objects.nonNull(cell12))
        item.setPercentNi(BigDecimal.valueOf(cell12.getNumericCellValue()));


    // Validate PercentCarbon
    Cell cell13=row.getCell(13);
    errorString = FileValidationUtil.validateDouble(cell13, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentCarbon:" + errorString.get(), rowIndex, 13));
    else if(Objects.nonNull(cell13))
        item.setPercentCarbon(BigDecimal.valueOf(cell13.getNumericCellValue()));

    // Validate PercentOtherAlloys
    Cell cell14=row.getCell(14);
    errorString = FileValidationUtil.validateDouble(cell14, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentOtherAlloys:" + errorString.get(), rowIndex, 14));
    else if(Objects.nonNull(cell14))
        item.setPercentOtherAlloys(BigDecimal.valueOf(cell14.getNumericCellValue()));


    // Validate PercentOtherMaterials
    Cell cell15=row.getCell(15);
    errorString = FileValidationUtil.validateDouble(cell15, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentOtherMaterials:" + errorString.get()
            , rowIndex, 15));
    else if(Objects.nonNull(cell15))
        item.setPercentOtherMaterials(BigDecimal.valueOf(cell15.getNumericCellValue()));


    // Validate PercentPreconsumerScrap
    Cell cell16=row.getCell(16);
    errorString = FileValidationUtil.validateDouble(cell16, 1, 100, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("PercentPreconsumerScrap:" + errorString.get(),
            rowIndex, 16));
    else if(Objects.nonNull(cell16))
        item.setPercentPreconsumerScrap(BigDecimal.valueOf(cell16.getNumericCellValue()));


    // Validate ScrapPerItem
    Cell cell17=row.getCell(17);
    errorString = FileValidationUtil.validateDouble(cell17, 0, 999999999, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("ScrapPerItem:" + errorString.get(), rowIndex, 17));
    else if(Objects.nonNull(cell17))
        item.setScrapPerItem(BigDecimal.valueOf(cell17.getNumericCellValue()));


    // Validate EfUnits
    Cell cell18=row.getCell(18);
    errorString = FileValidationUtil.validateDouble(cell18, 0, 999999999, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("EfUnits:" + errorString.get(), rowIndex, 18));
    else if(Objects.nonNull(cell18))
        item.setEfUnits(BigDecimal.valueOf(cell18.getNumericCellValue()));

    // Validate EfScalingFactor
    Cell cell19=row.getCell(19);
    errorString = FileValidationUtil.validateDouble(cell19, 0, 999999999, Boolean.FALSE,2);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("EfScalingFactor:" + errorString.get(), rowIndex, 19));
    else if(Objects.nonNull(cell19))
        item.setEfScalingFactor(BigDecimal.valueOf(cell19.getNumericCellValue()));

    // Validate AggregatedGoodsCategory
    Cell cell20=row.getCell(20);

    errorString = FileValidationUtil.validateString(cell20, 255, Boolean.FALSE);
    if (errorString.isPresent())
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("AggregatedGoodsCategory:" + errorString.get(), rowIndex, 2));
    else if (Objects.nonNull(cell20) && !(cell20.getCellType() == CellType.BLANK)){
        List<AggregatedGoodsCategory> categories = Arrays.stream(AggregatedGoodsCategory.values())
            .filter(category -> category.getDisplayName().equalsIgnoreCase(cell20.getStringCellValue())).collect(Collectors.toList());
         if(!categories.isEmpty() ) {
            item.setAggregatedGoodsCategory(AggregatedGoodsCategory.valueOf(cell20.getStringCellValue()));
        }
        else {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Aggregated Goods Category: Not valid", rowIndex, 20));
        }
    }



    // Validate DefaultAverageEF
    Cell cell21 =row.getCell(21);
    errorString = FileValidationUtil.validateString(cell21, 255, Boolean.FALSE);
    if (errorString.isPresent()) {
        errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("DefaultAverageEF: " + errorString.get(), rowIndex, 21));
    } else if(Objects.nonNull(cell21)){
        DefaultAverageEF defaultAverageEF = defaultAverageEFRepository.findByDomain(cell21.getStringCellValue());
        if (!Objects.isNull(defaultAverageEF)) {
            item.setDefaultAverageEF(defaultAverageEF);
        }
    }
    return item;
}

    private Company getCurrentUserCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyRepository.findByName(userMetaData.get("companyName"));
    }
    /**
     * {@inheritDoc}
     */
    public void deleteItemById(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        itemRepository.deleteById(id);
    }

    /**
     * Validates the given ItemDTO to ensure all required fields are present and valid.
     *
     * @param itemDTO - Item object to validate.
     */
    private void validateItemDTO(ItemDTO itemDTO) {
        if (StringUtils.isBlank(itemDTO.getItemName()))
            throw new BadRequestAlertException("Item Name should not be Empty", ENTITY_NAME, "nameEmpty");

        if (StringUtils.isBlank(itemDTO.getDescription()))
            throw new BadRequestAlertException("Item Description should not be Empty", ENTITY_NAME, "descriptionEmpty");

        if (itemDTO.getItemType() == null || StringUtils.isBlank(itemDTO.getItemType().toString()))
            throw new BadRequestAlertException("Item Type should not be Empty", ENTITY_NAME, "typeEmpty");

        if (itemDTO.getPurchasedItem() == null)
            throw new BadRequestAlertException("Purchased Item should not be Empty", ENTITY_NAME, "purchasedItemEmpty");

    }

    /**
     * {@inheritDoc}
     */
    public Page<ItemDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(itemMapper::itemToItemDTO);
    }

    /**
     * {@inheritDoc}
     */
    public Page<ItemDTO> getItemCategoryFilter(String filterValue, Pageable pageable){
        return itemRepository.findAllByItemCategory(filterValue, pageable).map(itemMapper::itemToItemDTO);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getItemCategory(){
        return itemRepository.fetchDistinctItemCategory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ItemDTO> getItems(int pageNo, int pageSize, String sortBy, String sortDirection, String itemName, List<String> itemCategories) {
        Page<Item> pageItem;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(itemName) && CollectionUtils.isEmpty(itemCategories)) {
            pageItem = itemRepository.findAll(pageable);
        } else if (StringUtils.isNotEmpty(itemName) && CollectionUtils.isEmpty(itemCategories)){
            pageItem = itemRepository.findByItemNameLikeIgnoreCase(itemName, pageable);
        } else if (StringUtils.isEmpty(itemName) && CollectionUtils.isNotEmpty(itemCategories)){
            pageItem = itemRepository.findByItemCategoryIn(itemCategories, pageable);
        } else {
            pageItem = itemRepository.findByItemNameContainingIgnoreCaseAndItemCategoryIn(itemName, itemCategories, pageable);
        }

        return pageItem.map(itemMapper::itemToItemDTO);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllItemName() {
        return itemRepository.getAllItemName();
    }

    @Override
    public ItemDTO findItemById(Long id) {
        return itemMapper.itemToItemDTO(itemRepository.findById(id).orElse(null));
    }

    @Override
    public ItemDTO getItemByName(String itemName) {
        return itemMapper.itemToItemDTO(itemRepository.findByItemNameIgnoreCase(itemName));
    }
}
