package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A ItemSupplier.
 */
@Entity
@Table(name = "item_supplier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemSupplier extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_supplier_seq")
    @SequenceGenerator(name = "item_supplier_seq", sequenceName = "item_supplier_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_own_item")
    private String supplierOwnItem;

    @Column(name = "supplier_mix", precision = 21, scale = 2)
    private BigDecimal supplierMix;

    @Column(name = "supplier_emission_multiplier", precision = 21, scale = 2)
    private BigDecimal supplierEmissionMultiplier;

    @Column(name = "intensity_units", precision = 21, scale = 2)
    private BigDecimal intensityUnits;

    @Column(name = "intensity_scaling_factor", precision = 21, scale = 2)
    private BigDecimal intensityScalingFactor;

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
    @JsonIgnoreProperties(value = { "company", "itemSuppliers" }, allowSetters = true)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "revenueUnitOfMeasure",
            "emissionsUnitOfMeasure",
            "emissionIntensityUnitOfMeasure",
            "activitylevelUnitOfMeasure",
            "energySources",
            "itemSuppliers",
        },
        allowSetters = true
    )
    private CompanyPublicEmission companyPublicEmission;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemSupplier")
    @JsonIgnoreProperties(value = { "company", "item", "itemSupplier", "unitOfMeasure" }, allowSetters = true)
    private Set<PurchasedQty> purchasedQties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemSupplier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierOwnItem() {
        return this.supplierOwnItem;
    }

    public ItemSupplier supplierOwnItem(String supplierOwnItem) {
        this.setSupplierOwnItem(supplierOwnItem);
        return this;
    }

    public void setSupplierOwnItem(String supplierOwnItem) {
        this.supplierOwnItem = supplierOwnItem;
    }

    public BigDecimal getSupplierMix() {
        return this.supplierMix;
    }

    public ItemSupplier supplierMix(BigDecimal supplierMix) {
        this.setSupplierMix(supplierMix);
        return this;
    }

    public void setSupplierMix(BigDecimal supplierMix) {
        this.supplierMix = supplierMix;
    }

    public BigDecimal getSupplierEmissionMultiplier() {
        return this.supplierEmissionMultiplier;
    }

    public ItemSupplier supplierEmissionMultiplier(BigDecimal supplierEmissionMultiplier) {
        this.setSupplierEmissionMultiplier(supplierEmissionMultiplier);
        return this;
    }

    public void setSupplierEmissionMultiplier(BigDecimal supplierEmissionMultiplier) {
        this.supplierEmissionMultiplier = supplierEmissionMultiplier;
    }

    public BigDecimal getIntensityUnits() {
        return this.intensityUnits;
    }

    public ItemSupplier intensityUnits(BigDecimal intensityUnits) {
        this.setIntensityUnits(intensityUnits);
        return this;
    }

    public void setIntensityUnits(BigDecimal intensityUnits) {
        this.intensityUnits = intensityUnits;
    }

    public BigDecimal getIntensityScalingFactor() {
        return this.intensityScalingFactor;
    }

    public ItemSupplier intensityScalingFactor(BigDecimal intensityScalingFactor) {
        this.setIntensityScalingFactor(intensityScalingFactor);
        return this;
    }

    public void setIntensityScalingFactor(BigDecimal intensityScalingFactor) {
        this.intensityScalingFactor = intensityScalingFactor;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ItemSupplier company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemSupplier item(Item item) {
        this.setItem(item);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public ItemSupplier supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public CompanyPublicEmission getCompanyPublicEmission() {
        return this.companyPublicEmission;
    }

    public void setCompanyPublicEmission(CompanyPublicEmission companyPublicEmission) {
        this.companyPublicEmission = companyPublicEmission;
    }

    public ItemSupplier companyPublicEmission(CompanyPublicEmission companyPublicEmission) {
        this.setCompanyPublicEmission(companyPublicEmission);
        return this;
    }

    public Set<PurchasedQty> getPurchasedQties() {
        return this.purchasedQties;
    }

    public void setPurchasedQties(Set<PurchasedQty> purchasedQties) {
        if (this.purchasedQties != null) {
            this.purchasedQties.forEach(i -> i.setItemSupplier(null));
        }
        if (purchasedQties != null) {
            purchasedQties.forEach(i -> i.setItemSupplier(this));
        }
        this.purchasedQties = purchasedQties;
    }

    public ItemSupplier purchasedQties(Set<PurchasedQty> purchasedQties) {
        this.setPurchasedQties(purchasedQties);
        return this;
    }

    public ItemSupplier addPurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.add(purchasedQty);
        purchasedQty.setItemSupplier(this);
        return this;
    }

    public ItemSupplier removePurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.remove(purchasedQty);
        purchasedQty.setItemSupplier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemSupplier)) {
            return false;
        }
        return getId() != null && getId().equals(((ItemSupplier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemSupplier{" +
            "id=" + getId() +
            ", supplierOwnItem='" + getSupplierOwnItem() + "'" +
            ", supplierMix=" + getSupplierMix() +
            ", supplierEmissionMultiplier=" + getSupplierEmissionMultiplier() +
            ", intensityUnits=" + getIntensityUnits() +
            ", intensityScalingFactor=" + getIntensityScalingFactor() +
            "}";
    }
}
