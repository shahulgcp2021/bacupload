package com.bacuti.service.dto;

import com.bacuti.domain.Site;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class SiteDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String siteName;

    @Size(max = 255)
    private String description;

    private boolean manufacturingSite;

    @Min(0)
    private int employeeCount;

    private boolean cbamImpacted;

    @NotNull
    @Size(min = 1, max = 100)
    private String country;

    @Size(max = 100)
    private String state;

    @Size(max = 255)
    private String address;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitude;

    @Size(max = 10)
    private String unlocode;

    @Size(max = 255)
    private String dataQualityDesc;

    @Size(max = 255)
    private String defaultValueUsageJustfn;

    @Size(max = 255)
    private String dataQAInfo;

    @Size(max = 50)
    private String defaultHeatNumber;

    @NotNull
    private Long companyId;

    @Size(max = 100)
    private String companyName;

    public Long getId() {
        return this.id;
    }

    public SiteDTO id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public SiteDTO siteName(String siteName) {
        this.setSiteName(siteName);
        return this;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturingSite(Boolean manufacturingSite) {
        this.manufacturingSite = manufacturingSite;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getUnlocode() {
        return this.unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getDataQualityDesc() {
        return this.dataQualityDesc;
    }

    public void setDataQualityDesc(String dataQualityDesc) {
        this.dataQualityDesc = dataQualityDesc;
    }

    public String getDefaultValueUsageJustfn() {
        return this.defaultValueUsageJustfn;
    }

    public void setDefaultValueUsageJustfn(String defaultValueUsageJustfn) {
        this.defaultValueUsageJustfn = defaultValueUsageJustfn;
    }

    public String getDataQAInfo() {
        return this.dataQAInfo;
    }

    public void setDataQAInfo(String dataQAInfo) {
        this.dataQAInfo = dataQAInfo;
    }

    public String getDefaultHeatNumber() {
        return this.defaultHeatNumber;
    }

    public void setDefaultHeatNumber(String defaultHeatNumber) {
        this.defaultHeatNumber = defaultHeatNumber;
    }

    public boolean isManufacturingSite() {
        return manufacturingSite;
    }

    public void setManufacturingSite(boolean manufacturingSite) {
        this.manufacturingSite = manufacturingSite;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public boolean isCbamImpacted() {
        return cbamImpacted;
    }

    public void setCbamImpacted(boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return getId() != null && getId().equals(((Site) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
