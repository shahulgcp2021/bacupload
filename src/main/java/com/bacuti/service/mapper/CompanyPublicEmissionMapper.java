package com.bacuti.service.mapper;

import com.bacuti.domain.CompanyPublicEmission;
import com.bacuti.security.SecurityUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyPublicEmissionMapper {

    default void updateAuditColumns(CompanyPublicEmission companyPublicEmission) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(companyPublicEmission.getId())) {
            // New entity
            companyPublicEmission.setCreatedBy(username);
            companyPublicEmission.setCreatedDate(now);
        }
        // Update last modified info
        companyPublicEmission.setLastModifiedBy(username);
        companyPublicEmission.setLastModifiedDate(now);
    }

}
