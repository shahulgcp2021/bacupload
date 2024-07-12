package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A PurchasedQty.
 */
@Entity
@Table(name = "purchased_qty")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchasedQty extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchased_qty_seq")
    @SequenceGenerator(name = "purchased_qty_seq", sequenceName = "purchased_qty_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "total_emissions", precision = 21, scale = 2)
    private BigDecimal totalEmissions;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "company", "item", "supplier", "companyPublicEmission", "purchasedQties" }, allowSetters = true)
    private ItemSupplier itemSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public PurchasedQty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public PurchasedQty purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public PurchasedQty quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalEmissions() {
        return this.totalEmissions;
    }

    public PurchasedQty totalEmissions(BigDecimal totalEmissions) {
        this.setTotalEmissions(totalEmissions);
        return this;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PurchasedQty company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public PurchasedQty item(Item item) {
        this.setItem(item);
        return this;
    }

    public ItemSupplier getItemSupplier() {
        return this.itemSupplier;
    }

    public void setItemSupplier(ItemSupplier itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public PurchasedQty itemSupplier(ItemSupplier itemSupplier) {
        this.setItemSupplier(itemSupplier);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public PurchasedQty unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasedQty)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchasedQty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "PurchasedQty{" +
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
