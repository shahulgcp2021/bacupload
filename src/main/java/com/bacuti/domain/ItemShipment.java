package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A ItemShipment.
 */
@Entity
@Table(name = "item_shipment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemShipment extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "shipmentdate")
    private LocalDate shipmentdate;

    @Column(name = "shipper")
    private String shipper;

    @Column(name = "upstream")
    private Boolean upstream;

    @Column(name = "quantity_shipped", precision = 21, scale = 2)
    private BigDecimal quantityShipped;

    @Column(name = "weight_in_kg", precision = 21, scale = 2)
    private BigDecimal weightInKg;

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
    @JsonIgnoreProperties(value = { "company", "itemShipments", "shipmentLegs" }, allowSetters = true)
    private ShipmentLane shipmentLane;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemShipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getShipmentdate() {
        return this.shipmentdate;
    }

    public ItemShipment shipmentdate(LocalDate shipmentdate) {
        this.setShipmentdate(shipmentdate);
        return this;
    }

    public void setShipmentdate(LocalDate shipmentdate) {
        this.shipmentdate = shipmentdate;
    }

    public String getShipper() {
        return this.shipper;
    }

    public ItemShipment shipper(String shipper) {
        this.setShipper(shipper);
        return this;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public Boolean getUpstream() {
        return this.upstream;
    }

    public ItemShipment upstream(Boolean upstream) {
        this.setUpstream(upstream);
        return this;
    }

    public void setUpstream(Boolean upstream) {
        this.upstream = upstream;
    }

    public BigDecimal getQuantityShipped() {
        return this.quantityShipped;
    }

    public ItemShipment quantityShipped(BigDecimal quantityShipped) {
        this.setQuantityShipped(quantityShipped);
        return this;
    }

    public void setQuantityShipped(BigDecimal quantityShipped) {
        this.quantityShipped = quantityShipped;
    }

    public BigDecimal getWeightInKg() {
        return this.weightInKg;
    }

    public ItemShipment weightInKg(BigDecimal weightInKg) {
        this.setWeightInKg(weightInKg);
        return this;
    }

    public void setWeightInKg(BigDecimal weightInKg) {
        this.weightInKg = weightInKg;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ItemShipment company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemShipment item(Item item) {
        this.setItem(item);
        return this;
    }

    public ShipmentLane getShipmentLane() {
        return this.shipmentLane;
    }

    public void setShipmentLane(ShipmentLane shipmentLane) {
        this.shipmentLane = shipmentLane;
    }

    public ItemShipment shipmentLane(ShipmentLane shipmentLane) {
        this.setShipmentLane(shipmentLane);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ItemShipment site(Site site) {
        this.setSite(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemShipment)) {
            return false;
        }
        return getId() != null && getId().equals(((ItemShipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemShipment{" +
            "id=" + getId() +
            ", shipmentdate='" + getShipmentdate() + "'" +
            ", shipper='" + getShipper() + "'" +
            ", upstream='" + getUpstream() + "'" +
            ", quantityShipped=" + getQuantityShipped() +
            ", weightInKg=" + getWeightInKg() +
            "}";
    }
}
