package com.bacuti.domain;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;


import java.io.Serializable;

@Entity
@Table(name = "file_upload_status")
public class FileUploadStatus extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "message", length = 500)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileUploadStatus that = (FileUploadStatus) o;

        return new EqualsBuilder().append(id, that.id).append(fileName, that.fileName).append(status, that.status).append(message, that.message).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(fileName).append(status).append(message).toHashCode();
    }

    @Override
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
    public String toString() {
        return "FileUploadStatus{" +
            "id=" + id +
            ", fileName='" + fileName + '\'' +
            ", status='" + status + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}

