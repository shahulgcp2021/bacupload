package com.bacuti.domain;

import com.bacuti.domain.enumeration.EfUnits;
import com.bacuti.domain.enumeration.EnergyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A EnergySource.
 */
@Entity
@Table(name = "energy_source")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnergySource extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "energy_source_seq")
    @SequenceGenerator(name = "energy_source_seq", sequenceName = "energy_source_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy_type")
    private EnergyType energyType;

    @Column(name = "description")
    private String description;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "co_2_emission_factor", precision = 21, scale = 2)
    private BigDecimal co2EmissionFactor;

    @Column(name = "upstream_co_2_ef", precision = 21, scale = 2)
    private BigDecimal upstreamCo2EF;

    @Enumerated(EnumType.STRING)
    @Column(name = "ef_units")
    private EfUnits efUnits;

    @Column(name = "source_for_ef")
    private String sourceForEf;

    @Column(name = "percent_renewable_src", precision = 21, scale = 2)
    private BigDecimal percentRenewableSrc;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "unitOfMeasure", "energySources", "items", "wastedisposals" }, allowSetters = true)
    private DefaultAverageEF defaultAverageEF;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "energySource")
    @JsonIgnoreProperties(value = { "company", "site", "energySource", "unitOfMeasure" }, allowSetters = true)
    private Set<AggregateEnergyUsage> aggregateEnergyUsages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EnergySource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return this.energyType;
    }

    public EnergySource energyType(EnergyType energyType) {
        this.setEnergyType(energyType);
        return this;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public String getDescription() {
        return this.description;
    }

    public EnergySource description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public EnergySource supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getCo2EmissionFactor() {
        return this.co2EmissionFactor;
    }

    public EnergySource co2EmissionFactor(BigDecimal co2EmissionFactor) {
        this.setCo2EmissionFactor(co2EmissionFactor);
        return this;
    }

    public void setCo2EmissionFactor(BigDecimal co2EmissionFactor) {
        this.co2EmissionFactor = co2EmissionFactor;
    }

    public BigDecimal getUpstreamCo2EF() {
        return this.upstreamCo2EF;
    }

    public EnergySource upstreamCo2EF(BigDecimal upstreamCo2EF) {
        this.setUpstreamCo2EF(upstreamCo2EF);
        return this;
    }

    public void setUpstreamCo2EF(BigDecimal upstreamCo2EF) {
        this.upstreamCo2EF = upstreamCo2EF;
    }

    public EfUnits getEfUnits() {
        return this.efUnits;
    }

    public EnergySource efUnits(EfUnits efUnits) {
        this.setEfUnits(efUnits);
        return this;
    }

    public void setEfUnits(EfUnits efUnits) {
        this.efUnits = efUnits;
    }

    public String getSourceForEf() {
        return this.sourceForEf;
    }

    public EnergySource sourceForEf(String sourceForEf) {
        this.setSourceForEf(sourceForEf);
        return this;
    }

    public void setSourceForEf(String sourceForEf) {
        this.sourceForEf = sourceForEf;
    }

    public BigDecimal getPercentRenewableSrc() {
        return this.percentRenewableSrc;
    }

    public EnergySource percentRenewableSrc(BigDecimal percentRenewableSrc) {
        this.setPercentRenewableSrc(percentRenewableSrc);
        return this;
    }

    public void setPercentRenewableSrc(BigDecimal percentRenewableSrc) {
        this.percentRenewableSrc = percentRenewableSrc;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public EnergySource company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public EnergySource site(Site site) {
        this.setSite(site);
        return this;
    }

    public CompanyPublicEmission getCompanyPublicEmission() {
        return this.companyPublicEmission;
    }

    public void setCompanyPublicEmission(CompanyPublicEmission companyPublicEmission) {
        this.companyPublicEmission = companyPublicEmission;
    }

    public EnergySource companyPublicEmission(CompanyPublicEmission companyPublicEmission) {
        this.setCompanyPublicEmission(companyPublicEmission);
        return this;
    }

    public DefaultAverageEF getDefaultAverageEF() {
        return this.defaultAverageEF;
    }

    public void setDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.defaultAverageEF = defaultAverageEF;
    }

    public EnergySource defaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.setDefaultAverageEF(defaultAverageEF);
        return this;
    }

    public Set<AggregateEnergyUsage> getAggregateEnergyUsages() {
        return this.aggregateEnergyUsages;
    }

    public void setAggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        if (this.aggregateEnergyUsages != null) {
            this.aggregateEnergyUsages.forEach(i -> i.setEnergySource(null));
        }
        if (aggregateEnergyUsages != null) {
            aggregateEnergyUsages.forEach(i -> i.setEnergySource(this));
        }
        this.aggregateEnergyUsages = aggregateEnergyUsages;
    }

    public EnergySource aggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        this.setAggregateEnergyUsages(aggregateEnergyUsages);
        return this;
    }

    public EnergySource addAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.add(aggregateEnergyUsage);
        aggregateEnergyUsage.setEnergySource(this);
        return this;
    }

    public EnergySource removeAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.remove(aggregateEnergyUsage);
        aggregateEnergyUsage.setEnergySource(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnergySource)) {
            return false;
        }
        return getId() != null && getId().equals(((EnergySource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnergySource{" +
            "id=" + getId() +
            ", energyType='" + getEnergyType() + "'" +
            ", description='" + getDescription() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", co2EmissionFactor=" + getCo2EmissionFactor() +
            ", upstreamCo2EF=" + getUpstreamCo2EF() +
            ", efUnits='" + getEfUnits() + "'" +
            ", sourceForEf='" + getSourceForEf() + "'" +
            ", percentRenewableSrc=" + getPercentRenewableSrc() +
            "}";
    }
}
