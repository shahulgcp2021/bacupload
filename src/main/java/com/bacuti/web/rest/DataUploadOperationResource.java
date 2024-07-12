package com.bacuti.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacuti.domain.DataPoint;
import com.bacuti.service.DataUploadOperationService;
import com.bacuti.service.dto.datauploadoperation.DataUploadOperationDto;

/**
 * Controller to handle data upload operation related CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/data-upload-operation")
@Transactional
public class DataUploadOperationResource {

    private final Logger log = LoggerFactory.getLogger(DataUploadOperationResource.class);

    @Autowired
    private DataUploadOperationService dataUploadOperationService;

    /**
     * Gets all the data upload operation
     *
     * @return list of dataUploadOperation.
     */
    @GetMapping("")
    public List<DataUploadOperationDto> getAllDataPointOperation() {
        log.debug("REST request to get all Data Upload Operation");
        return dataUploadOperationService.findAll();
    }

    /**
     * Update the isSelected field in datapoint.
     *
     * @param id id of the datapoint to be updated.
     * @param isSelected boolean to be updated.
     * @return updated datapoint.
     */
    @PatchMapping("/data-point")
    public DataPoint update(@RequestParam("id") Long id, @RequestParam("isSelected") Boolean isSelected) {
        log.debug("REST request to update Data Upload OperationDto");
        return dataUploadOperationService.updateDataPoint(id, isSelected);
    }

    /**
     * Update the isSelected field of the selected data upload operation.
     *
     * @param operationName name of the data upload operation to be updated.
     * @param isSelected boolean to be updated.
     */
    @PatchMapping("")
    public void update(@RequestParam("operationName") String operationName, @RequestParam("isSelected") Boolean isSelected) {
        log.debug("REST request to update Data Upload OperationDto");
        dataUploadOperationService.update(operationName, isSelected);
    }
}
