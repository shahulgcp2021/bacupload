package com.bacuti.service.mapper;


import com.bacuti.domain.EmployeeTravelAvg;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.EmployeeTravelAvgDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeTravelAvgMapper {

    EmployeeTravelAvgMapper INSTANCE = Mappers.getMapper(EmployeeTravelAvgMapper.class);

    EmployeeTravelAvgDTO employeeTravelAvgToEmployeeTravelAvgDTO(EmployeeTravelAvg employeeTravelAvg);

    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    EmployeeTravelAvg employeeTravelAvgDTOToEmployeeTravelAvg(EmployeeTravelAvgDTO employeeTravelAvgDTO);

    default void updateAuditColumns(EmployeeTravelAvg employeeTravelAvg) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (employeeTravelAvg.getId() == null) {
            employeeTravelAvg.setCreatedBy(username);
            employeeTravelAvg.setCreatedDate(now);
        }
        employeeTravelAvg.setLastModifiedBy(username);
        employeeTravelAvg.setLastModifiedDate(now);
    }
}
