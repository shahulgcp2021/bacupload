package com.bacuti.service.mapper;

import com.bacuti.domain.DataPoint;
import com.bacuti.service.dto.datauploadoperation.DataPointDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Data upload operation related entities.
 */
@Mapper(componentModel = "spring")
public interface DataUploadOperationMapper {

    /**
     * Converts data point to data point dto.
     *
     * @param dataPoint entity to be converted
     * @return converted dto.
     */
    @Mapping(target = "dataPointItems", source = "dataPointItems")
    DataPointDto dataPointToDataPointDto(DataPoint dataPoint);
}
