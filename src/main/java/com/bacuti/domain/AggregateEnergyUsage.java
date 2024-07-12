package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A AggregateEnergyUsage.
 */
@Entity
@Table(name = "aggregate_energy_usage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AggregateEnergyUsage extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aggregate_energy_usage_seq")
    @SequenceGenerator(name = "aggregate_energy_usage_seq", sequenceName = "aggregate_energy_usage_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "usage", precision = 21, scale = 2)
    private BigDecimal usage;

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
    @JsonIgnoreProperties(
        value = { "company", "site", "companyPublicEmission", "defaultAverageEF", "aggregateEnergyUsages" },
        allowSetters = true
    )
    private EnergySource energySource;

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

    public AggregateEnergyUsage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public AggregateEnergyUsage date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getUsage() {
        return this.usage;
    }

    public AggregateEnergyUsage usage(BigDecimal usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(BigDecimal usage) {
        this.usage = usage;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AggregateEnergyUsage company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public AggregateEnergyUsage site(Site site) {
        this.setSite(site);
        return this;
    }

    public EnergySource getEnergySource() {
        return this.energySource;
    }

    public void setEnergySource(EnergySource energySource) {
        this.energySource = energySource;
    }

    public AggregateEnergyUsage energySource(EnergySource energySource) {
        this.setEnergySource(energySource);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public AggregateEnergyUsage unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregateEnergyUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((AggregateEnergyUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AggregateEnergyUsage{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", usage=" + getUsage() +
            "}";
    }
}
