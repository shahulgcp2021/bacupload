package com.bacuti.service.mapper;

import com.bacuti.domain.AggregateEnergyUsage;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.AggregateEnergyUsageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AggregateEnergyUsageMapper {

    AggregateEnergyUsageMapper INSTANCE = Mappers.getMapper(AggregateEnergyUsageMapper.class);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    AggregateEnergyUsage toEntity(AggregateEnergyUsageDTO aggregateEnergyUsageDTO);

    AggregateEnergyUsageDTO toDTO(AggregateEnergyUsage aggregateEnergyUsage);

    default void updateAuditColumns(AggregateEnergyUsage aggregateEnergyUsage) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(aggregateEnergyUsage.getId())) {
            // New entity
            aggregateEnergyUsage.setCreatedBy(username);
            aggregateEnergyUsage.setCreatedDate(now);
        }
        // Update last modified info
        aggregateEnergyUsage.setLastModifiedBy(username);
        aggregateEnergyUsage.setLastModifiedDate(now);
    }
}
