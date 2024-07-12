package com.bacuti.service.mapper;

import com.bacuti.domain.SiteFinishedGood;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.SiteFinishedGoodDTO;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface SiteFinishedGoodMapper {

    SiteFinishedGoodDTO siteFinishedGoodToSiteFinishedGoodDTO(SiteFinishedGood siteFinishedGood);

    SiteFinishedGood siteFinishedGoodDTOToSiteFinishedGood(SiteFinishedGoodDTO siteFinishedGood);

    default void updateAuditColumns(SiteFinishedGood siteFinishedGood) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (siteFinishedGood.getId() == null) {
            // New entity
            siteFinishedGood.setCreatedBy(username);
            siteFinishedGood.setCreatedDate(now);
        }
        // Update last modified info
        siteFinishedGood.setLastModifiedBy(username);
        siteFinishedGood.setLastModifiedDate(now);
    }
}
