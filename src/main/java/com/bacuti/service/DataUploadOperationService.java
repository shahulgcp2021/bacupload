package com.bacuti.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bacuti.domain.DataPoint;
import com.bacuti.service.dto.datauploadoperation.DataUploadOperationDto;

/**
 * Interface to handle data upload operation related CRUD operations.
 */
@Service
public interface DataUploadOperationService {

    /**
     * Gets all the data upload operation
     *
     * @return list of DataUploadOperation.
     */
    List<DataUploadOperationDto> findAll();

    /**
     * Update the isSelected field in DataPoint.
     *
     * @param id id of the DataPoint to be updated.
     * @param isSelected boolean to be updated.
     * @return updated DataPoint.
     */
    DataPoint updateDataPoint(Long id, Boolean isSelected);

    /**
     * Update the isSelected field of the selected data upload operation.
     *
     * @param operationName name of the data upload operation to be updated.
     * @param isSelected boolean to be updated.
     */
    void update(String operationName, Boolean isSelected);
}
