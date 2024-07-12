package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.DateUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.*;
import com.bacuti.domain.enumeration.EnergyType;
import com.bacuti.repository.*;
import com.bacuti.service.AggregateEnergyUsageService;
import com.bacuti.service.dto.AggregateEnergyUsageDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.mapper.AggregateEnergyUsageMapper;
import com.bacuti.service.mapper.EnergySourceMapper;
import com.bacuti.service.mapper.SiteMapper;
import com.bacuti.service.mapper.UnitofMeasureMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AggregateEnergyUsageServiceImpl implements AggregateEnergyUsageService {

    private final Logger log = LoggerFactory.getLogger(com.bacuti.service.AggregateEnergyUsageService.class);
    private static final int MAX_DIGITS = 15;

    private final AggregateEnergyUsageRepository aggregateEnergyUsageRepository;
    private final SiteRepository siteRepository;
    private final EnergySourceRepository energySourceRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final AggregateEnergyUsageMapper aggregateEnergyUsageMapper;
    private final CompanyRepository companyRepository;

    public AggregateEnergyUsageServiceImpl(AggregateEnergyUsageRepository aggregateEnergyUsageRepository,
                                           SiteRepository siteRepository, EnergySourceRepository energySourceRepository, UnitOfMeasureRepository unitOfMeasureRepository, AggregateEnergyUsageMapper aggregateEnergyUsageMapper, CompanyRepository companyRepository) {
        this.aggregateEnergyUsageRepository = aggregateEnergyUsageRepository;
        this.siteRepository = siteRepository;
        this.energySourceRepository = energySourceRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.aggregateEnergyUsageMapper = aggregateEnergyUsageMapper;
        this.companyRepository = companyRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AggregateEnergyUsageDTO saveAggregateEnergyUsage(AggregateEnergyUsageDTO aggregateEnergyUsageDTO) {
        if (Objects.isNull(aggregateEnergyUsageDTO.getEnergySource()) || Objects.isNull(aggregateEnergyUsageDTO.getEnergySource().getId())) {
            throw new BusinessException("Invalid Energy Source/Energy Source not provided", HttpStatus.PRECONDITION_FAILED.value());
        }
        if (Objects.isNull(aggregateEnergyUsageDTO.getUnitOfMeasure()) || Objects.isNull(aggregateEnergyUsageDTO.getUnitOfMeasure().getId())) {
            throw new BusinessException("Invalid UnitOfMeasure/UnitOfMeasure not provided", HttpStatus.PRECONDITION_FAILED.value());
        }
        validateUsage(aggregateEnergyUsageDTO);

        EnergySource energySource = energySourceRepository.findById(aggregateEnergyUsageDTO.getEnergySource().getId()).get();
        if (Objects.isNull(energySource.getSite())) {
            throw new BusinessException("No Site present for the Energy Source", HttpStatus.NOT_FOUND.value());
        }

        aggregateEnergyUsageDTO.setSite(energySource.getSite());
        Optional<AggregateEnergyUsage> aeuObject = aggregateEnergyUsageRepository
            .findBySiteEnergyTypeUom(energySource.getSite().getId(),
                aggregateEnergyUsageDTO.getEnergySource().getId(),
                aggregateEnergyUsageDTO.getUnitOfMeasure().getId());

        if (aeuObject.isPresent()) {
            if (aggregateEnergyUsageDTO.getId() == null) {
                throw new BusinessException("AggregateEnergyUsage with the same site for the given" +
                    " energyType and UoM already exists.", HttpStatus.PRECONDITION_FAILED.value());
            }
            aggregateEnergyUsageRepository.findById(aggregateEnergyUsageDTO.getId())
                .orElseThrow(() -> new BusinessException("Aggregate Energy Usage Not found", HttpStatus.NOT_FOUND.value()));
        }

        AggregateEnergyUsage aggregateEnergyUsage = AggregateEnergyUsageMapper.INSTANCE.toEntity(aggregateEnergyUsageDTO);
        AggregateEnergyUsageMapper.INSTANCE.updateAuditColumns(aggregateEnergyUsage);
        return AggregateEnergyUsageMapper.INSTANCE.toDTO(aggregateEnergyUsageRepository.save(aggregateEnergyUsage));
    }

    private void validateUsage(AggregateEnergyUsageDTO aggregateEnergyUsageDTO) {
        BigDecimal roundedUsage = aggregateEnergyUsageDTO.getUsage().setScale(0, RoundingMode.DOWN);

        if (roundedUsage.precision() > MAX_DIGITS) {
            throw new BusinessException("Invalid Usage value - Usage value is too big", HttpStatus.PRECONDITION_FAILED.value());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEnergyUsageById(Long id) {
        if(!aggregateEnergyUsageRepository.existsById(id)){
            throw new BusinessException("Invalid Id", HttpStatus.NOT_FOUND.value());
        }
        aggregateEnergyUsageRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AggregateEnergyUsageDTO> getAllEnergyUsage(int pageNo, int pageSize, String sortBy,
                                                           String sortDirection, String searchString) {
        log.debug("Request to get all AggregateEnergyUsages");
        Page<AggregateEnergyUsage> aggregateEnergyUsagePage;
        if (StringUtils.isEmpty(searchString)) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            aggregateEnergyUsagePage = aggregateEnergyUsageRepository.findAll(pageable);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            aggregateEnergyUsagePage = aggregateEnergyUsageRepository
                    .findBySiteNameLikeIgnoreCase(searchString, pageable);
        }

        return aggregateEnergyUsagePage.map(AggregateEnergyUsageMapper.INSTANCE::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();
        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.AGGREGATE_ENERGY_USAGE_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            AggregateEnergyUsage aggregateEnergyUsage = parseRow(row, errorDTOs, rowIndex);
            if (errorDTOs.isEmpty()) {
                try{
                    AggregateEnergyUsageDTO aggregateEnergyUsageDTO = AggregateEnergyUsageMapper.INSTANCE.toDTO(aggregateEnergyUsage);
                    setEnergyResourceAndUoM(aggregateEnergyUsageDTO);
                    saveAggregateEnergyUsage(aggregateEnergyUsageDTO);
                } catch (BusinessException businessException){
                    errorDetailDTOS.add(FileValidationUtil.generateErrorDTO(businessException.getMessage(), rowIndex, 0));
                }
            } else
                errorDetailDTOS.addAll(errorDTOs);
        }
        return errorDetailDTOS;
    }

    private void setEnergyResourceAndUoM(AggregateEnergyUsageDTO aggregateEnergyUsageDTO) {
        Site site = siteRepository.findBySiteNameIgnoreCase(aggregateEnergyUsageDTO.getSite().getSiteName());
        if (Objects.isNull(site)) {
            SiteMapper.INSTANCE.updateAuditColumns(aggregateEnergyUsageDTO.getSite());
            site = siteRepository.save(aggregateEnergyUsageDTO.getSite());
            aggregateEnergyUsageDTO.setSite(site);
        } else {
            aggregateEnergyUsageDTO.setSite(site);
        }
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByName(aggregateEnergyUsageDTO.getUnitOfMeasure().getName());
        if (Objects.isNull(unitOfMeasure)) {
            UnitofMeasureMapper.INSTANCE.updateAuditColumns(aggregateEnergyUsageDTO.getUnitOfMeasure());
            unitOfMeasure = unitOfMeasureRepository.save(aggregateEnergyUsageDTO.getUnitOfMeasure());
            aggregateEnergyUsageDTO.setUnitOfMeasure(unitOfMeasure);
        } else {
            aggregateEnergyUsageDTO.setUnitOfMeasure(unitOfMeasure);
        }
        EnergyType energyType = aggregateEnergyUsageDTO.getEnergySource().getEnergyType();
        Optional<EnergySource> energySourceOptional = energySourceRepository.findByEnergyTypeAndSiteId(energyType, site.getId());
        if (energySourceOptional.isEmpty()) {
            aggregateEnergyUsageDTO.getEnergySource().setSite(site);
            EnergySourceMapper.INSTANCE.updateAuditColumns(aggregateEnergyUsageDTO.getEnergySource());
            EnergySource energySource = energySourceRepository.save(aggregateEnergyUsageDTO.getEnergySource());
            aggregateEnergyUsageDTO.setEnergySource(energySource);
        } else {
            aggregateEnergyUsageDTO.setEnergySource(energySourceOptional.get());
        }

    }

    private AggregateEnergyUsage parseRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        AggregateEnergyUsage aggregateEnergyUsage = new AggregateEnergyUsage();
        log.debug("row: {}", rowIndex);

        //Validate Date
        Cell cell0=row.getCell(0);
        Optional<String> productionDateError = FileValidationUtil.validateDate(cell0, Boolean.TRUE);
        if (productionDateError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Aggregate Energy Usage Date: " + productionDateError.get(), rowIndex, 0));
        else
            aggregateEnergyUsage.setDate(getInstantDate(cell0.getStringCellValue()));

        // Validate Site
        Cell cell1=row.getCell(1);
        Optional<String> siteError = FileValidationUtil.validateString(cell1, 1000, Boolean.FALSE);
        if (siteError.isPresent())
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Site name:" + siteError.get(), rowIndex, 1));
        else if (Objects.nonNull(cell1) && cell1.getCellType() != CellType.BLANK) {
            Site site = new Site();
            site.setSiteName(cell1.getStringCellValue());
            aggregateEnergyUsage.setSite(site);
        }


        // Validate Energy Type
        Cell cell2=row.getCell(2);
        Optional<String> itemNameError = FileValidationUtil.validateString(cell2, 1000, Boolean.FALSE);
        if (itemNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Energy Source Type: " + itemNameError.get(), rowIndex, 2));
        } else if(Objects.nonNull(cell2)) {
            EnergySource energySource = new EnergySource();
            energySource.setEnergyType(EnergyType.valueOf(cell2.getStringCellValue()));
            aggregateEnergyUsage.setEnergySource(energySource);
        }

        // Validate quantity
        Cell cell3=row.getCell(3);
        Optional<String> quantityError = FileValidationUtil.validateInteger(cell3, 1, 1000000000, Boolean.TRUE);
        if (quantityError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("Production Quantity: " + quantityError.get(), rowIndex, 3));
        } else {
            aggregateEnergyUsage.setUsage(BigDecimal.valueOf(cell3.getNumericCellValue()));
        }

        // Validate unit of measure name
        Cell cell4 =row.getCell(4);
        Optional<String> uomNameError = FileValidationUtil.validateString(cell4, 1000, Boolean.FALSE);
        if (uomNameError.isPresent()) {
            errorDetailDTOS.add(FileValidationUtil.generateErrorDTO("UOM: " + uomNameError.get(), rowIndex, 4));
        } else if(Objects.nonNull(cell4)){
            UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure.setName(cell4.getStringCellValue());
            aggregateEnergyUsage.setUnitOfMeasure(unitOfMeasure);
        }
        return aggregateEnergyUsage;
    }

    private Instant getInstantDate(String dateString) {
        LocalDate localDate = DateUtil.convertToLocalDateFromString(dateString);

        // Convert LocalDate to Instant
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
