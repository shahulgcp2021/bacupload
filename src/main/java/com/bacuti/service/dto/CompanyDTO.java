package com.bacuti.service.dto;

import com.bacuti.domain.enumeration.BacutiCapsEnabled;
import com.bacuti.domain.enumeration.CompanyStatus;
import com.bacuti.domain.enumeration.IndustryType;

public class CompanyDTO extends BaseDTO {

    private String name;

    private String description;

    private String industry;

    private String domain;

    private String country;

    private IndustryType industryType;

    private String admin;

    private CompanyStatus status;

    private BacutiCapsEnabled bacutiCapsEnabled;

    private String imageURL;

    private String imageContentType;

    private boolean cbamImpacted;

    public boolean isCbamImpacted() {
        return cbamImpacted;
    }

    public void setCbamImpacted(boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BacutiCapsEnabled getBacutiCapsEnabled() {
        return bacutiCapsEnabled;
    }

    public void setBacutiCapsEnabled(BacutiCapsEnabled bacutiCapsEnabled) {
        this.bacutiCapsEnabled = bacutiCapsEnabled;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
