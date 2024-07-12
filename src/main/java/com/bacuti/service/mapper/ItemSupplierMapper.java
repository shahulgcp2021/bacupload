package com.bacuti.service.mapper;

import com.bacuti.domain.ItemSupplier;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.ItemSupplierDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemSupplierMapper {

    ItemSupplierMapper INSTANCE = Mappers.getMapper(ItemSupplierMapper.class);

    // Convert Item supplier dto item supplier
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    ItemSupplier itemSupplierDTOToItemSupplier(ItemSupplierDTO itemSupplierDTO);

    // Convert Item supplier to item supplier dto
    ItemSupplierDTO itemSupplierToItemSupplierDTO(ItemSupplier itemSupplier);

    default void updateAuditColumns(ItemSupplier itemSupplier) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(itemSupplier.getId())) {
            // New entity
            itemSupplier.setCreatedBy(username);
            itemSupplier.setCreatedDate(now);
        }
        // Update last modified info
        itemSupplier.setLastModifiedBy(username);
        itemSupplier.setLastModifiedDate(now);
    }
}
