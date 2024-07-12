package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.CommonUtil;
import com.bacuti.domain.Company;
import com.bacuti.domain.Machine;
import com.bacuti.repository.MachineRepository;
import com.bacuti.service.CompanyService;
import com.bacuti.service.MachineService;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.MachineDTO;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.mapper.MachineMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineMapper machineMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Override
    public MachineDTO createMachine(MachineDTO machineDTO) {
        // Validating the input params
        validateMachine(machineDTO);

        Machine machine = machineMapper.machineDTOToMachine(machineDTO);
        machine.setCompany(companyMapper.companyDTOToCompany(getLoggedInCompany()));
        machineMapper.updateAuditColumns(machine);
        return machineMapper.machineToMachineDTO(machineRepository.save(machine));
    }

    /**
     * Method used to validate the input machine params.
     * @param machineDTO - Input machineDTO value
     */
    private void validateMachine(MachineDTO machineDTO) {
        if (Objects.isNull(machineDTO.getMachineName()) || StringUtils.isBlank(machineDTO.getMachineName()))
            throw new BusinessException("Machine Name should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.isNull(machineDTO.getDescription()) || StringUtils.isBlank(machineDTO.getDescription()))
            throw new BusinessException("Description should not be Empty", HttpStatus.NOT_FOUND.value());

        if (Objects.nonNull(machineDTO.getId()) && !machineRepository.existsById(machineDTO.getId()))
            throw new BusinessException("ID not Found", HttpStatus.NOT_FOUND.value());

        Machine machineData = machineRepository.findByMachineName(machineDTO.getMachineName());

        if (Objects.nonNull(machineData)) {
            if (Objects.isNull(machineDTO.getId()) && (Objects.nonNull(machineData.getMachineName())
                || StringUtils.isBlank(machineData.getMachineName())))
                throw new BusinessException("Machine Name Already Exist", HttpStatus.NOT_FOUND.value());

            if (machineData.getMachineName().equals(machineDTO.getMachineName()) && !Objects.equals(machineData.getId(), machineDTO.getId()))
                throw new BusinessException("Machine Name Already exist", HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMachineById(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new BusinessException("Id Not Found", HttpStatus.NOT_FOUND.value());
        }
        machineRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MachineDTO> getMachineDetails(int pageNo, int pageSize, String sortBy, String sortDirection, String machineName) {
        Page<Machine> pageSite;
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (StringUtils.isEmpty(machineName)) {
            pageSite = machineRepository.findAll(pageable);
        } else {
            pageSite = machineRepository.findByMachineNameContaining(machineName, pageable);
        }
        return pageSite.map(machineMapper::machineToMachineDTO);
    }

    /**
     * Gets the loggedIn company.
     *
     * @return loggedIn company.
     */
    private CompanyDTO getLoggedInCompany() {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyService.findByName(userMetaData.get("companyName"));
    }
}
