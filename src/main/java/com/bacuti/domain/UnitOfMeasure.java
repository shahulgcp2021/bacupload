package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UnitOfMeasure.
 */
@Entity
@Table(name = "unit_of_measure")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasure extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "site", "energySource", "unitOfMeasure" }, allowSetters = true)
    private Set<AggregateEnergyUsage> aggregateEnergyUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "product", "component", "unitOfMeasure" }, allowSetters = true)
    private Set<BillofMaterial> billofMaterials = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "unitOfMeasure", "energySources", "items", "wastedisposals" }, allowSetters = true)
    private Set<DefaultAverageEF> defaultAverageEFS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Set<Item> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "site", "unitOfMeasure" }, allowSetters = true)
    private Set<MachineUsage> machineUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "product", "unitOfMeasure" }, allowSetters = true)
    private Set<ProductUsageDetail> productUsageDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "item", "itemSupplier", "unitOfMeasure" }, allowSetters = true)
    private Set<PurchasedQty> purchasedQties = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "product", "unitOfMeasure", "machines" }, allowSetters = true)
    private Set<Routing> routings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitOfMeasure")
    @JsonIgnoreProperties(value = { "company", "product", "defaultAverageEF", "unitOfMeasure" }, allowSetters = true)
    private Set<Wastedisposal> wastedisposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitOfMeasure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UnitOfMeasure name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public UnitOfMeasure key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public UnitOfMeasure value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<AggregateEnergyUsage> getAggregateEnergyUsages() {
        return this.aggregateEnergyUsages;
    }

    public void setAggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        if (this.aggregateEnergyUsages != null) {
            this.aggregateEnergyUsages.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (aggregateEnergyUsages != null) {
            aggregateEnergyUsages.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.aggregateEnergyUsages = aggregateEnergyUsages;
    }

    public UnitOfMeasure aggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        this.setAggregateEnergyUsages(aggregateEnergyUsages);
        return this;
    }

    public UnitOfMeasure addAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.add(aggregateEnergyUsage);
        aggregateEnergyUsage.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.remove(aggregateEnergyUsage);
        aggregateEnergyUsage.setUnitOfMeasure(null);
        return this;
    }

    public Set<BillofMaterial> getBillofMaterials() {
        return this.billofMaterials;
    }

    public void setBillofMaterials(Set<BillofMaterial> billofMaterials) {
        if (this.billofMaterials != null) {
            this.billofMaterials.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (billofMaterials != null) {
            billofMaterials.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.billofMaterials = billofMaterials;
    }

    public UnitOfMeasure billofMaterials(Set<BillofMaterial> billofMaterials) {
        this.setBillofMaterials(billofMaterials);
        return this;
    }

    public UnitOfMeasure addBillofMaterial(BillofMaterial billofMaterial) {
        this.billofMaterials.add(billofMaterial);
        billofMaterial.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeBillofMaterial(BillofMaterial billofMaterial) {
        this.billofMaterials.remove(billofMaterial);
        billofMaterial.setUnitOfMeasure(null);
        return this;
    }

    public Set<DefaultAverageEF> getDefaultAverageEFS() {
        return this.defaultAverageEFS;
    }

    public void setDefaultAverageEFS(Set<DefaultAverageEF> defaultAverageEFS) {
        if (this.defaultAverageEFS != null) {
            this.defaultAverageEFS.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (defaultAverageEFS != null) {
            defaultAverageEFS.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.defaultAverageEFS = defaultAverageEFS;
    }

    public UnitOfMeasure defaultAverageEFS(Set<DefaultAverageEF> defaultAverageEFS) {
        this.setDefaultAverageEFS(defaultAverageEFS);
        return this;
    }

    public UnitOfMeasure addDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.defaultAverageEFS.add(defaultAverageEF);
        defaultAverageEF.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.defaultAverageEFS.remove(defaultAverageEF);
        defaultAverageEF.setUnitOfMeasure(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (items != null) {
            items.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.items = items;
    }

    public UnitOfMeasure items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public UnitOfMeasure addItem(Item item) {
        this.items.add(item);
        item.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeItem(Item item) {
        this.items.remove(item);
        item.setUnitOfMeasure(null);
        return this;
    }

    public Set<MachineUsage> getMachineUsages() {
        return this.machineUsages;
    }

    public void setMachineUsages(Set<MachineUsage> machineUsages) {
        if (this.machineUsages != null) {
            this.machineUsages.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (machineUsages != null) {
            machineUsages.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.machineUsages = machineUsages;
    }

    public UnitOfMeasure machineUsages(Set<MachineUsage> machineUsages) {
        this.setMachineUsages(machineUsages);
        return this;
    }

    public UnitOfMeasure addMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.add(machineUsage);
        machineUsage.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.remove(machineUsage);
        machineUsage.setUnitOfMeasure(null);
        return this;
    }

    public Set<ProductUsageDetail> getProductUsageDetails() {
        return this.productUsageDetails;
    }

    public void setProductUsageDetails(Set<ProductUsageDetail> productUsageDetails) {
        if (this.productUsageDetails != null) {
            this.productUsageDetails.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (productUsageDetails != null) {
            productUsageDetails.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.productUsageDetails = productUsageDetails;
    }

    public UnitOfMeasure productUsageDetails(Set<ProductUsageDetail> productUsageDetails) {
        this.setProductUsageDetails(productUsageDetails);
        return this;
    }

    public UnitOfMeasure addProductUsageDetail(ProductUsageDetail productUsageDetail) {
        this.productUsageDetails.add(productUsageDetail);
        productUsageDetail.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeProductUsageDetail(ProductUsageDetail productUsageDetail) {
        this.productUsageDetails.remove(productUsageDetail);
        productUsageDetail.setUnitOfMeasure(null);
        return this;
    }

    public Set<PurchasedQty> getPurchasedQties() {
        return this.purchasedQties;
    }

    public void setPurchasedQties(Set<PurchasedQty> purchasedQties) {
        if (this.purchasedQties != null) {
            this.purchasedQties.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (purchasedQties != null) {
            purchasedQties.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.purchasedQties = purchasedQties;
    }

    public UnitOfMeasure purchasedQties(Set<PurchasedQty> purchasedQties) {
        this.setPurchasedQties(purchasedQties);
        return this;
    }

    public UnitOfMeasure addPurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.add(purchasedQty);
        purchasedQty.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removePurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.remove(purchasedQty);
        purchasedQty.setUnitOfMeasure(null);
        return this;
    }

    public Set<Routing> getRoutings() {
        return this.routings;
    }

    public void setRoutings(Set<Routing> routings) {
        if (this.routings != null) {
            this.routings.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (routings != null) {
            routings.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.routings = routings;
    }

    public UnitOfMeasure routings(Set<Routing> routings) {
        this.setRoutings(routings);
        return this;
    }

    public UnitOfMeasure addRouting(Routing routing) {
        this.routings.add(routing);
        routing.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeRouting(Routing routing) {
        this.routings.remove(routing);
        routing.setUnitOfMeasure(null);
        return this;
    }

    public Set<Wastedisposal> getWastedisposals() {
        return this.wastedisposals;
    }

    public void setWastedisposals(Set<Wastedisposal> wastedisposals) {
        if (this.wastedisposals != null) {
            this.wastedisposals.forEach(i -> i.setUnitOfMeasure(null));
        }
        if (wastedisposals != null) {
            wastedisposals.forEach(i -> i.setUnitOfMeasure(this));
        }
        this.wastedisposals = wastedisposals;
    }

    public UnitOfMeasure wastedisposals(Set<Wastedisposal> wastedisposals) {
        this.setWastedisposals(wastedisposals);
        return this;
    }

    public UnitOfMeasure addWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.add(wastedisposal);
        wastedisposal.setUnitOfMeasure(this);
        return this;
    }

    public UnitOfMeasure removeWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.remove(wastedisposal);
        wastedisposal.setUnitOfMeasure(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasure)) {
            return false;
        }
        return getId() != null && getId().equals(((UnitOfMeasure) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasure{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
