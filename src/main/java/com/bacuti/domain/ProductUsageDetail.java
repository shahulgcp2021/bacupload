package com.bacuti.domain;

import com.bacuti.domain.enumeration.EmissionSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ProductUsageDetail.
 */
@Entity
@Table(name = "product_usage_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductUsageDetail extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "useful_life_yrs", precision = 21, scale = 2)
    private BigDecimal usefulLifeYrs;

    @Enumerated(EnumType.STRING)
    @Column(name = "emission_source")
    private EmissionSource emissionSource;

    @Column(name = "detail")
    private String detail;

    @Column(name = "avg_quantity_per_day", precision = 21, scale = 2)
    private BigDecimal avgQuantityPerDay;

    @Column(name = "proportion", precision = 21, scale = 2)
    private BigDecimal proportion;

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
    private Item product;

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

    public ProductUsageDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUsefulLifeYrs() {
        return this.usefulLifeYrs;
    }

    public ProductUsageDetail usefulLifeYrs(BigDecimal usefulLifeYrs) {
        this.setUsefulLifeYrs(usefulLifeYrs);
        return this;
    }

    public void setUsefulLifeYrs(BigDecimal usefulLifeYrs) {
        this.usefulLifeYrs = usefulLifeYrs;
    }

    public EmissionSource getEmissionSource() {
        return this.emissionSource;
    }

    public ProductUsageDetail emissionSource(EmissionSource emissionSource) {
        this.setEmissionSource(emissionSource);
        return this;
    }

    public void setEmissionSource(EmissionSource emissionSource) {
        this.emissionSource = emissionSource;
    }

    public String getDetail() {
        return this.detail;
    }

    public ProductUsageDetail detail(String detail) {
        this.setDetail(detail);
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getAvgQuantityPerDay() {
        return this.avgQuantityPerDay;
    }

    public ProductUsageDetail avgQuantityPerDay(BigDecimal avgQuantityPerDay) {
        this.setAvgQuantityPerDay(avgQuantityPerDay);
        return this;
    }

    public void setAvgQuantityPerDay(BigDecimal avgQuantityPerDay) {
        this.avgQuantityPerDay = avgQuantityPerDay;
    }

    public BigDecimal getProportion() {
        return this.proportion;
    }

    public ProductUsageDetail proportion(BigDecimal proportion) {
        this.setProportion(proportion);
        return this;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ProductUsageDetail company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getProduct() {
        return this.product;
    }

    public void setProduct(Item item) {
        this.product = item;
    }

    public ProductUsageDetail product(Item item) {
        this.setProduct(item);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public ProductUsageDetail unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductUsageDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductUsageDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductUsageDetail{" +
            "id=" + getId() +
            ", usefulLifeYrs=" + getUsefulLifeYrs() +
            ", emissionSource='" + getEmissionSource() + "'" +
            ", detail='" + getDetail() + "'" +
            ", avgQuantityPerDay=" + getAvgQuantityPerDay() +
            ", proportion=" + getProportion() +
            "}";
    }
}
