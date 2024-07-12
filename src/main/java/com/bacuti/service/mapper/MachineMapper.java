package com.bacuti.service.mapper;

import com.bacuti.domain.Machine;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.MachineDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MachineMapper {

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Machine machineDTOToMachine(MachineDTO machineDTO);

    MachineDTO machineToMachineDTO(Machine machine);

    default void updateAuditColumns(Machine machine) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (machine.getId() == null) {
            // New entity
            machine.setCreatedBy(username);
            machine.setCreatedDate(now);
        }
        // Update last modified info
        machine.setLastModifiedBy(username);
        machine.setLastModifiedDate(now);
    }
}
