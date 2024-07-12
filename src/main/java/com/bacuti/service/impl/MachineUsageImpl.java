package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.domain.*;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.repository.MachineUsageRepository;
import com.bacuti.repository.SiteRepository;
import com.bacuti.repository.UnitOfMeasureRepository;
import com.bacuti.service.MachineUsageService;
import com.bacuti.service.dto.MachineUsageDTO;
import com.bacuti.service.mapper.MachineUsageMapper;
import com.bacuti.service.mapper.SiteMapper;
import com.bacuti.service.mapper.UnitofMeasureMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MachineUsageImpl implements MachineUsageService {

    private final MachineUsageRepository machineUsageRepository;
    private final SiteRepository siteRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CompanyRepository companyRepository;

    public MachineUsageImpl(MachineUsageRepository machineUsageRepository, SiteRepository siteRepository, UnitOfMeasureRepository unitOfMeasureRepository, CompanyRepository companyRepository) {
        this.machineUsageRepository = machineUsageRepository;
        this.siteRepository = siteRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.companyRepository = companyRepository;
    }


    @Override
    public MachineUsageDTO saveMachineUsage(MachineUsageDTO machineUsageDTO) {
        Optional<MachineUsage> muObject = machineUsageRepository.findBySiteMachineUom(
            machineUsageDTO.getSite().getSiteName(), machineUsageDTO.getUnitOfMeasure().getName());

        if (machineUsageDTO.getId() == null && muObject.isPresent()) {
            throw new BusinessException("machine usage with the same site("+machineUsageDTO.getSite().getSiteName()+") " +
                "with uom ("+machineUsageDTO.getUnitOfMeasure().getName()+" already exists.", HttpStatus.BAD_REQUEST.value());
        } else if (machineUsageDTO.getId() != null) {
            // Check if the AEU exists when id is provided
            machineUsageRepository.findById(machineUsageDTO.getId())
                .orElseThrow(() -> new BusinessException("machine usage Not found", HttpStatus.BAD_REQUEST.value()));

            if (muObject.isPresent() && !machineUsageDTO.getId().equals(muObject.get().getId()))
                throw new BusinessException("machine usage with the same site("+machineUsageDTO.getSite().getSiteName()+") " +
                    "with uom ("+machineUsageDTO.getUnitOfMeasure().getName()+" already exists.", HttpStatus.BAD_REQUEST.value());
        }

        MachineUsage machineUsage = MachineUsageMapper.INSTANCE.toEntity(machineUsageDTO);

        /** LinkedTreeMap<String, String> linkedTreeMap = CommonUtil.getLoggedInUserMeta();
        String companyName = linkedTreeMap.get("companyName");
        Optional<Company> company = Optional.ofNullable(companyRepository.findByCompanyName(companyName)
            .orElseThrow(() -> new BusinessException("Company Not found", HttpStatus.BAD_REQUEST.value()))); */

        if (Objects.nonNull(machineUsage.getSite()) && Objects.nonNull(machineUsage.getSite().getSiteName())) {
            Site site = siteRepository.findBySiteNameIgnoreCase(machineUsage.getSite().getSiteName());
            if(Objects.isNull(site)){
                SiteMapper.INSTANCE.updateAuditColumns(machineUsage.getSite());
//                company.ifPresent(value -> machineUsage.getSite().setCompany(value));
                site = siteRepository.save(machineUsage.getSite());
            }
            machineUsage.setSite(site);
        }

        if (Objects.nonNull(machineUsage.getUnitOfMeasure()) && Objects.nonNull(machineUsage.getUnitOfMeasure().getName())) {
            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByName(machineUsage.getUnitOfMeasure().getName());
            if (Objects.isNull(unitOfMeasure)) {
                UnitofMeasureMapper.INSTANCE.updateAuditColumns(machineUsage.getUnitOfMeasure());
                unitOfMeasure = unitOfMeasureRepository.save(machineUsage.getUnitOfMeasure());
            }
            machineUsage.setUnitOfMeasure(unitOfMeasure);
        }
        MachineUsageMapper.INSTANCE.updateAuditColumns(machineUsage);
        MachineUsage mu = machineUsageRepository.save(machineUsage);
        return MachineUsageMapper.INSTANCE.toDTO(mu);
    }

    @Override
    public void deleteMachineUsageById(Long id) {
        if(!machineUsageRepository.existsById(id)){
            throw new BusinessException("Invalid Id", HttpStatus.BAD_REQUEST.value());
        }
        machineUsageRepository.deleteById(id);
    }

    @Override
    public Page<MachineUsageDTO> getAllMachineUsage(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return machineUsageRepository.findAll(pageable).map(MachineUsageMapper.INSTANCE::toDTO);
    }
}
