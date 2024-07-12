package com.bacuti.service.dto.datauploadoperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for DataPoint.
 */
public class DataPointDto {

    private Boolean isSelected;
    private Long id;
    private String name;
    private String description;
    private List<DataPointItemDto> dataPointItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<DataPointItemDto> getDataPointItems() {
        return dataPointItems;
    }

    public void setDataPointItems(List<DataPointItemDto> dataPointItems) {
        this.dataPointItems = dataPointItems;
    }
}
