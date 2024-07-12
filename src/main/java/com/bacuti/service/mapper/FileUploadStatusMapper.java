package com.bacuti.service.mapper;

import com.bacuti.domain.FileUploadStatus;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.FileUploadStatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileUploadStatusMapper {

    FileUploadStatusMapper INSTANCE = Mappers.getMapper(FileUploadStatusMapper.class);

    // Convert Item supplier dto item supplier
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    FileUploadStatus fileUploadStatusDTOToFileUploadStatus(FileUploadStatusDTO fileUploadStatusDTO);

    FileUploadStatusDTO fileUploadStatusToFileUploadStatusDTO(FileUploadStatus fileUploadStatus);

    default void updateAuditColumns(FileUploadStatus fileUploadStatus) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(fileUploadStatus.getId())) {
            // New entity
            fileUploadStatus.setCreatedBy(username);
            fileUploadStatus.setCreatedDate(now);
        }
        // Update last modified info
        fileUploadStatus.setLastModifiedBy(username);
        fileUploadStatus.setLastModifiedDate(now);
    }
}
