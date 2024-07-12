package com.bacuti.service.mapper;

import com.bacuti.domain.MachineUsage;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.MachineUsageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MachineUsageMapper {

    MachineUsageMapper INSTANCE = Mappers.getMapper(MachineUsageMapper.class);

    MachineUsageDTO toDTO(MachineUsage machineUsage);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    MachineUsage toEntity(MachineUsageDTO machineUsageDTO);

    default void updateAuditColumns(MachineUsage machineUsage) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(machineUsage.getId())) {
            // New entity
            machineUsage.setCreatedBy(username);
            machineUsage.setCreatedDate(now);
        }
        // Update last modified info
        machineUsage.setLastModifiedBy(username);
        machineUsage.setLastModifiedDate(now);
    }
}
