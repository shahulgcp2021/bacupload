package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Company;
import com.bacuti.domain.Site;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.SiteRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.SiteService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.SiteDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.SiteMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author shahul.Buhari
 * @version v1
 */
@Service("SiteService")
public class SiteServiceImpl implements SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    SiteMapper siteMapper;

    @Autowired
    private CompanyService companyService;

    @Override
    @Transactional
    public SiteDTO saveSite(SiteDTO siteDTO) {
        if (siteDTO.getId() != null) {
            // Check if the site exists when id is provided
            siteRepository.findById(siteDTO.getId())
                .orElseThrow(() -> new BusinessException("Site Not found", HttpStatus.BAD_REQUEST.value()));
        }
        if (siteRepository.existsBySiteName(siteDTO.getSiteName()) && ((siteDTO.getId() == null) || !Objects.equals(siteRepository.getIdBySiteName(siteDTO.getSiteName()), siteDTO.getId()))) {
            // Check if site name already exists when creating and updating a site
            throw new BusinessException("Site with the name '" + siteDTO.getSiteName() + "' already exists.", HttpStatus.BAD_REQUEST.value());
        }

        // Convert SiteDTO to Site entity
        Site site = siteMapper.siteDTOToSite(siteDTO);
        //Set login company details
        site.setCompany(getLoggedInCompany());
        // Update audit columns
        siteMapper.updateAuditColumns(site);
        // Save the Site entity
        Site savedSite = siteRepository.save(site);

        // Convert the saved Site entity back to SiteDTO
        return siteMapper.siteToSiteDTO(savedSite);
    }

    @Override
    @Transactional
    public Page<SiteDTO> getSites(int pageNo, int pageSize, String sortBy, String sortDirection, String searchString) {
        Page<Site> pageSite;
        if (StringUtils.isEmpty(searchString)) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            pageSite = siteRepository.findAll(pageable);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            pageSite = siteRepository.findBySiteNameLikeIgnoreCase(searchString, pageable);
        }

        return pageSite.map(siteMapper::siteToSiteDTO);
    }

    @Override
    @Transactional
    public SiteDTO findById(Long id) {
        Site dbSite = siteRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value()));
        return siteMapper.siteToSiteDTO(dbSite);
    }


    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        boolean exists = siteRepository.existsById(id);
        if(!exists) {
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        siteRepository.deleteById(id);
        return true;
    }

    /**
     * Method used to process the input sheet and save the entity
     *
     * @param sheet - Input sheet from upload
     * @return - Returns the error detail dto
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<Site> siteList = new ArrayList<>();
        log.debug("REST request to validate and Save Site");
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();

        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.SITE_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            Site site = validateRow(row, errorDTOs, rowIndex);

            if (errorDTOs.isEmpty()) {
                Site savedSite = siteRepository.findBySiteNameIgnoreCase(site.getSiteName());

                if (Objects.nonNull(savedSite))
                    site.setId(savedSite.getId());

                site.setCompany(getLoggedInCompany());
                siteMapper.updateAuditColumns(site);
                siteList.add(site);
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        log.debug("Site details in sheet");
        saveEntitiesInBatches(siteList);
        return errorDetailDTOS;
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
     * Method used to validate the input row by cell wise.
     *
     * @param row - Row from the sheet
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @return - Returns the customer entity
     */
    private Site validateRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        log.debug("REST request to validate row " + rowIndex);
        Site site = new Site();

        validateAndSetString(row, errorDetailDTOS, rowIndex, 0, site::setSiteName, "Site Name", Boolean.TRUE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 1, site::setDescription, "Description", Boolean.TRUE);
        validateAndSetBoolean(row, errorDetailDTOS, rowIndex, 2, site::setManufacturingSite, "Manufacturing Site", Boolean.TRUE);
        validateAndSetInteger(row, errorDetailDTOS, rowIndex, 3, site::setEmployeeCount, "Employee Count", Boolean.TRUE);
        validateAndSetBoolean(row, errorDetailDTOS, rowIndex, 4, site::setCbamImpacted, "CBAM Impacted", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 5, site::setCountry, "Country", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 6, site::setState, "State", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 7, site::setAddress, "Address", Boolean.FALSE);
        validateAndSetDouble(row, 8, site::setLattitude, "Latitude ", errorDetailDTOS);
        validateAndSetDouble(row, 9, site::setLongitude, "Longitude", errorDetailDTOS);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 10, site::setUnlocode, "Unlocode", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 11, site::setDataQualityDesc, "Data Quality Desc", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 12, site::setDefaultValueUsageJustfn, "Default Value Usage", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 13, site::setDataQAInfo, "Data QA Info", Boolean.FALSE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 14, site::setDefaultHeatNumber, "Default Heat Number", Boolean.FALSE);

        return site;
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
        log.debug("REST request to validate row as string " + rowIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateString(cell, 255, mandate);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + error.get(), rowIndex, cellIndex));
        } else {
            setter.accept(Objects.nonNull(cell) ? cell.getStringCellValue() : null);
        }
    }

    /**
     * Method to validate cell value and set it to a Boolean field or add error to list
     *
     * @param row - Row from the file
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @param cellIndex - Cell index of each row
     * @param setter - Setter key to set the value
     * @param fieldName - Field name to check
     * @param mandate - Mandatory check for field
     */
    private void validateAndSetBoolean(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, int cellIndex,
                                      Consumer<Boolean> setter, String fieldName, Boolean mandate) {
        log.debug("REST request to validate row as boolean" + rowIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateBoolean(cell, mandate);

        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + error.get(), rowIndex, 2));
        } else {
            Boolean valueToSet = null;
            if (Objects.nonNull(cell)) {
                valueToSet = FileValidationUtil.getBooleanValue(cell);
            }
            setter.accept(valueToSet);
        }
    }

    /**
     * Method to validate cell value and set it to a Integer field or add error to list
     *
     * @param row - Row from the file
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from the sheet
     * @param cellIndex - Cell index of each row
     * @param setter - Setter key to set the value
     * @param fieldName - Field name to check
     * @param mandate - Mandatory check for field
     */
    private void validateAndSetInteger(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, int cellIndex,
                                       Consumer<Integer> setter, String fieldName, Boolean mandate) {
        log.debug("REST request to validate row as int" + rowIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> quantityError = FileValidationUtil.validateInteger(cell, 0, 1000000000, mandate);
        if (quantityError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(fieldName + ": " + quantityError.get(), rowIndex, 3));
        } else {
            setter.accept(Objects.nonNull(cell) ? (int) cell.getNumericCellValue() : null);
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
     * Method used to save the entries by batch wise
     *
     * @param siteList - List of site entity
     */
    public void saveEntitiesInBatches(List<Site> siteList) {
        for (int i = 0; i < siteList.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, siteList.size());
            List<Site> batchList = siteList.subList(i, endIndex);
            siteRepository.saveAll(batchList);
            siteRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> fetchAllSiteNames(){
        return siteRepository.fetchAllSiteNames();
    }

    @Override
    public SiteDTO getSiteBySiteName(String siteName) {
        return siteMapper.siteToSiteDTO(siteRepository.findBySiteNameIgnoreCase(siteName));
    }
}
