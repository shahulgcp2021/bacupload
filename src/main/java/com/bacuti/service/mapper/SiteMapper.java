package com.bacuti.service.mapper;

import com.bacuti.domain.Site;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.SiteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Site siteDTOToSite(SiteDTO siteDTO);

    SiteDTO siteToSiteDTO(Site site);

    default void updateAuditColumns(Site site) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (site.getId() == null) {
            // New entity
            site.setCreatedBy(username);
            site.setCreatedDate(now);
        }
        // Update last modified info
        site.setLastModifiedBy(username);
        site.setLastModifiedDate(now);
    }
}

