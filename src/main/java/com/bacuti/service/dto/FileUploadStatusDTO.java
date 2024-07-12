package com.bacuti.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.Instant;

public class FileUploadStatusDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String fileName;

    private String status;

    private String message;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileUploadStatusDTO that = (FileUploadStatusDTO) o;

        return new EqualsBuilder().append(id, that.id).append(fileName, that.fileName).append(status, that.status).append(message, that.message).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(fileName).append(status).append(message).toHashCode();
    }

    @Override
    public String toString() {
        return "FileUploadStatusDTO{" +
            "id=" + id +
            ", fileName='" + fileName + '\'' +
            ", status='" + status + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
