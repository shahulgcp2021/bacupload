package com.bacuti.domain;

import com.bacuti.domain.enumeration.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A CarbonPricePayment.
 */
@Entity
@Table(name = "carbon_price_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarbonPricePayment extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "eur_fx_rate", precision = 21, scale = 2)
    private BigDecimal eurFxRate;

    @Column(name = "amount_in_eur", precision = 21, scale = 2)
    private BigDecimal amountInEur;

    @Column(name = "emission_from_date")
    private LocalDate emissionFromDate;

    @Column(name = "emission_to_date")
    private LocalDate emissionToDate;

    @Column(name = "form_of_carbon_price")
    private String formOfCarbonPrice;

    @Column(name = "percent_emission_by_price", precision = 21, scale = 2)
    private BigDecimal percentEmissionByPrice;

    @Column(name = "form_of_rebate")
    private String formOfRebate;

    @Column(name = "percent_emission_by_rebate", precision = 21, scale = 2)
    private BigDecimal percentEmissionByRebate;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarbonPricePayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public CarbonPricePayment paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CarbonPricePayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public CarbonPricePayment currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getEurFxRate() {
        return this.eurFxRate;
    }

    public CarbonPricePayment eurFxRate(BigDecimal eurFxRate) {
        this.setEurFxRate(eurFxRate);
        return this;
    }

    public void setEurFxRate(BigDecimal eurFxRate) {
        this.eurFxRate = eurFxRate;
    }

    public BigDecimal getAmountInEur() {
        return this.amountInEur;
    }

    public CarbonPricePayment amountInEur(BigDecimal amountInEur) {
        this.setAmountInEur(amountInEur);
        return this;
    }

    public void setAmountInEur(BigDecimal amountInEur) {
        this.amountInEur = amountInEur;
    }

    public LocalDate getEmissionFromDate() {
        return this.emissionFromDate;
    }

    public CarbonPricePayment emissionFromDate(LocalDate emissionFromDate) {
        this.setEmissionFromDate(emissionFromDate);
        return this;
    }

    public void setEmissionFromDate(LocalDate emissionFromDate) {
        this.emissionFromDate = emissionFromDate;
    }

    public LocalDate getEmissionToDate() {
        return this.emissionToDate;
    }

    public CarbonPricePayment emissionToDate(LocalDate emissionToDate) {
        this.setEmissionToDate(emissionToDate);
        return this;
    }

    public void setEmissionToDate(LocalDate emissionToDate) {
        this.emissionToDate = emissionToDate;
    }

    public String getFormOfCarbonPrice() {
        return this.formOfCarbonPrice;
    }

    public CarbonPricePayment formOfCarbonPrice(String formOfCarbonPrice) {
        this.setFormOfCarbonPrice(formOfCarbonPrice);
        return this;
    }

    public void setFormOfCarbonPrice(String formOfCarbonPrice) {
        this.formOfCarbonPrice = formOfCarbonPrice;
    }

    public BigDecimal getPercentEmissionByPrice() {
        return this.percentEmissionByPrice;
    }

    public CarbonPricePayment percentEmissionByPrice(BigDecimal percentEmissionByPrice) {
        this.setPercentEmissionByPrice(percentEmissionByPrice);
        return this;
    }

    public void setPercentEmissionByPrice(BigDecimal percentEmissionByPrice) {
        this.percentEmissionByPrice = percentEmissionByPrice;
    }

    public String getFormOfRebate() {
        return this.formOfRebate;
    }

    public CarbonPricePayment formOfRebate(String formOfRebate) {
        this.setFormOfRebate(formOfRebate);
        return this;
    }

    public void setFormOfRebate(String formOfRebate) {
        this.formOfRebate = formOfRebate;
    }

    public BigDecimal getPercentEmissionByRebate() {
        return this.percentEmissionByRebate;
    }

    public CarbonPricePayment percentEmissionByRebate(BigDecimal percentEmissionByRebate) {
        this.setPercentEmissionByRebate(percentEmissionByRebate);
        return this;
    }

    public void setPercentEmissionByRebate(BigDecimal percentEmissionByRebate) {
        this.percentEmissionByRebate = percentEmissionByRebate;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CarbonPricePayment company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public CarbonPricePayment site(Site site) {
        this.setSite(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarbonPricePayment)) {
            return false;
        }
        return getId() != null && getId().equals(((CarbonPricePayment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarbonPricePayment{" +
            "id=" + getId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", eurFxRate=" + getEurFxRate() +
            ", amountInEur=" + getAmountInEur() +
            ", emissionFromDate='" + getEmissionFromDate() + "'" +
            ", emissionToDate='" + getEmissionToDate() + "'" +
            ", formOfCarbonPrice='" + getFormOfCarbonPrice() + "'" +
            ", percentEmissionByPrice=" + getPercentEmissionByPrice() +
            ", formOfRebate='" + getFormOfRebate() + "'" +
            ", percentEmissionByRebate=" + getPercentEmissionByRebate() +
            "}";
    }
}
