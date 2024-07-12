package com.bacuti.service.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductionQtyDTO implements Serializable {

    private Long id;

    private LocalDate productionnDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Quantity should not be less than 0")
    @DecimalMax(value = "1000000000.0", inclusive = true, message = "Quantity should not be more than 1000000000")
    private BigDecimal quantity;

    private String heatNumber;

    private CompanyDTO company;

    private SiteDTO site;

    private ItemDTO product;

    private UnitOfMeasureDTO unitOfMeasure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionnDate() {
        return productionnDate;
    }

    public void setProductionnDate(LocalDate productionnDate) {
        this.productionnDate = productionnDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getHeatNumber() {
        return heatNumber;
    }

    public void setHeatNumber(String heatNumber) {
        this.heatNumber = heatNumber;
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

    public ItemDTO getProduct() {
        return product;
    }

    public void setProduct(ItemDTO product) {
        this.product = product;
    }

    public UnitOfMeasureDTO getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasureDTO unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return "ProductionQtyDTO{" +
            "id=" + id +
            ", productionnDate=" + productionnDate +
            ", quantity=" + quantity +
            ", heatNumber='" + heatNumber + '\'' +
            ", company=" + company +
            ", site=" + site +
            ", product=" + product +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
