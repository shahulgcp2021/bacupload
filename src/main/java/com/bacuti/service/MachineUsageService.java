package com.bacuti.service;

import com.bacuti.service.dto.MachineUsageDTO;
import org.springframework.data.domain.Page;

public interface MachineUsageService {

    MachineUsageDTO saveMachineUsage(MachineUsageDTO machineUsageDTO);
    void deleteMachineUsageById(Long id);
    Page<MachineUsageDTO> getAllMachineUsage(int pageNo, int pageSize, String sortBy, String sortDirection);
}
