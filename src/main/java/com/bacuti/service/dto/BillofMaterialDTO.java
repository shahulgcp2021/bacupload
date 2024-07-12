package com.bacuti.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * A DTO representing a user, with only the public attributes.
 */

public class BillofMaterialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private BigDecimal quantity;

    private BigDecimal yieldFactor;

    private ItemDTO product;

    private ItemDTO component;

    private UnitOfMeasureDTO unitOfMeasure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getYieldFactor() {
        return yieldFactor;
    }

    public void setYieldFactor(BigDecimal yieldFactor) {
        this.yieldFactor = yieldFactor;
    }

    public ItemDTO getProduct() {
        return product;
    }

    public void setProduct(ItemDTO product) {
        this.product = product;
    }

    public ItemDTO getComponent() {
        return component;
    }

    public void setComponent(ItemDTO component) {
        this.component = component;
    }

    public UnitOfMeasureDTO getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasureDTO unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BillofMaterialDTO that = (BillofMaterialDTO) o;

        return new EqualsBuilder().append(id, that.id).append(quantity, that.quantity).append(yieldFactor, that.yieldFactor).append(product, that.product).append(component, that.component).append(unitOfMeasure, that.unitOfMeasure).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(quantity).append(yieldFactor).append(product).append(component).append(unitOfMeasure).toHashCode();
    }

    @Override
    public String toString() {
        return "BillofMaterialDTO{" +
            "id=" + id +
            ", quantity=" + quantity +
            ", yieldFactor=" + yieldFactor +
            ", product=" + product +
            ", component=" + component +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
