package com.bacuti.service.mapper;

import com.bacuti.domain.Item;
import com.bacuti.domain.Site;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Item itemDTOToItem(ItemDTO itemDTO);

    ItemDTO itemToItemDTO(Item item);

    default void updateAuditColumns(Item item) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (item.getId() == null) {
            // New entity
            item.setCreatedBy(username);
            item.setCreatedDate(now);
        }
        // Update last modified info
        item.setLastModifiedBy(username);
        item.setLastModifiedDate(now);
    }

}
