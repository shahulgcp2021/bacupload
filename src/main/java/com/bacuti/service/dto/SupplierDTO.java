package com.bacuti.service.dto;

import java.time.Instant;

/**
 * Data Transfer Object for Supplier
 */
public class SupplierDTO {

    private Long id;
    private String supplierName;
    private String description;
    private String category;
    private String website;
    private String country;
    private String sustainabilityContactName;
    private String sustainabilityContactEmail;
    private String createdBy;
    private Instant createdDate = Instant.now();
    private String lastModifiedBy;
    private Instant lastModifiedDate = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSustainabilityContactName() {
        return sustainabilityContactName;
    }

    public void setSustainabilityContactName(String sustainabilityContactName) {
        this.sustainabilityContactName = sustainabilityContactName;
    }

    public String getSustainabilityContactEmail() {
        return sustainabilityContactEmail;
    }

    public void setSustainabilityContactEmail(String sustainabilityContactEmail) {
        this.sustainabilityContactEmail = sustainabilityContactEmail;
    }

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
}
