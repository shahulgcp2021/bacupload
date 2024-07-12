package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BillofMaterial.
 */
@Entity
@Table(name = "billof_material")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillofMaterial extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billof_material_seq")
    @SequenceGenerator(name = "billof_material_seq", sequenceName = "billof_material_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "yield_factor", precision = 21, scale = 2)
    private BigDecimal yieldFactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "aggregateEnergyUsages",
            "billofMaterials",
            "capitalGoods",
            "carbonPricePayments",
            "customers",
            "employeeTravelAvgs",
            "energySources",
            "itemShipments",
            "itemSuppliers",
            "items",
            "machines",
            "machineUsages",
            "productUsageDetails",
            "productionQties",
            "purchasedQties",
            "routings",
            "shipmentLanes",
            "siteFinishedGoods",
            "sites",
            "suppliers",
            "wastedisposals",
        },
        allowSetters = true
    )
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Item product;

    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Item component;

    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @JsonIgnoreProperties(
        value = {
            "aggregateEnergyUsages",
            "billofMaterials",
            "defaultAverageEFS",
            "items",
            "machineUsages",
            "productUsageDetails",
            "purchasedQties",
            "routings",
            "wastedisposals",
        },
        allowSetters = true
    )
    private UnitOfMeasure unitOfMeasure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillofMaterial id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public BillofMaterial quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getYieldFactor() {
        return this.yieldFactor;
    }

    public BillofMaterial yieldFactor(BigDecimal yieldFactor) {
        this.setYieldFactor(yieldFactor);
        return this;
    }

    public void setYieldFactor(BigDecimal yieldFactor) {
        this.yieldFactor = yieldFactor;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BillofMaterial company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getProduct() {
        return this.product;
    }

    public void setProduct(Item item) {
        this.product = item;
    }

    public BillofMaterial product(Item item) {
        this.setProduct(item);
        return this;
    }

    public Item getComponent() {
        return this.component;
    }

    public void setComponent(Item item) {
        this.component = item;
    }

    public BillofMaterial component(Item item) {
        this.setComponent(item);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BillofMaterial unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillofMaterial)) {
            return false;
        }
        return getId() != null && getId().equals(((BillofMaterial) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillofMaterial{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", yieldFactor=" + getYieldFactor() +
            "}";
    }
}
