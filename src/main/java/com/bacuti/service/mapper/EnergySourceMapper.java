package com.bacuti.service.mapper;

import com.bacuti.domain.EnergySource;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.EnergySourceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergySourceMapper {

    EnergySourceMapper INSTANCE = Mappers.getMapper(EnergySourceMapper.class);

    // Convert Energy source dto to Energy source
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    EnergySource energySourceDTOToEnergySource(EnergySourceDTO energySourceDTO);

    // Convert Energy source to Energy source dto
    EnergySourceDTO energySourceToEnergySourceDTO(EnergySource energySource);

    default void updateAuditColumns(EnergySource energySource) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(energySource.getId())) {
            // New entity
            energySource.setCreatedBy(username);
            energySource.setCreatedDate(now);
        }
        // Update last modified info
        energySource.setLastModifiedBy(username);
        energySource.setLastModifiedDate(now);
    }
}
