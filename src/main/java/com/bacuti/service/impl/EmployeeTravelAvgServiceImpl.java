package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonConstants;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.common.utils.FileValidationUtil;
import com.bacuti.config.Constants;
import com.bacuti.domain.Company;
import com.bacuti.domain.EmployeeTravelAvg;
import com.bacuti.domain.Site;
import com.bacuti.domain.enumeration.TravelType;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.EmployeeTravelAvgRepository;
import com.bacuti.repository.SiteRepository;
import com.bacuti.service.EmployeeTravelAvgService;
import com.bacuti.service.dto.EmployeeTravelAvgDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.mapper.EmployeeTravelAvgMapper;
import com.bacuti.service.mapper.SiteMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeTravelAvgServiceImpl implements EmployeeTravelAvgService {

    private static final String ENTITY_NAME = "EmployeeTravelAvgService";
    private final Logger log = LoggerFactory.getLogger(EmployeeTravelAvgServiceImpl.class);

    @Autowired
    private EmployeeTravelAvgRepository employeeTravelAvgRepository;

    @Autowired
    private EmployeeTravelAvgMapper employeeTravelAvgMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    public EmployeeTravelAvgServiceImpl(EmployeeTravelAvgRepository employeeTravelAvgRepository,
                                        CompanyRepository companyRepository,
                                        SiteRepository siteRepository) {
        this.employeeTravelAvgRepository = employeeTravelAvgRepository;
        this.employeeTravelAvgMapper = EmployeeTravelAvgMapper.INSTANCE;
        this.companyRepository = companyRepository;
        this.siteRepository = siteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeTravelAvgDTO saveEmployeeTravelAvg(EmployeeTravelAvgDTO employeeTravelAvgDTO) {
        EmployeeTravelAvg employeeTravelAvg = employeeTravelAvgMapper.employeeTravelAvgDTOToEmployeeTravelAvg(employeeTravelAvgDTO);
        validateEmployeeTravelAvg(employeeTravelAvg);
        log.debug("EmployeeTravelAvg to save: {}", employeeTravelAvg);
        employeeTravelAvgMapper.updateAuditColumns(employeeTravelAvg);
        EmployeeTravelAvg savedEmployeeTravelAvg = employeeTravelAvgRepository.save(employeeTravelAvg);
        return employeeTravelAvgMapper.employeeTravelAvgToEmployeeTravelAvgDTO(savedEmployeeTravelAvg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmployeeTravelAvgById(Long id) {
        if (!employeeTravelAvgRepository.existsById(id)) {
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        employeeTravelAvgRepository.deleteById(id);
    }

    /**
     * Validates the given EmployeeTravelAvgDTO to ensure all required fields are present and valid.
     *
     * @param employeeTravelAvg - EmployeeTravelAvg object to validate.
     */
    private void validateEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        Company company = getCurrentUserCompany();
        employeeTravelAvg.setCompany(company);
        if (Objects.nonNull(employeeTravelAvg.getSite()) && Objects.nonNull(employeeTravelAvg.getSite().getSiteName())) {
            Site site = siteRepository.findBySiteNameIgnoreCase(employeeTravelAvg.getSite().getSiteName());
            if (Objects.isNull(site)) {
                SiteMapper.INSTANCE.updateAuditColumns(employeeTravelAvg.getSite());
                site = siteRepository.save(employeeTravelAvg.getSite());
            }
            employeeTravelAvg.setSite(site);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EmployeeTravelAvgDTO> getAllEmployeeTravelAvgs(Pageable pageable,String travelType,String siteName) {
        Page<EmployeeTravelAvg> pageEmployeeTravelAvg;
       try {
           if (siteName.isEmpty())
               pageEmployeeTravelAvg = employeeTravelAvgRepository.findByTravelType(pageable, TravelType.valueOf(travelType));
           else
               pageEmployeeTravelAvg = employeeTravelAvgRepository.findByTravelTypeAndSiteContaining(pageable, TravelType.valueOf(travelType), siteName);
           return pageEmployeeTravelAvg.map(employeeTravelAvgMapper::employeeTravelAvgToEmployeeTravelAvgDTO);
       }
       catch (IllegalArgumentException e) {
            return Page.empty();
        }
    }

    private Company getCurrentUserCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyRepository.findByName(Objects.nonNull(userMetaData) ? userMetaData.get("companyName") : "");
    }

    @Override
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet) {
        List<ErrorDetailDTO> errorDetailDTOS = new ArrayList<>();

        for (int rowIndex = 1; Objects.nonNull(sheet.getRow(rowIndex)); rowIndex++) {
            List<ErrorDetailDTO> errorDTOs = new ArrayList<>();
            Row row = sheet.getRow(rowIndex);
            //checking whether the row is empty or not
            boolean emptyRow = FileValidationUtil.isEmptyRow(row, Constants.EMPLOYEE_TRAVEL_AVERAGE_TOTAL_COLUMNS);
            if (emptyRow) {
                break;
            }
            EmployeeTravelAvg employeeTravelAvg = parseRow(row, errorDTOs, rowIndex);

            if (errorDTOs.isEmpty()) {
                try {
                    validateEmployeeTravelAvg(employeeTravelAvg);
                    employeeTravelAvgMapper.updateAuditColumns(employeeTravelAvg);
                    employeeTravelAvgRepository.save(employeeTravelAvg);
                } catch (Exception e) {
                    errorDTOs.add(FileValidationUtil.generateErrorDTO(CommonConstants.EMPLOYEE_TRAVEL_AVG_SAVE_ERROR, rowIndex, 0));
                }
            }

            if (!errorDTOs.isEmpty()) {
                errorDetailDTOS.addAll(errorDTOs);
            }
        }

        return errorDetailDTOS;

    }

    private EmployeeTravelAvg parseRow(Row row, List<ErrorDetailDTO> errorDetailDTOS, int rowIndex) {
        EmployeeTravelAvg employeeTravelAvg = new EmployeeTravelAvg();
        log.debug("row " + rowIndex);

        Cell cell0 = row.getCell(0);

        log.debug("employeeTravelAvg :: " + employeeTravelAvg.toString());
        return employeeTravelAvg;

    }

}
