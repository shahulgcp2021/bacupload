package com.bacuti.service.mapper;


import com.bacuti.domain.UnitOfMeasure;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.UnitOfMeasureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitofMeasureMapper {

    UnitofMeasureMapper INSTANCE = Mappers.getMapper(UnitofMeasureMapper.class);

    List<UnitOfMeasureDTO> toDto(List<UnitOfMeasure> UnitofMeasures);
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    UnitOfMeasure unitOfMeasureDTOToUnitOfMeasure(UnitOfMeasureDTO unitOfMeasureDTO);

    UnitOfMeasureDTO unitOfMeasureToUnitOfMeasureDTO(UnitOfMeasure unitOfMeasure);


    default void updateAuditColumns(UnitOfMeasure unitOfMeasure) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (unitOfMeasure.getId() == null) {
            // New entity
            unitOfMeasure.setCreatedBy(username);
            unitOfMeasure.setCreatedDate(now);
        }
        // Update last modified info
        unitOfMeasure.setLastModifiedBy(username);
        unitOfMeasure.setLastModifiedDate(now);
    }

}
