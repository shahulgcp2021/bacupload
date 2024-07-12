package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A CapitalGood.
 */
@Entity
@Table(name = "capital_good")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CapitalGood extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_name", unique = true)
    private String assetName;

    @Column(name = "description")
    private String description;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 21, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "useful_life", precision = 21, scale = 2)
    private BigDecimal usefulLife;

    @Column(name = "intensity_units", precision = 21, scale = 2)
    private BigDecimal intensityUnits;

    @Column(name = "scaling_factor", precision = 21, scale = 2)
    private BigDecimal scalingFactor;

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
        value = {
            "company",
            "aggregateEnergyUsages",
            "capitalGoods",
            "carbonPricePayments",
            "employeeTravelAvgs",
            "energySources",
            "itemShipments",
            "machineUsages",
            "productionQties",
            "siteFinishedGoods",
        },
        allowSetters = true
    )
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "unitOfMeasure", "energySources", "items", "wastedisposals" }, allowSetters = true)
    private DefaultAverageEF emissionDb;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CapitalGood id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public CapitalGood assetName(String assetName) {
        this.setAssetName(assetName);
        return this;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getDescription() {
        return this.description;
    }

    public CapitalGood description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public CapitalGood supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public CapitalGood purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return this.purchasePrice;
    }

    public CapitalGood purchasePrice(BigDecimal purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getUsefulLife() {
        return this.usefulLife;
    }

    public CapitalGood usefulLife(BigDecimal usefulLife) {
        this.setUsefulLife(usefulLife);
        return this;
    }

    public void setUsefulLife(BigDecimal usefulLife) {
        this.usefulLife = usefulLife;
    }

    public BigDecimal getIntensityUnits() {
        return this.intensityUnits;
    }

    public CapitalGood intensityUnits(BigDecimal intensityUnits) {
        this.setIntensityUnits(intensityUnits);
        return this;
    }

    public void setIntensityUnits(BigDecimal intensityUnits) {
        this.intensityUnits = intensityUnits;
    }

    public BigDecimal getScalingFactor() {
        return this.scalingFactor;
    }

    public CapitalGood scalingFactor(BigDecimal scalingFactor) {
        this.setScalingFactor(scalingFactor);
        return this;
    }

    public void setScalingFactor(BigDecimal scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CapitalGood company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public CapitalGood site(Site site) {
        this.setSite(site);
        return this;
    }

    public DefaultAverageEF getEmissionDb() {
        return this.emissionDb;
    }

    public void setEmissionDb(DefaultAverageEF defaultAverageEF) {
        this.emissionDb = defaultAverageEF;
    }

    public CapitalGood emissionDb(DefaultAverageEF defaultAverageEF) {
        this.setEmissionDb(defaultAverageEF);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CapitalGood)) {
            return false;
        }
        return getId() != null && getId().equals(((CapitalGood) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CapitalGood{" +
            "id=" + getId() +
            ", assetName='" + getAssetName() + "'" +
            ", description='" + getDescription() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", usefulLife=" + getUsefulLife() +
            ", intensityUnits=" + getIntensityUnits() +
            ", scalingFactor=" + getScalingFactor() +
            "}";
    }
}
