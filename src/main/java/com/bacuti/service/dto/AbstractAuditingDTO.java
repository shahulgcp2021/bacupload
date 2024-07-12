package com.bacuti.service.dto;



import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.Instant;

public class AbstractAuditingDTO {

    private String createdBy;
    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractAuditingDTO that = (AbstractAuditingDTO) o;

        return new EqualsBuilder().append(createdBy, that.createdBy).append(createdDate, that.createdDate).append(lastModifiedBy, that.lastModifiedBy).append(lastModifiedDate, that.lastModifiedDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(createdBy).append(createdDate).append(lastModifiedBy).append(lastModifiedDate).toHashCode();
    }

    @Override
    public String toString() {
        return "AbstractAuditingDTO{" +
            "createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            '}';
    }
}
