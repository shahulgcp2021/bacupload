package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.CompanyPublicEmission;
import com.bacuti.domain.DefaultAverageEF;
import com.bacuti.domain.enumeration.EfUnits;
import com.bacuti.domain.enumeration.EnergyType;
import com.bacuti.repository.CompanyPublicEmissionRepository;
import com.bacuti.repository.DefaultAverageEFRepository;
import com.bacuti.repository.EnergySourceRepository;
import com.bacuti.repository.SiteRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.EnergySourceService;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.EnergySourceDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.mapper.CompanyPublicEmissionMapper;
import com.bacuti.service.mapper.DefaultAverageEFMapper;
import com.bacuti.service.mapper.EnergySourceMapper;
import com.bacuti.service.mapper.SiteMapper;
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
import com.bacuti.domain.Site;
import com.bacuti.domain.EnergySource;
import com.bacuti.service.mapper.CompanyMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service("EnergySourceService")
public class EnergySourceServiceImpl implements EnergySourceService {

    private final Logger log = LoggerFactory.getLogger(EnergySourceServiceImpl.class);

    @Autowired
    private EnergySourceMapper energySourceMapper;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private EnergySourceRepository energySourceRepository;

    @Autowired
    private CompanyPublicEmissionRepository companyPublicEmissionRepository;

    @Autowired
    private CompanyPublicEmissionMapper companyPublicEmissionMapper;

    @Autowired
    private DefaultAverageEFRepository defaultAverageEFRepository;

    @Autowired
    private DefaultAverageEFMapper defaultAverageEFMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public EnergySourceDTO createEnergySource(EnergySourceDTO energySourceDTO) {
        log.debug("REST request to save EnergySource");
        // Validating the input params
        validateEnergySource(energySourceDTO);
        EnergySource energySource = energySourceMapper.energySourceDTOToEnergySource(energySourceDTO);
        createParentAndChildEntity(energySource);
        energySource.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
        List<EnergySource> savedEnergySource = energySourceRepository.findBy(energySource.getEnergyType(), energySource.getSite(), energySource.getSupplier());

        if (!savedEnergySource.isEmpty() && (Objects.isNull(energySource.getId()) || !savedEnergySource.get(0).getId().equals(energySource.getId())))
            throw new BusinessException("Site(" +energySource.getSite().getSiteName() +") already has a Energy Type("
                +energySource.getEnergyType() + ") with Supplier(" +energySource.getSupplier() + ")", HttpStatus.NOT_FOUND.value());
        energySourceMapper.updateAuditColumns(energySource);
        EnergySource energySourceObj = energySourceRepository.save(energySource);
        log.debug("Created EnergySource");
        return energySourceMapper.energySourceToEnergySourceDTO(energySourceObj);
    }

    /**
     * Method used to validate the input energy source params.
     * @param energySourceDTO - Input Energy source DTO
     */
    private void validateEnergySource(EnergySourceDTO energySourceDTO) {
        log.debug("Validate Energy Source");
        if (Objects.isNull(energySourceDTO.getSite()) || Objects.isNull(energySourceDTO.getSite().getSiteName())
                || StringUtils.isBlank(energySourceDTO.getSite().getSiteName()))
            throw new BusinessException("Site should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(energySourceDTO.getEnergyType()))
            throw new BusinessException("Energy_Type should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(energySourceDTO.getDescription()) || StringUtils.isBlank(energySourceDTO.getDescription()))
            throw new BusinessException("Description should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(energySourceDTO.getSupplier()) || StringUtils.isBlank(energySourceDTO.getSupplier()))
            throw new BusinessException("Supplier should not be Empty", HttpStatus.NOT_FOUND.value());
    }

    /**
     * Find or create the Site entity.
     * @param siteEntity - Site Entity
     * @return - Site Object
     */
    private Site findOrCreateSite(Site siteEntity) {
        Site site = siteRepository.findBySiteNameIgnoreCase(siteEntity.getSiteName());

        if (Objects.isNull(site)) {
            site = new Site();
            site.setSiteName(siteEntity.getSiteName());
            site.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
            siteMapper.updateAuditColumns(site);
            site = siteRepository.save(site);
        }
        return site;
    }

    /**
     * Find or create the Company Public Emission entity.
     * @param companyPublicEmissionEntity - Company Public Emission Entity
     * @return - Company Public Emission entity.
     */
    private CompanyPublicEmission findOrCreateCompanyPE(CompanyPublicEmission companyPublicEmissionEntity) {
        log.debug("Find company Public Emission by company id: {}", companyPublicEmissionEntity.getId());
        CompanyPublicEmission companyPublicEmission = companyPublicEmissionRepository.findByReportingCompany(companyPublicEmissionEntity.getReportingCompany());

        if (Objects.isNull(companyPublicEmission)) {
            companyPublicEmission = new CompanyPublicEmission();
            companyPublicEmission.setReportingCompany(companyPublicEmissionEntity.getReportingCompany());
            companyPublicEmissionMapper.updateAuditColumns(companyPublicEmission);
            companyPublicEmission = companyPublicEmissionRepository.save(companyPublicEmission);
        }
        return companyPublicEmission;
    }

    /**
     * Find or create the Default Average EF entity.
     * @param defaultAverageEFEntity - Default Average EF Entity
     * @return - Default average emission factor entity
     */
    private DefaultAverageEF findOrCreateDefaultAvgEF(DefaultAverageEF defaultAverageEFEntity) {
        log.debug("Find Default Average EF id: {}", defaultAverageEFEntity.getId());
        DefaultAverageEF defaultAverageEF = defaultAverageEFRepository.findByDomain(defaultAverageEFEntity.getDomain());

        if (Objects.isNull(defaultAverageEF)) {
            defaultAverageEF = new DefaultAverageEF();
            defaultAverageEF.setDomain(defaultAverageEFEntity.getDomain());
            defaultAverageEFMapper.updateAuditColumns(defaultAverageEF);
            defaultAverageEF = defaultAverageEFRepository.save(defaultAverageEF);
        }
        return defaultAverageEF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EnergySourceDTO> getEnergySource(int pageNo, int pageSize, String sortBy, String sortDirection, String energyType) {
        log.debug("REST request to get a page of EnergySource");
        Page<EnergySource> pageSite;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(energyType)) {
            pageSite = energySourceRepository.findAll(pageable);
        } else {
            pageSite = energySourceRepository.findByEnergyTypeContaining(energyType, pageable);
        }
        return pageSite.map(energySourceMapper::energySourceToEnergySourceDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        log.debug("REST request to delete EnergySource : {}", id);
        if (!energySourceRepository.existsById(id)) {
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());
        }
        energySourceRepository.deleteById(id);
    }

    @Override
    public Page<EnergySourceDTO> getEnergySourceTypes() {
        Pageable pageable = Pageable.unpaged();
        Page<EnergySource> pageSite = energySourceRepository.findAll(pageable);
        return pageSite.map(energySourceMapper::energySourceToEnergySourceDTO);
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
     * Method used to process the input sheet and save the entity
     *
     * @param sheet - Input sheet from upload
     * @return - Returns the error detail dto
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<EnergySource> energySourceList = new ArrayList<>();
        log.debug("REST request to validate And Save XSSFSheet");
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();

        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.ENERGY_SOURCE_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            EnergySource energySource = validateRow(row, errorDTOs, rowIndex);

            if (errorDTOs.isEmpty()) {
                createParentAndChildEntity(energySource);
                energySource.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
                List<EnergySource> savedEnergySource = energySourceRepository.findBy(energySource.getEnergyType(),
                    energySource.getSite(), energySource.getSupplier());

                if (!savedEnergySource.isEmpty())
                    energySource.setId(savedEnergySource.get(0).getId());

                energySourceMapper.updateAuditColumns(energySource);
                energySourceList.add(energySource);
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        saveEntitiesInBatches(energySourceList);
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
    private EnergySource validateRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        log.debug("REST request to validate Row : {}", rowIndex);
        EnergySource energySource = new EnergySource();

        validateAndSetEnum(row, 0, EnergyType.class, Boolean.TRUE, errorDetailDTOS, rowIndex, "Energy Type:", energySource::setEnergyType);
        // Validate Site name
        Cell cell1 = row.getCell(1);
        Optional<String> siteError = FileValidationUtil.validateString(cell1, 255, Boolean.TRUE);
        if (siteError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Site name:" + siteError.get(), rowIndex, 1));
        else if(Objects.nonNull(cell1)){
            Site site = new Site();
            site.setSiteName(cell1.getStringCellValue());
            energySource.setSite(site);
        }

        validateAndSetString(row, errorDetailDTOS, rowIndex, 2, energySource::setDescription, "Description", Boolean.TRUE);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 3, energySource::setSupplier, "Supplier", Boolean.TRUE);
        validateAndSetDouble(row, 4, energySource::setCo2EmissionFactor, "Co2 Emission Factor:", errorDetailDTOS);
        validateAndSetDouble(row, 5, energySource::setUpstreamCo2EF, "Upstream Co2 EF:", errorDetailDTOS);
        validateAndSetEnum(row, 6, EfUnits.class, Boolean.FALSE, errorDetailDTOS, rowIndex, "EF Units:", energySource::setEfUnits);
        validateAndSetString(row, errorDetailDTOS, rowIndex, 7, energySource::setSourceForEf, "Source For EF", Boolean.FALSE);
        validateAndSetDouble(row, 8, energySource::setPercentRenewableSrc, "Percent Renewable Src:", errorDetailDTOS);

        // Validate Default Average Emission Factor
        Cell cell9 = row.getCell(9);
        Optional<String> defaultAvgEFError = FileValidationUtil.validateString(cell9, 255, Boolean.FALSE);
        if (defaultAvgEFError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Default Average Emission Factor:" + defaultAvgEFError.get(), rowIndex, 9));
        else if (Objects.nonNull(cell9)) {
            DefaultAverageEF defaultAverageEF = new DefaultAverageEF();
            defaultAverageEF.setDomain(cell9.getStringCellValue());
            energySource.setDefaultAverageEF(defaultAverageEF);
        }

        // Validate Company Public Emission.
        Cell cell10 = row.getCell(10);
        Optional<String> companyPublicEError = FileValidationUtil.validateString(cell10, 255, Boolean.FALSE);
        if (companyPublicEError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Company Public Emission:" + companyPublicEError.get(), rowIndex, 10));
        else if (Objects.nonNull(cell10)) {
            CompanyPublicEmission companyPublicEmission = new CompanyPublicEmission();
            companyPublicEmission.setReportingCompany(cell10.getStringCellValue());
            energySource.setCompanyPublicEmission(companyPublicEmission);
        }
        return energySource;
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
     * Method to validate cell value and set it to a enum field or add error to list
     *
     * @param row - Row from the file
     * @param cellIndex - Cell index of each row
     * @param enumClass - Enum class to get value
     * @param isMandatory - Mandatory check value
     * @param errorDetailDTOS - To add the error details if error exist.
     * @param rowIndex - Row index from file.
     * @param errorPrefix - Prefix column name
     * @param setter - Setter name
     */
    private <E extends Enum<E>> void validateAndSetEnum(Row row, int cellIndex, Class<E> enumClass, boolean isMandatory,
                                                        List<ErrorDetailDTO> errorDetailDTOS, int rowIndex, String errorPrefix,
                                                        Consumer<E> setter) {
        log.debug("REST request to validate Row : {}", cellIndex);
        Cell cell = row.getCell(cellIndex);
        Optional<String> error = FileValidationUtil.validateEnum(cell, enumClass, isMandatory);
        if (error.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(errorPrefix + error.get(), rowIndex, cellIndex));
        } else if (Objects.nonNull(cell)) {
            try {
                E enumValue = Enum.valueOf(enumClass, cell.getStringCellValue().toUpperCase());
                setter.accept(enumValue);
            } catch (IllegalArgumentException e) {
                errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(errorPrefix + "Invalid enum value", rowIndex, cellIndex));
            }
        }
    }

    /**
     * Method used to create the child or parent entity for current entity.
     *
     * @param energySource - Energy source entity
     */
    private void createParentAndChildEntity(EnergySource energySource) {
        log.debug("REST request to create ParentAndChildEntity");
        //Check site exist or create new entity
        if (Objects.nonNull(energySource.getSite()))
            energySource.setSite(findOrCreateSite(energySource.getSite()));

        //Check Company Public Emission exist or create new Entity
        if (Objects.nonNull(energySource.getCompanyPublicEmission()))
            energySource.setCompanyPublicEmission(findOrCreateCompanyPE(energySource.getCompanyPublicEmission()));

        //Check Default AverageEF exist or create new entity
        if (Objects.nonNull(energySource.getDefaultAverageEF()))
            energySource.setDefaultAverageEF(findOrCreateDefaultAvgEF(energySource.getDefaultAverageEF()));
    }

    /**
     * Method used to save the entries by batch wise
     *
     * @param energySources - List of energy sources entity
     */
    public void saveEntitiesInBatches(List<EnergySource> energySources) {
        for (int i = 0; i < energySources.size(); i += CommonUtil.batchSize) {
            int endIndex = Math.min(i + CommonUtil.batchSize, energySources.size());
            List<EnergySource> batchList = energySources.subList(i, endIndex);
            energySourceRepository.saveAll(batchList);
            energySourceRepository.flush(); // Optional: Flush changes to the database after each batch
        }
    }
}
