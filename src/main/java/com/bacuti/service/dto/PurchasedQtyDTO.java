package com.bacuti.service.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchasedQtyDTO  implements Serializable {
    private Long id;
    private LocalDate purchaseDate;
    @DecimalMin(value = "0.0", inclusive = true, message = "Quantity should not be less than 0")
    @DecimalMax(value = "1000000000.0", inclusive = true, message = "Quantity should not be more than 1000000000")
    private BigDecimal quantity;
    private BigDecimal totalEmissions;
    private CompanyDTO company;
    private ItemDTO item;
    private ItemSupplierDTO itemSupplier;
    private UnitOfMeasureDTO unitOfMeasure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
        this.totalEmissions = totalEmissions;
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

    public ItemSupplierDTO getItemSupplier() {
        return itemSupplier;
    }

    public void setItemSupplier(ItemSupplierDTO itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public UnitOfMeasureDTO getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasureDTO unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return "PurchasedQtyDTO{" +
            "id=" + id +
            ", purchaseDate=" + purchaseDate +
            ", quantity=" + quantity +
            ", totalEmissions=" + totalEmissions +
            ", company=" + company +
            ", item=" + item +
            ", itemSupplier=" + itemSupplier +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
