package com.bacuti.domain;

import com.bacuti.domain.enumeration.AggregatedGoodsCategory;
import com.bacuti.domain.enumeration.ItemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "item_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_name", unique = true)
    private String itemName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private ItemType itemType;

    @Column(name = "item_category")
    private String itemCategory;

    @Column(name = "purchased_item")
    private Boolean purchasedItem;

    @Column(name = "cbam_impacted")
    private Boolean cbamImpacted;

    @Column(name = "cn_code")
    private String cnCode;

    @Column(name = "cn_name")
    private String cnName;

    @Column(name = "percent_mn", precision = 21, scale = 2)
    private BigDecimal percentMn;

    @Column(name = "percent_cr", precision = 21, scale = 2)
    private BigDecimal percentCr;

    @Column(name = "percent_ni", precision = 21, scale = 2)
    private BigDecimal percentNi;

    @Column(name = "percent_carbon", precision = 21, scale = 2)
    private BigDecimal percentCarbon;

    @Column(name = "percent_other_alloys", precision = 21, scale = 2)
    private BigDecimal percentOtherAlloys;

    @Column(name = "percent_other_materials", precision = 21, scale = 2)
    private BigDecimal percentOtherMaterials;

    @Column(name = "percent_preconsumer_scrap", precision = 21, scale = 2)
    private BigDecimal percentPreconsumerScrap;

    @Column(name = "scrap_per_item", precision = 21, scale = 2)
    private BigDecimal scrapPerItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregated_goods_category")
    private AggregatedGoodsCategory aggregatedGoodsCategory;

    @Column(name = "ef_units", precision = 21, scale = 2)
    private BigDecimal efUnits;

    @Column(name = "ef_scaling_factor", precision = 21, scale = 2)
    private BigDecimal efScalingFactor;

    @Column(name = "supplier_emission_multipler", precision = 21, scale = 2)
    private BigDecimal supplierEmissionMultipler;

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
    @JsonIgnoreProperties(value = { "unitOfMeasure", "energySources", "items", "wastedisposals" }, allowSetters = true)
    private DefaultAverageEF defaultAverageEF;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    @JsonIgnoreProperties(value = { "company", "item", "shipmentLane", "site" }, allowSetters = true)
    private Set<ItemShipment> itemShipments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    @JsonIgnoreProperties(value = { "company", "item", "supplier", "companyPublicEmission", "purchasedQties" }, allowSetters = true)
    private Set<ItemSupplier> itemSuppliers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    @JsonIgnoreProperties(value = { "company", "item", "itemSupplier", "unitOfMeasure" }, allowSetters = true)
    private Set<PurchasedQty> purchasedQties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Item itemName(String itemName) {
        this.setItemName(itemName);
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return this.description;
    }

    public Item description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public Item itemType(ItemType itemType) {
        this.setItemType(itemType);
        return this;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemCategory() {
        return this.itemCategory;
    }

    public Item itemCategory(String itemCategory) {
        this.setItemCategory(itemCategory);
        return this;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Boolean getPurchasedItem() {
        return this.purchasedItem;
    }

    public Item purchasedItem(Boolean purchasedItem) {
        this.setPurchasedItem(purchasedItem);
        return this;
    }

    public void setPurchasedItem(Boolean purchasedItem) {
        this.purchasedItem = purchasedItem;
    }

    public Boolean getCbamImpacted() {
        return this.cbamImpacted;
    }

    public Item cbamImpacted(Boolean cbamImpacted) {
        this.setCbamImpacted(cbamImpacted);
        return this;
    }

    public void setCbamImpacted(Boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public String getCnCode() {
        return this.cnCode;
    }

    public Item cnCode(String cnCode) {
        this.setCnCode(cnCode);
        return this;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public String getCnName() {
        return this.cnName;
    }

    public Item cnName(String cnName) {
        this.setCnName(cnName);
        return this;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public BigDecimal getPercentMn() {
        return this.percentMn;
    }

    public Item percentMn(BigDecimal percentMn) {
        this.setPercentMn(percentMn);
        return this;
    }

    public void setPercentMn(BigDecimal percentMn) {
        this.percentMn = percentMn;
    }

    public BigDecimal getPercentCr() {
        return this.percentCr;
    }

    public Item percentCr(BigDecimal percentCr) {
        this.setPercentCr(percentCr);
        return this;
    }

    public void setPercentCr(BigDecimal percentCr) {
        this.percentCr = percentCr;
    }

    public BigDecimal getPercentNi() {
        return this.percentNi;
    }

    public Item percentNi(BigDecimal percentNi) {
        this.setPercentNi(percentNi);
        return this;
    }

    public void setPercentNi(BigDecimal percentNi) {
        this.percentNi = percentNi;
    }

    public BigDecimal getPercentCarbon() {
        return this.percentCarbon;
    }

    public Item percentCarbon(BigDecimal percentCarbon) {
        this.setPercentCarbon(percentCarbon);
        return this;
    }

    public void setPercentCarbon(BigDecimal percentCarbon) {
        this.percentCarbon = percentCarbon;
    }

    public BigDecimal getPercentOtherAlloys() {
        return this.percentOtherAlloys;
    }

    public Item percentOtherAlloys(BigDecimal percentOtherAlloys) {
        this.setPercentOtherAlloys(percentOtherAlloys);
        return this;
    }

    public void setPercentOtherAlloys(BigDecimal percentOtherAlloys) {
        this.percentOtherAlloys = percentOtherAlloys;
    }

    public BigDecimal getPercentOtherMaterials() {
        return this.percentOtherMaterials;
    }

    public Item percentOtherMaterials(BigDecimal percentOtherMaterials) {
        this.setPercentOtherMaterials(percentOtherMaterials);
        return this;
    }

    public void setPercentOtherMaterials(BigDecimal percentOtherMaterials) {
        this.percentOtherMaterials = percentOtherMaterials;
    }

    public BigDecimal getPercentPreconsumerScrap() {
        return this.percentPreconsumerScrap;
    }

    public Item percentPreconsumerScrap(BigDecimal percentPreconsumerScrap) {
        this.setPercentPreconsumerScrap(percentPreconsumerScrap);
        return this;
    }

    public void setPercentPreconsumerScrap(BigDecimal percentPreconsumerScrap) {
        this.percentPreconsumerScrap = percentPreconsumerScrap;
    }

    public BigDecimal getScrapPerItem() {
        return this.scrapPerItem;
    }

    public Item scrapPerItem(BigDecimal scrapPerItem) {
        this.setScrapPerItem(scrapPerItem);
        return this;
    }

    public void setScrapPerItem(BigDecimal scrapPerItem) {
        this.scrapPerItem = scrapPerItem;
    }

    public AggregatedGoodsCategory getAggregatedGoodsCategory() {
        return this.aggregatedGoodsCategory;
    }

    public Item aggregatedGoodsCategory(AggregatedGoodsCategory aggregatedGoodsCategory) {
        this.setAggregatedGoodsCategory(aggregatedGoodsCategory);
        return this;
    }

    public void setAggregatedGoodsCategory(AggregatedGoodsCategory aggregatedGoodsCategory) {
        this.aggregatedGoodsCategory = aggregatedGoodsCategory;
    }

    public BigDecimal getEfUnits() {
        return this.efUnits;
    }

    public Item efUnits(BigDecimal efUnits) {
        this.setEfUnits(efUnits);
        return this;
    }

    public void setEfUnits(BigDecimal efUnits) {
        this.efUnits = efUnits;
    }

    public BigDecimal getEfScalingFactor() {
        return this.efScalingFactor;
    }

    public Item efScalingFactor(BigDecimal efScalingFactor) {
        this.setEfScalingFactor(efScalingFactor);
        return this;
    }

    public void setEfScalingFactor(BigDecimal efScalingFactor) {
        this.efScalingFactor = efScalingFactor;
    }

    public BigDecimal getSupplierEmissionMultipler() {
        return this.supplierEmissionMultipler;
    }

    public Item supplierEmissionMultipler(BigDecimal supplierEmissionMultipler) {
        this.setSupplierEmissionMultipler(supplierEmissionMultipler);
        return this;
    }

    public void setSupplierEmissionMultipler(BigDecimal supplierEmissionMultipler) {
        this.supplierEmissionMultipler = supplierEmissionMultipler;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Item company(Company company) {
        this.setCompany(company);
        return this;
    }

    public DefaultAverageEF getDefaultAverageEF() {
        return this.defaultAverageEF;
    }

    public void setDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.defaultAverageEF = defaultAverageEF;
    }

    public Item defaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.setDefaultAverageEF(defaultAverageEF);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Item unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public Set<ItemShipment> getItemShipments() {
        return this.itemShipments;
    }

    public void setItemShipments(Set<ItemShipment> itemShipments) {
        if (this.itemShipments != null) {
            this.itemShipments.forEach(i -> i.setItem(null));
        }
        if (itemShipments != null) {
            itemShipments.forEach(i -> i.setItem(this));
        }
        this.itemShipments = itemShipments;
    }

    public Item itemShipments(Set<ItemShipment> itemShipments) {
        this.setItemShipments(itemShipments);
        return this;
    }

    public Item addItemShipment(ItemShipment itemShipment) {
        this.itemShipments.add(itemShipment);
        itemShipment.setItem(this);
        return this;
    }

    public Item removeItemShipment(ItemShipment itemShipment) {
        this.itemShipments.remove(itemShipment);
        itemShipment.setItem(null);
        return this;
    }

    public Set<ItemSupplier> getItemSuppliers() {
        return this.itemSuppliers;
    }

    public void setItemSuppliers(Set<ItemSupplier> itemSuppliers) {
        if (this.itemSuppliers != null) {
            this.itemSuppliers.forEach(i -> i.setItem(null));
        }
        if (itemSuppliers != null) {
            itemSuppliers.forEach(i -> i.setItem(this));
        }
        this.itemSuppliers = itemSuppliers;
    }

    public Item itemSuppliers(Set<ItemSupplier> itemSuppliers) {
        this.setItemSuppliers(itemSuppliers);
        return this;
    }

    public Item addItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.add(itemSupplier);
        itemSupplier.setItem(this);
        return this;
    }

    public Item removeItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.remove(itemSupplier);
        itemSupplier.setItem(null);
        return this;
    }

    public Set<PurchasedQty> getPurchasedQties() {
        return this.purchasedQties;
    }

    public void setPurchasedQties(Set<PurchasedQty> purchasedQties) {
        if (this.purchasedQties != null) {
            this.purchasedQties.forEach(i -> i.setItem(null));
        }
        if (purchasedQties != null) {
            purchasedQties.forEach(i -> i.setItem(this));
        }
        this.purchasedQties = purchasedQties;
    }

    public Item purchasedQties(Set<PurchasedQty> purchasedQties) {
        this.setPurchasedQties(purchasedQties);
        return this;
    }

    public Item addPurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.add(purchasedQty);
        purchasedQty.setItem(this);
        return this;
    }

    public Item removePurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.remove(purchasedQty);
        purchasedQty.setItem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return getId() != null && getId().equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", description='" + getDescription() + "'" +
            ", itemType='" + getItemType() + "'" +
            ", itemCategory='" + getItemCategory() + "'" +
            ", purchasedItem='" + getPurchasedItem() + "'" +
            ", cbamImpacted='" + getCbamImpacted() + "'" +
            ", cnCode='" + getCnCode() + "'" +
            ", cnName='" + getCnName() + "'" +
            ", percentMn=" + getPercentMn() +
            ", percentCr=" + getPercentCr() +
            ", percentNi=" + getPercentNi() +
            ", percentCarbon=" + getPercentCarbon() +
            ", percentOtherAlloys=" + getPercentOtherAlloys() +
            ", percentOtherMaterials=" + getPercentOtherMaterials() +
            ", percentPreconsumerScrap=" + getPercentPreconsumerScrap() +
            ", scrapPerItem=" + getScrapPerItem() +
            ", aggregatedGoodsCategory='" + getAggregatedGoodsCategory() + "'" +
            ", efUnits=" + getEfUnits() +
            ", efScalingFactor=" + getEfScalingFactor() +
            ", supplierEmissionMultipler=" + getSupplierEmissionMultipler() +
            "}";
    }
}
