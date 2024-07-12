package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A ProductionQty.
 */
@Entity
@Table(name = "production_qty")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionQty extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_qty_seq")
    @SequenceGenerator(name = "production_qty_seq", sequenceName = "production_qty_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "productionn_date")
    private LocalDate productionnDate;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "heat_number")
    private String heatNumber;

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

    public ProductionQty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionnDate() {
        return this.productionnDate;
    }

    public ProductionQty productionnDate(LocalDate productionnDate) {
        this.setProductionnDate(productionnDate);
        return this;
    }

    public void setProductionnDate(LocalDate productionnDate) {
        this.productionnDate = productionnDate;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public ProductionQty quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getHeatNumber() {
        return this.heatNumber;
    }

    public ProductionQty heatNumber(String heatNumber) {
        this.setHeatNumber(heatNumber);
        return this;
    }

    public void setHeatNumber(String heatNumber) {
        this.heatNumber = heatNumber;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ProductionQty company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ProductionQty site(Site site) {
        this.setSite(site);
        return this;
    }

    public Item getProduct() {
        return this.product;
    }

    public void setProduct(Item item) {
        this.product = item;
    }

    public ProductionQty product(Item item) {
        this.setProduct(item);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionQty)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductionQty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductionQty{" +
            "id=" + id +
            ", productionnDate=" + productionnDate +
            ", quantity=" + quantity +
            ", heatNumber='" + heatNumber + '\'' +
            ", company=" + company +
            ", site=" + site +
            ", product=" + product +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
