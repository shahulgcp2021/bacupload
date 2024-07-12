package com.bacuti.service.dto.datauploadoperation;

import com.bacuti.domain.enumeration.DataPointUploadStatus;

/**
 * Data transfer object for DataPointItem.
 */
public class DataPointItemDto {

    private Long id;
    private String item;
    private DataPointUploadStatus uploadStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public DataPointUploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(DataPointUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
