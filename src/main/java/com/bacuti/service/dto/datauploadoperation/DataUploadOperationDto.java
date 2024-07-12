package com.bacuti.service.dto.datauploadoperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for Data Upload Operation.
 */
public class DataUploadOperationDto {

    private Boolean isSelected;
    private Long id;
    private String operation;
    private List<DataPointDto> dataPointDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<DataPointDto> getDataPointDtos() {
        return dataPointDtos;
    }

    public void setDataPointDtos(List<DataPointDto> dataPointDtos) {
        this.dataPointDtos = dataPointDtos;
    }
}
