package com.bacuti.service.mapper;

import com.bacuti.domain.*;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.PurchasedQtyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchasedQtyMapper {
    PurchasedQtyMapper INSTANCE = Mappers.getMapper(PurchasedQtyMapper.class);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    PurchasedQty purchasedQtyDTOToPurchasedQty(PurchasedQtyDTO purchasedQtyDTO);
    PurchasedQtyDTO purchasedQtyToPurchasedQtyDTO(PurchasedQty purchasedQty);

    default void updateAuditColumns(PurchasedQty purchasedQty) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (purchasedQty.getId() == null) {
            purchasedQty.setCreatedBy(username);
            purchasedQty.setCreatedDate(now);
        }
        purchasedQty.setLastModifiedBy(username);
        purchasedQty.setLastModifiedDate(now);
    }
}
