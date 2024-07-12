package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Routing.
 */
@Entity
@Table(name = "routing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Routing extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "step", precision = 21, scale = 2)
    private BigDecimal step;

    @Column(name = "duration", precision = 21, scale = 2)
    private BigDecimal duration;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "routing")
    @JsonIgnoreProperties(value = { "company", "routing" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Routing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getStep() {
        return this.step;
    }

    public Routing step(BigDecimal step) {
        this.setStep(step);
        return this;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }

    public BigDecimal getDuration() {
        return this.duration;
    }

    public Routing duration(BigDecimal duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Routing company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getProduct() {
        return this.product;
    }

    public void setProduct(Item item) {
        this.product = item;
    }

    public Routing product(Item item) {
        this.setProduct(item);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Routing unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public void setMachines(Set<Machine> machines) {
        if (this.machines != null) {
            this.machines.forEach(i -> i.setRouting(null));
        }
        if (machines != null) {
            machines.forEach(i -> i.setRouting(this));
        }
        this.machines = machines;
    }

    public Routing machines(Set<Machine> machines) {
        this.setMachines(machines);
        return this;
    }

    public Routing addMachine(Machine machine) {
        this.machines.add(machine);
        machine.setRouting(this);
        return this;
    }

    public Routing removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.setRouting(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Routing)) {
            return false;
        }
        return getId() != null && getId().equals(((Routing) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Routing{" +
            "id=" + getId() +
            ", step=" + getStep() +
            ", duration=" + getDuration() +
            "}";
    }
}
