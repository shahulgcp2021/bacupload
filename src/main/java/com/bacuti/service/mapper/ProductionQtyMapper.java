package com.bacuti.service.mapper;

import com.bacuti.domain.ProductionQty;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.ProductionQtyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionQtyMapper {

    ProductionQtyMapper INSTANCE = Mappers.getMapper(ProductionQtyMapper.class);

    ProductionQtyDTO productionQtyToProductionQtyDTO(ProductionQty productionQty);
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    ProductionQty productionQtyDTOToProductionQty(ProductionQtyDTO productionQtyDTO);

    default void updateAuditColumns(ProductionQty productionQty) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (productionQty.getId() == null) {
            productionQty.setCreatedBy(username);
            productionQty.setCreatedDate(now);
        }
        productionQty.setLastModifiedBy(username);
        productionQty.setLastModifiedDate(now);
    }
}
