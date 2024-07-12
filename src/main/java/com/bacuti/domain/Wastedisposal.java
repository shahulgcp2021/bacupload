package com.bacuti.domain;

import com.bacuti.domain.enumeration.DisposalMethod;
import com.bacuti.domain.enumeration.Stage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Wastedisposal.
 */
@Entity
@Table(name = "wastedisposal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wastedisposal extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @Column(name = "waste_component")
    private String wasteComponent;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "disposal_method")
    private DisposalMethod disposalMethod;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wastedisposal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stage getStage() {
        return this.stage;
    }

    public Wastedisposal stage(Stage stage) {
        this.setStage(stage);
        return this;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getWasteComponent() {
        return this.wasteComponent;
    }

    public Wastedisposal wasteComponent(String wasteComponent) {
        this.setWasteComponent(wasteComponent);
        return this;
    }

    public void setWasteComponent(String wasteComponent) {
        this.wasteComponent = wasteComponent;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public Wastedisposal quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public DisposalMethod getDisposalMethod() {
        return this.disposalMethod;
    }

    public Wastedisposal disposalMethod(DisposalMethod disposalMethod) {
        this.setDisposalMethod(disposalMethod);
        return this;
    }

    public void setDisposalMethod(DisposalMethod disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Wastedisposal company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Item getProduct() {
        return this.product;
    }

    public void setProduct(Item item) {
        this.product = item;
    }

    public Wastedisposal product(Item item) {
        this.setProduct(item);
        return this;
    }

    public DefaultAverageEF getDefaultAverageEF() {
        return this.defaultAverageEF;
    }

    public void setDefaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.defaultAverageEF = defaultAverageEF;
    }

    public Wastedisposal defaultAverageEF(DefaultAverageEF defaultAverageEF) {
        this.setDefaultAverageEF(defaultAverageEF);
        return this;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Wastedisposal unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wastedisposal)) {
            return false;
        }
        return getId() != null && getId().equals(((Wastedisposal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wastedisposal{" +
            "id=" + getId() +
            ", stage='" + getStage() + "'" +
            ", wasteComponent='" + getWasteComponent() + "'" +
            ", quantity=" + getQuantity() +
            ", disposalMethod='" + getDisposalMethod() + "'" +
            "}";
    }
}
