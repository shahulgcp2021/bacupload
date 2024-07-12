package com.bacuti.service.mapper;

import com.bacuti.domain.DefaultAverageEF;
import com.bacuti.security.SecurityUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DefaultAverageEFMapper {

    default void updateAuditColumns(DefaultAverageEF defaultAverageEF) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(defaultAverageEF.getId())) {
            // New entity
            defaultAverageEF.setCreatedBy(username);
            defaultAverageEF.setCreatedDate(now);
        }
        // Update last modified info
        defaultAverageEF.setLastModifiedBy(username);
        defaultAverageEF.setLastModifiedDate(now);
    }

}
