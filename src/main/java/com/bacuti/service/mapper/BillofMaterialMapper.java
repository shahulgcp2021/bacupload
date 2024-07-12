package com.bacuti.service.mapper;

import com.bacuti.domain.BillofMaterial;
import com.bacuti.domain.Item;
import com.bacuti.domain.Site;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.BillofMaterialDTO;
import com.bacuti.service.dto.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BillofMaterialMapper {

    BillofMaterialMapper INSTANCE = Mappers.getMapper(BillofMaterialMapper.class);

    BillofMaterialDTO billofMaterialToBillofMaterialDTO(BillofMaterial billofMaterial);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    BillofMaterial billofMaterialDTOToBillofMaterial(BillofMaterialDTO billofMaterialDTO);

    BillofMaterialDTO toDto(BillofMaterial billofMaterial);

    ItemDTO getItemDtos(Item items);

    Item getItems(ItemDTO itemDTO);

    default void updateAuditColumns(BillofMaterial billofMaterial) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (billofMaterial.getId() == null) {
            // New entity
            billofMaterial.setCreatedBy(username);
            billofMaterial.setCreatedDate(now);
        }
        // Update last modified info
        billofMaterial.setLastModifiedBy(username);
        billofMaterial.setLastModifiedDate(now);
    }
}
