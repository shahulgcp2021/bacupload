package com.bacuti.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Item;
import com.bacuti.domain.Site;
import com.bacuti.domain.SiteFinishedGood;
import com.bacuti.enumeration.SiteFinishedGoodSheet;
import com.bacuti.repository.SiteFinishedGoodRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.ItemService;
import com.bacuti.service.SiteFinishedGoodService;
import com.bacuti.service.SiteService;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.ItemDTO;
import com.bacuti.service.dto.SiteDTO;
import com.bacuti.service.dto.SiteFinishedGoodDTO;
import com.bacuti.service.mapper.SiteFinishedGoodMapper;
import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SiteFinishedGoodServiceImpl implements SiteFinishedGoodService {

    private static final Logger log = LoggerFactory.getLogger(SiteFinishedGoodServiceImpl.class);

    private final SiteFinishedGoodRepository siteFinishedGoodRepository;
    private final SiteService siteService;

    private final ItemService itemService;
    private final SiteFinishedGoodMapper siteFinishedGoodMapper;
    private final CompanyService companyService;

    public SiteFinishedGoodServiceImpl(SiteFinishedGoodRepository siteFinishedGoodRepository, SiteService siteService, ItemService itemService, SiteFinishedGoodMapper siteFinishedGoodMapper, CompanyService companyService) {
        this.siteFinishedGoodRepository = siteFinishedGoodRepository;
        this.siteService = siteService;
        this.itemService = itemService;
        this.siteFinishedGoodMapper = siteFinishedGoodMapper;
        this.companyService = companyService;
    }

    @Override
    public SiteFinishedGoodDTO saveSiteFinishedGood(SiteFinishedGoodDTO siteFinishedGoodDTO) {
        log.debug("Saving SiteFinishedGood: {}", siteFinishedGoodDTO);

        if (isNull(siteFinishedGoodDTO)) {
            log.error("Site Finished Good validation failed: Site Finished Good should not be null or Empty");
            throw new BusinessException("SiteFinishedGood cannot be null", HttpStatus.NOT_FOUND.value());
        }
        validateSiteFinishedGood(siteFinishedGoodDTO);
        if (siteFinishedGoodDTO.getId() != null) {
            log.debug("Checking existence of SiteFinishedGood with id: {}", siteFinishedGoodDTO.getId());
            siteFinishedGoodRepository.findById(siteFinishedGoodDTO.getId())
                .orElseThrow(() -> {
                    log.error("SiteFinishedGood not found with id: {}", siteFinishedGoodDTO.getId());
                    return new BusinessException("SiteFinishedGood Not found", HttpStatus.NOT_FOUND.value());
                });
        }

        // Retrieve the company entity
        CompanyDTO company = getLoggedInCompany(companyService);
        if (isNull(company)) {
            log.error("Invalid company detail for SiteFinishedGood: {}", siteFinishedGoodDTO);
            throw new BusinessException("Invalid company detail", HttpStatus.BAD_REQUEST.value());
        }
        siteFinishedGoodDTO.setCompany(company);

        createChildEntitiesIfRequired(siteFinishedGoodDTO);

        SiteFinishedGood siteFinishedGood = siteFinishedGoodMapper.siteFinishedGoodDTOToSiteFinishedGood(siteFinishedGoodDTO);
        // Update audit columns
        siteFinishedGoodMapper.updateAuditColumns(siteFinishedGood);

        //saving siteFinishedGood entity
        SiteFinishedGood savedSiteFinishedGood = siteFinishedGoodRepository.save(siteFinishedGood);

        log.debug("Saved SiteFinishedGood: {}", savedSiteFinishedGood);
        return siteFinishedGoodMapper.siteFinishedGoodToSiteFinishedGoodDTO(savedSiteFinishedGood);
    }

    private void createChildEntitiesIfRequired(SiteFinishedGoodDTO siteFinishedGoodDTO) {
        SiteDTO siteDTO = null;
        if (nonNull(siteFinishedGoodDTO.getSite().getId())) {
            siteDTO = siteService.findById(siteFinishedGoodDTO.getSite().getId());
        }
        else {
            siteDTO = siteService.getSiteBySiteName(siteFinishedGoodDTO.getSite().getSiteName());
        }
        if (nonNull(siteDTO)) {
            siteFinishedGoodDTO.setSite(siteDTO);
        }
        else {
            siteDTO = new SiteDTO();
            siteDTO.setSiteName(siteFinishedGoodDTO.getSite().getSiteName());
            siteFinishedGoodDTO.setSite(siteService.saveSite(siteDTO));
        }
        ItemDTO itemDTO = null;
        if (nonNull(siteFinishedGoodDTO.getFinishedGood().getId())) {
            itemDTO = itemService.findItemById(siteFinishedGoodDTO.getFinishedGood().getId());
        }
        else {
            itemDTO = itemService.getItemByName(siteFinishedGoodDTO.getFinishedGood().getItemName());
        }
        if (nonNull(itemDTO)) {
            siteFinishedGoodDTO.setFinishedGood(itemDTO);
        }
        else {
            siteFinishedGoodDTO.setFinishedGood(itemService.saveItemForOtherEntities(siteFinishedGoodDTO.getFinishedGood()));
        }
    }

    private void validateSiteFinishedGood(SiteFinishedGoodDTO siteFinishedGoodDTO) {
        log.debug("Validating Site Finished Good: {}", siteFinishedGoodDTO);
        if (isNull(siteFinishedGoodDTO.getFinishedGood()) || (isNull(siteFinishedGoodDTO.getFinishedGood().getId()) && StringUtils.isEmpty(siteFinishedGoodDTO.getFinishedGood().getItemName()))) {
            log.error("Site Finished Good validation failed: Finished Good should not be null and must contain id or item name");
            throw new BusinessException("Finished Good is Mandatory", HttpStatus.NOT_FOUND.value());
        }

        if (isNull(siteFinishedGoodDTO.getSite()) || (isNull(siteFinishedGoodDTO.getSite().getId()) && StringUtils.isEmpty(siteFinishedGoodDTO.getSite().getSiteName()))) {
            log.error("Site Finished Good validation failed: Site should not be null and must contain id or site name");
            throw new BusinessException("Site is Mandatory", HttpStatus.NOT_FOUND.value());
        }

        // checking whether site finished good already mapped with the same site and finished good
        if ((nonNull(siteFinishedGoodDTO.getFinishedGood().getId()) && nonNull(siteFinishedGoodDTO.getSite().getId()) && siteFinishedGoodRepository.checkSiteFinishedGoodExistsBySiteIdAndFinishedGoodId(siteFinishedGoodDTO.getSite().getId(), siteFinishedGoodDTO.getFinishedGood().getId())
            || (siteFinishedGoodRepository.checkSiteFinishedGoodExistsBySiteNameAndFinishedGoodName(siteFinishedGoodDTO.getSite().getSiteName(), siteFinishedGoodDTO.getFinishedGood().getItemName())))) {
            log.error("SiteFinishedGoods already exists for FinishedGoodId: {} and SiteId: {}", siteFinishedGoodDTO.getFinishedGood().getId(), siteFinishedGoodDTO.getSite().getId());
            throw new BusinessException("SiteFinishedGoods already exists", HttpStatus.NOT_FOUND.value());
        }
    }

    @Override
    public SiteFinishedGoodDTO getSiteFinishedGoodById(long id) {
        log.debug("Getting SiteFinishedGood by id: {}", id);
        SiteFinishedGood siteFinishedGood = siteFinishedGoodRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Invalid Id: {}", id);
                return new BusinessException("Invalid Id", HttpStatus.NOT_FOUND.value());
            });
        log.debug("Found SiteFinishedGood: {}", siteFinishedGood);
        return siteFinishedGoodMapper.siteFinishedGoodToSiteFinishedGoodDTO(siteFinishedGood);
    }

    @Override
    public boolean deleteSiteFinishedGoodById(long id) {
        log.debug("Deleting SiteFinishedGood by id: {}", id);
        boolean exists = siteFinishedGoodRepository.existsById(id);
        if (exists) {
            siteFinishedGoodRepository.deleteById(id);
            log.debug("Deleted SiteFinishedGood with id: {}", id);
            return true;
        }
        log.error("Invalid Id: {}", id);
        throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public Page<SiteFinishedGoodDTO> getSiteFinishedGoods(int pageNo, int pageSize, String sortBy, String sortDirection, String searchString) {
        log.debug("Getting SiteFinishedGoods with pageNo: {}, pageSize: {}, sortBy: {}, sortDirection: {}, searchString: {}", pageNo, pageSize, sortBy, sortDirection, searchString);
        Page<SiteFinishedGood> pageSiteFinishedGood;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(searchString)) {
            pageSiteFinishedGood = siteFinishedGoodRepository.findAll(pageable);
        } else {
            pageSiteFinishedGood = siteFinishedGoodRepository.findBySiteNameOrItemNameLikeIgnoreCase(searchString, pageable);
        }

        log.debug("Retrieved SiteFinishedGoods: {}", pageSiteFinishedGood.getContent());
        return pageSiteFinishedGood.map(siteFinishedGoodMapper::siteFinishedGoodToSiteFinishedGoodDTO);
    }

    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        log.debug("Validating and saving SiteFinishedGoods from sheet: {}", sheet);
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        List<SiteFinishedGood> toSaveList = new ArrayList<>();
        for (int rowIndex = 1; nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.SITE_FINISHED_GOOD_TOTAL_COLUMNS);
            if (emptyRow) {
                log.debug("Empty row encountered, stopping processing at row: {}", rowIndex);
                break;
            }
            SiteFinishedGood siteFinishedGood = parseRow(row, errorDTOs, rowIndex);
            if (errorDTOs.size() == 0) {
                toSaveList.add(siteFinishedGood);
            } else {
                errorDetailDTOS.addAll(errorDTOs);
            }
        }
        saveEntitiesInBatches(toSaveList);
        return errorDetailDTOS;
    }

    private SiteFinishedGood parseRow(Row row, List<ErrorDetailDTO> errorDTOs, int rowIndex) {
        log.debug("row " + rowIndex);
        //Validate Site Finished Good
        SiteFinishedGood siteFinishedGood = new SiteFinishedGood();
        for (int columnIndex = 0; columnIndex < Constants.SITE_FINISHED_GOOD_TOTAL_COLUMNS; columnIndex++) {

            //getting the respected column name enum by the index
            SiteFinishedGoodSheet column = SiteFinishedGoodSheet.getByIndex(columnIndex);
            if (isNull(column)) {
                continue;
            }
            Cell cell = row.getCell(columnIndex);

            //getting the max length of a cell by its enum
            Integer maxLength = column.maxLength;
            switch (column) {
                case SiteName -> {
                    Optional<String> siteNameError = FileValidationUtil.validateString(cell, maxLength, Boolean.TRUE);
                    if (siteNameError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + siteNameError.get(), rowIndex, columnIndex));
                    } else if (nonNull(cell)) {
                        Site site = new Site();
                        site.setSiteName(cell.getStringCellValue());
                        siteFinishedGood.setSite(site);
                    }
                }
                case FinishedGood -> {
                    Optional<String> finishedGoodError = FileValidationUtil.validateString(cell, maxLength, Boolean.TRUE);
                    if (finishedGoodError.isPresent()) {
                        errorDTOs.add(FileValidationUtil.generateErrorDTO(column.fieldName + ": " + finishedGoodError.get(), rowIndex, columnIndex));

                    } else if (nonNull(cell)) {
                        Item item = new Item();
                        item.setItemName(cell.getStringCellValue());
                        siteFinishedGood.setFinishedGood(item);
                    }
                }
            }
        }
        return siteFinishedGood;
    }

    public void saveEntitiesInBatches(List<SiteFinishedGood> siteFinishedGoods) {
        log.debug("saveEntitiesInBatches called");
        for (int i = 0; i < siteFinishedGoods.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, siteFinishedGoods.size());
            List<SiteFinishedGood> batchList = siteFinishedGoods.subList(i, endIndex);
            siteFinishedGoodRepository.saveAll(batchList);
            siteFinishedGoodRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
