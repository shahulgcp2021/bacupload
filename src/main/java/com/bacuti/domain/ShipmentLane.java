package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ShipmentLane.
 */
@Entity
@Table(name = "shipment_lane")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentLane extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "lane", unique = true)
    private String lane;

    @Column(name = "description")
    private String description;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shipmentLane")
    @JsonIgnoreProperties(value = { "company", "item", "shipmentLane", "site" }, allowSetters = true)
    private Set<ItemShipment> itemShipments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shipmentLane")
    @JsonIgnoreProperties(value = { "shipmentLane" }, allowSetters = true)
    private Set<ShipmentLeg> shipmentLegs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentLane id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLane() {
        return this.lane;
    }

    public ShipmentLane lane(String lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getDescription() {
        return this.description;
    }

    public ShipmentLane description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ShipmentLane company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<ItemShipment> getItemShipments() {
        return this.itemShipments;
    }

    public void setItemShipments(Set<ItemShipment> itemShipments) {
        if (this.itemShipments != null) {
            this.itemShipments.forEach(i -> i.setShipmentLane(null));
        }
        if (itemShipments != null) {
            itemShipments.forEach(i -> i.setShipmentLane(this));
        }
        this.itemShipments = itemShipments;
    }

    public ShipmentLane itemShipments(Set<ItemShipment> itemShipments) {
        this.setItemShipments(itemShipments);
        return this;
    }

    public ShipmentLane addItemShipment(ItemShipment itemShipment) {
        this.itemShipments.add(itemShipment);
        itemShipment.setShipmentLane(this);
        return this;
    }

    public ShipmentLane removeItemShipment(ItemShipment itemShipment) {
        this.itemShipments.remove(itemShipment);
        itemShipment.setShipmentLane(null);
        return this;
    }

    public Set<ShipmentLeg> getShipmentLegs() {
        return this.shipmentLegs;
    }

    public void setShipmentLegs(Set<ShipmentLeg> shipmentLegs) {
        if (this.shipmentLegs != null) {
            this.shipmentLegs.forEach(i -> i.setShipmentLane(null));
        }
        if (shipmentLegs != null) {
            shipmentLegs.forEach(i -> i.setShipmentLane(this));
        }
        this.shipmentLegs = shipmentLegs;
    }

    public ShipmentLane shipmentLegs(Set<ShipmentLeg> shipmentLegs) {
        this.setShipmentLegs(shipmentLegs);
        return this;
    }

    public ShipmentLane addShipmentLeg(ShipmentLeg shipmentLeg) {
        this.shipmentLegs.add(shipmentLeg);
        shipmentLeg.setShipmentLane(this);
        return this;
    }

    public ShipmentLane removeShipmentLeg(ShipmentLeg shipmentLeg) {
        this.shipmentLegs.remove(shipmentLeg);
        shipmentLeg.setShipmentLane(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentLane)) {
            return false;
        }
        return getId() != null && getId().equals(((ShipmentLane) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentLane{" +
            "id=" + getId() +
            ", lane='" + getLane() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
