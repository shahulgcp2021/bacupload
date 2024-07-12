package com.bacuti.service.mapper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.bacuti.domain.Supplier;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.SupplierDTO;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper class for Supplier.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {

    /**
     * Converts Supplier to SupplierDTO.
     *
     * @param supplier to be converted.
     */
    SupplierDTO toDto(Supplier supplier);

    /**
     * converts SupplierDTO to Supplier.
     *
     * @param supplierDTO to be converted.
     * @return converted SupplierDTO.
     */
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Supplier toEntity(SupplierDTO supplierDTO);

    /**
     * Converts list of Supplier to list of SupplierDTO.
     *
     * @param suppliers to be converted.
     */
    List<SupplierDTO> mapAllToDto(List<Supplier> suppliers);

    /**
     * Updates the audit fields.
     *
     * @param supplier to update the audit fields.
     */
    default void updateAuditColumns(Supplier supplier) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();
        if (Objects.isNull(supplier.getId())) {
            supplier.setCreatedBy(username);
            supplier.setCreatedDate(now);
        }
        supplier.setLastModifiedBy(username);
        supplier.setLastModifiedDate(now);
    }
}
