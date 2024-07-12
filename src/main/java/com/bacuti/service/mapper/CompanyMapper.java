package com.bacuti.service.mapper;

import com.bacuti.domain.Company;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.CompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    // Convert company dto to company
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Company companyDTOToCompany(CompanyDTO itemSupplierDTO);

    // Convert Company to Company dto
    CompanyDTO companyToCompanyDTO(Company company);

    default void updateAuditColumns(Company company) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(company.getId())) {
            // New entity
            company.setCreatedBy(username);
            company.setCreatedDate(now);
        }
        // Update last modified info
        company.setLastModifiedBy(username);
        company.setLastModifiedDate(now);
    }
}
