package com.bacuti.service.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for ItemSupplier
 */
public class ItemSupplierDTO {

    private Long id;

    private String supplierOwnItem;

    private BigDecimal supplierMix;

    private BigDecimal supplierEmissionMultiplier;

    private BigDecimal intensityUnits;

    private BigDecimal intensityScalingFactor;

    private CompanyDTO company;

    private ItemDTO item;

    private SupplierDTO supplier;

    private CompanyPublicEmissionDTO companyPublicEmission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierOwnItem() {
        return supplierOwnItem;
    }

    public void setSupplierOwnItem(String supplierOwnItem) {
        this.supplierOwnItem = supplierOwnItem;
    }

    public BigDecimal getSupplierMix() {
        return supplierMix;
    }

    public void setSupplierMix(BigDecimal supplierMix) {
        this.supplierMix = supplierMix;
    }

    public BigDecimal getSupplierEmissionMultiplier() {
        return supplierEmissionMultiplier;
    }

    public void setSupplierEmissionMultiplier(BigDecimal supplierEmissionMultiplier) {
        this.supplierEmissionMultiplier = supplierEmissionMultiplier;
    }

    public BigDecimal getIntensityUnits() {
        return intensityUnits;
    }

    public void setIntensityUnits(BigDecimal intensityUnits) {
        this.intensityUnits = intensityUnits;
    }

    public BigDecimal getIntensityScalingFactor() {
        return intensityScalingFactor;
    }

    public void setIntensityScalingFactor(BigDecimal intensityScalingFactor) {
        this.intensityScalingFactor = intensityScalingFactor;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    public CompanyPublicEmissionDTO getCompanyPublicEmission() {
        return companyPublicEmission;
    }

    public void setCompanyPublicEmission(CompanyPublicEmissionDTO companyPublicEmission) {
        this.companyPublicEmission = companyPublicEmission;
    }
}
