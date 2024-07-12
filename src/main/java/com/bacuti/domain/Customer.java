package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_name", unique = true)
    private String customerName;

    @Column(name = "description")
    private String description;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Customer customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return this.description;
    }

    public Customer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return this.website;
    }

    public Customer website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCountry() {
        return this.country;
    }

    public Customer country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSustainabilityContactName() {
        return this.sustainabilityContactName;
    }

    public Customer sustainabilityContactName(String sustainabilityContactName) {
        this.setSustainabilityContactName(sustainabilityContactName);
        return this;
    }

    public void setSustainabilityContactName(String sustainabilityContactName) {
        this.sustainabilityContactName = sustainabilityContactName;
    }

    public String getSustainabilityContactEmail() {
        return this.sustainabilityContactEmail;
    }

    public Customer sustainabilityContactEmail(String sustainabilityContactEmail) {
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

    public Customer company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", description='" + getDescription() + "'" +
            ", website='" + getWebsite() + "'" +
            ", country='" + getCountry() + "'" +
            ", sustainabilityContactName='" + getSustainabilityContactName() + "'" +
            ", sustainabilityContactEmail='" + getSustainabilityContactEmail() + "'" +
            "}";
    }
}
