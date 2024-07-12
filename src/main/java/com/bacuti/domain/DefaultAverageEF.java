package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A DefaultAverageEF.
 */
@Entity
@Table(name = "default_average_ef")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DefaultAverageEF extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_average_ef_seq")
    @SequenceGenerator(name = "default_average_ef_seq", sequenceName = "default_average_ef_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "detail")
    private String detail;

    @Column(name = "country_or_region")
    private String countryOrRegion;

    @Column(name = "emission_factor", precision = 21, scale = 2)
    private BigDecimal emissionFactor;

    @Column(name = "ef_source")
    private String efSource;

    @Column(name = "code")
    private String code;

    @Column(name = "code_type")
    private String codeType;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultAverageEF")
    @JsonIgnoreProperties(
        value = { "company", "site", "companyPublicEmission", "defaultAverageEF", "aggregateEnergyUsages" },
        allowSetters = true
    )
    private Set<EnergySource> energySources = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultAverageEF")
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Set<Item> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultAverageEF")
    @JsonIgnoreProperties(value = { "company", "product", "defaultAverageEF", "unitOfMeasure" }, allowSetters = true)
    private Set<Wastedisposal> wastedisposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DefaultAverageEF id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return this.domain;
    }

    public DefaultAverageEF domain(String domain) {
        this.setDomain(domain);
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDetail() {
        return this.detail;
    }

    public DefaultAverageEF detail(String detail) {
        this.setDetail(detail);
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCountryOrRegion() {
        return this.countryOrRegion;
    }

    public DefaultAverageEF countryOrRegion(String countryOrRegion) {
        this.setCountryOrRegion(countryOrRegion);
        return this;
    }

    public void setCountryOrRegion(String countryOrRegion) {
        this.countryOrRegion = countryOrRegion;
    }

    public BigDecimal getEmissionFactor() {
        return this.emissionFactor;
    }

    public DefaultAverageEF emissionFactor(BigDecimal emissionFactor) {
        this.setEmissionFactor(emissionFactor);
        return this;
    }

    public void setEmissionFactor(BigDecimal emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public String getEfSource() {
        return this.efSource;
    }

    public DefaultAverageEF efSource(String efSource) {
        this.setEfSource(efSource);
        return this;
    }

    public void setEfSource(String efSource) {
        this.efSource = efSource;
    }

    public String getCode() {
        return this.code;
    }

    public DefaultAverageEF code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return this.codeType;
    }

    public DefaultAverageEF codeType(String codeType) {
        this.setCodeType(codeType);
        return this;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public DefaultAverageEF unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public Set<EnergySource> getEnergySources() {
        return this.energySources;
    }

    public void setEnergySources(Set<EnergySource> energySources) {
        if (this.energySources != null) {
            this.energySources.forEach(i -> i.setDefaultAverageEF(null));
        }
        if (energySources != null) {
            energySources.forEach(i -> i.setDefaultAverageEF(this));
        }
        this.energySources = energySources;
    }

    public DefaultAverageEF energySources(Set<EnergySource> energySources) {
        this.setEnergySources(energySources);
        return this;
    }

    public DefaultAverageEF addEnergySource(EnergySource energySource) {
        this.energySources.add(energySource);
        energySource.setDefaultAverageEF(this);
        return this;
    }

    public DefaultAverageEF removeEnergySource(EnergySource energySource) {
        this.energySources.remove(energySource);
        energySource.setDefaultAverageEF(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setDefaultAverageEF(null));
        }
        if (items != null) {
            items.forEach(i -> i.setDefaultAverageEF(this));
        }
        this.items = items;
    }

    public DefaultAverageEF items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public DefaultAverageEF addItem(Item item) {
        this.items.add(item);
        item.setDefaultAverageEF(this);
        return this;
    }

    public DefaultAverageEF removeItem(Item item) {
        this.items.remove(item);
        item.setDefaultAverageEF(null);
        return this;
    }

    public Set<Wastedisposal> getWastedisposals() {
        return this.wastedisposals;
    }

    public void setWastedisposals(Set<Wastedisposal> wastedisposals) {
        if (this.wastedisposals != null) {
            this.wastedisposals.forEach(i -> i.setDefaultAverageEF(null));
        }
        if (wastedisposals != null) {
            wastedisposals.forEach(i -> i.setDefaultAverageEF(this));
        }
        this.wastedisposals = wastedisposals;
    }

    public DefaultAverageEF wastedisposals(Set<Wastedisposal> wastedisposals) {
        this.setWastedisposals(wastedisposals);
        return this;
    }

    public DefaultAverageEF addWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.add(wastedisposal);
        wastedisposal.setDefaultAverageEF(this);
        return this;
    }

    public DefaultAverageEF removeWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.remove(wastedisposal);
        wastedisposal.setDefaultAverageEF(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultAverageEF)) {
            return false;
        }
        return getId() != null && getId().equals(((DefaultAverageEF) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DefaultAverageEF{" +
            "id=" + getId() +
            ", domain='" + getDomain() + "'" +
            ", detail='" + getDetail() + "'" +
            ", countryOrRegion='" + getCountryOrRegion() + "'" +
            ", emissionFactor=" + getEmissionFactor() +
            ", efSource='" + getEfSource() + "'" +
            ", code='" + getCode() + "'" +
            ", codeType='" + getCodeType() + "'" +
            "}";
    }
}
