package com.bacuti.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Supplier.
 */
@Entity
@Table(name = "supplier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Supplier extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @SequenceGenerator(name = "supplier_seq", sequenceName = "supplier_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_name", unique = true)
    private String supplierName;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "website")
    private String website;

    @Column(name = "country")
    private String country;

    @Column(name = "sustainability_contact_name")
    private String sustainabilityContactName;

    @Column(name = "sustainability_contact_email")
    private String sustainabilityContactEmail;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @JsonIgnoreProperties(value = { "company", "item", "supplier", "companyPublicEmission", "purchasedQties" }, allowSetters = true)
    private Set<ItemSupplier> itemSuppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Supplier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public Supplier supplierName(String supplierName) {
        this.setSupplierName(supplierName);
        return this;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDescription() {
        return this.description;
    }

    public Supplier description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public Supplier category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsite() {
        return this.website;
    }

    public Supplier website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountry() {
        return this.country;
    }

    public Supplier country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSustainabilityContactName() {
        return this.sustainabilityContactName;
    }

    public Supplier sustainabilityContactName(String sustainabilityContactName) {
        this.setSustainabilityContactName(sustainabilityContactName);
        return this;
    }

    public void setSustainabilityContactName(String sustainabilityContactName) {
        this.sustainabilityContactName = sustainabilityContactName;
    }

    public String getSustainabilityContactEmail() {
        return this.sustainabilityContactEmail;
    }

    public Supplier sustainabilityContactEmail(String sustainabilityContactEmail) {
        this.setSustainabilityContactEmail(sustainabilityContactEmail);
        return this;
    }

    public void setSustainabilityContactEmail(String sustainabilityContactEmail) {
        this.sustainabilityContactEmail = sustainabilityContactEmail;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Supplier company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<ItemSupplier> getItemSuppliers() {
        return this.itemSuppliers;
    }

    public void setItemSuppliers(Set<ItemSupplier> itemSuppliers) {
        if (this.itemSuppliers != null) {
            this.itemSuppliers.forEach(i -> i.setSupplier(null));
        }
        if (itemSuppliers != null) {
            itemSuppliers.forEach(i -> i.setSupplier(this));
        }
        this.itemSuppliers = itemSuppliers;
    }

    public Supplier itemSuppliers(Set<ItemSupplier> itemSuppliers) {
        this.setItemSuppliers(itemSuppliers);
        return this;
    }

    public Supplier addItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.add(itemSupplier);
        itemSupplier.setSupplier(this);
        return this;
    }

    public Supplier removeItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.remove(itemSupplier);
        itemSupplier.setSupplier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return getId() != null && getId().equals(((Supplier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", supplierName='" + getSupplierName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", website='" + getWebsite() + "'" +
            ", country='" + getCountry() + "'" +
            ", sustainabilityContactName='" + getSustainabilityContactName() + "'" +
            ", sustainabilityContactEmail='" + getSustainabilityContactEmail() + "'" +
            "}";
    }
}
