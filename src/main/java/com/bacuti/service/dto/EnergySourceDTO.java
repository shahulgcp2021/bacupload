package com.bacuti.service.dto;

import com.bacuti.domain.enumeration.EfUnits;
import com.bacuti.domain.enumeration.EnergyType;

import java.math.BigDecimal;

/**
 * DTO for the Energy Source Entity
 */
public class EnergySourceDTO {

    private Long id;

    private EnergyType energyType;

    private String description;

    private String supplier;

    private BigDecimal co2EmissionFactor;

    private BigDecimal upstreamCo2EF;

    private EfUnits efUnits;

    private String sourceForEf;

    private BigDecimal percentRenewableSrc;

    private CompanyDTO company;

    private SiteDTO site;

    private CompanyPublicEmissionDTO companyPublicEmission;

    private DefaultAverageEFDTO defaultAverageEF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getCo2EmissionFactor() {
        return co2EmissionFactor;
    }

    public void setCo2EmissionFactor(BigDecimal co2EmissionFactor) {
        this.co2EmissionFactor = co2EmissionFactor;
    }

    public BigDecimal getUpstreamCo2EF() {
        return upstreamCo2EF;
    }

    public void setUpstreamCo2EF(BigDecimal upstreamCo2EF) {
        this.upstreamCo2EF = upstreamCo2EF;
    }

    public EfUnits getEfUnits() {
        return efUnits;
    }

    public void setEfUnits(EfUnits efUnits) {
        this.efUnits = efUnits;
    }

    public String getSourceForEf() {
        return sourceForEf;
    }

    public void setSourceForEf(String sourceForEf) {
        this.sourceForEf = sourceForEf;
    }

    public BigDecimal getPercentRenewableSrc() {
        return percentRenewableSrc;
    }

    public void setPercentRenewableSrc(BigDecimal percentRenewableSrc) {
        this.percentRenewableSrc = percentRenewableSrc;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public CompanyPublicEmissionDTO getCompanyPublicEmission() {
        return companyPublicEmission;
    }

    public void setCompanyPublicEmission(CompanyPublicEmissionDTO companyPublicEmission) {
        this.companyPublicEmission = companyPublicEmission;
    }

    public DefaultAverageEFDTO getDefaultAverageEF() {
        return defaultAverageEF;
    }

    public void setDefaultAverageEF(DefaultAverageEFDTO defaultAverageEF) {
        this.defaultAverageEF = defaultAverageEF;
    }
}
