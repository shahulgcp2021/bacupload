package com.bacuti.domain;

import com.bacuti.domain.enumeration.TravelMode;
import com.bacuti.domain.enumeration.TravelType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A EmployeeTravelAvg.
 */
@Entity
@Table(name = "employee_travel_avg")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeTravelAvg extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_travel_avg_seq")
    @SequenceGenerator(name = "employee_travel_avg_seq", sequenceName = "employee_travel_avg_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_type")
    private TravelType travelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_mode")
    private TravelMode travelMode;

    @Column(name = "period_from")
    private LocalDate periodFrom;

    @Column(name = "period_to")
    private LocalDate periodTo;

    @Column(name = "pct_employees", precision = 21, scale = 2)
    private BigDecimal pctEmployees;

    @Column(name = "commute_days_per_week", precision = 21, scale = 2)
    private BigDecimal commuteDaysPerWeek;

    @Column(name = "pct_days_travelled", precision = 21, scale = 2)
    private BigDecimal pctDaysTravelled;

    @Column(name = "avg_trips_in_period", precision = 21, scale = 2)
    private BigDecimal avgTripsInPeriod;

    @Column(name = "avg_travel_distance_in_km", precision = 21, scale = 2)
    private BigDecimal avgTravelDistanceInKm;

    @Column(name = "avg_hotel_stay_days_per_trip", precision = 21, scale = 2)
    private BigDecimal avgHotelStayDaysPerTrip;

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

    public EmployeeTravelAvg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TravelType getTravelType() {
        return this.travelType;
    }

    public EmployeeTravelAvg travelType(TravelType travelType) {
        this.setTravelType(travelType);
        return this;
    }

    public void setTravelType(TravelType travelType) {
        this.travelType = travelType;
    }

    public TravelMode getTravelMode() {
        return this.travelMode;
    }

    public EmployeeTravelAvg travelMode(TravelMode travelMode) {
        this.setTravelMode(travelMode);
        return this;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public LocalDate getPeriodFrom() {
        return this.periodFrom;
    }

    public EmployeeTravelAvg periodFrom(LocalDate periodFrom) {
        this.setPeriodFrom(periodFrom);
        return this;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return this.periodTo;
    }

    public EmployeeTravelAvg periodTo(LocalDate periodTo) {
        this.setPeriodTo(periodTo);
        return this;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }

    public BigDecimal getPctEmployees() {
        return this.pctEmployees;
    }

    public EmployeeTravelAvg pctEmployees(BigDecimal pctEmployees) {
        this.setPctEmployees(pctEmployees);
        return this;
    }

    public void setPctEmployees(BigDecimal pctEmployees) {
        this.pctEmployees = pctEmployees;
    }

    public BigDecimal getCommuteDaysPerWeek() {
        return this.commuteDaysPerWeek;
    }

    public EmployeeTravelAvg commuteDaysPerWeek(BigDecimal commuteDaysPerWeek) {
        this.setCommuteDaysPerWeek(commuteDaysPerWeek);
        return this;
    }

    public void setCommuteDaysPerWeek(BigDecimal commuteDaysPerWeek) {
        this.commuteDaysPerWeek = commuteDaysPerWeek;
    }

    public BigDecimal getPctDaysTravelled() {
        return this.pctDaysTravelled;
    }

    public EmployeeTravelAvg pctDaysTravelled(BigDecimal pctDaysTravelled) {
        this.setPctDaysTravelled(pctDaysTravelled);
        return this;
    }

    public void setPctDaysTravelled(BigDecimal pctDaysTravelled) {
        this.pctDaysTravelled = pctDaysTravelled;
    }

    public BigDecimal getAvgTripsInPeriod() {
        return this.avgTripsInPeriod;
    }

    public EmployeeTravelAvg avgTripsInPeriod(BigDecimal avgTripsInPeriod) {
        this.setAvgTripsInPeriod(avgTripsInPeriod);
        return this;
    }

    public void setAvgTripsInPeriod(BigDecimal avgTripsInPeriod) {
        this.avgTripsInPeriod = avgTripsInPeriod;
    }

    public BigDecimal getAvgTravelDistanceInKm() {
        return this.avgTravelDistanceInKm;
    }

    public EmployeeTravelAvg avgTravelDistanceInKm(BigDecimal avgTravelDistanceInKm) {
        this.setAvgTravelDistanceInKm(avgTravelDistanceInKm);
        return this;
    }

    public void setAvgTravelDistanceInKm(BigDecimal avgTravelDistanceInKm) {
        this.avgTravelDistanceInKm = avgTravelDistanceInKm;
    }

    public BigDecimal getAvgHotelStayDaysPerTrip() {
        return this.avgHotelStayDaysPerTrip;
    }

    public EmployeeTravelAvg avgHotelStayDaysPerTrip(BigDecimal avgHotelStayDaysPerTrip) {
        this.setAvgHotelStayDaysPerTrip(avgHotelStayDaysPerTrip);
        return this;
    }

    public void setAvgHotelStayDaysPerTrip(BigDecimal avgHotelStayDaysPerTrip) {
        this.avgHotelStayDaysPerTrip = avgHotelStayDaysPerTrip;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public EmployeeTravelAvg company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public EmployeeTravelAvg site(Site site) {
        this.setSite(site);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeTravelAvg)) {
            return false;
        }
        return getId() != null && getId().equals(((EmployeeTravelAvg) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeTravelAvg{" +
            "id=" + getId() +
            ", travelType='" + getTravelType() + "'" +
            ", travelMode='" + getTravelMode() + "'" +
            ", periodFrom='" + getPeriodFrom() + "'" +
            ", periodTo='" + getPeriodTo() + "'" +
            ", pctEmployees=" + getPctEmployees() +
            ", commuteDaysPerWeek=" + getCommuteDaysPerWeek() +
            ", pctDaysTravelled=" + getPctDaysTravelled() +
            ", avgTripsInPeriod=" + getAvgTripsInPeriod() +
            ", avgTravelDistanceInKm=" + getAvgTravelDistanceInKm() +
            ", avgHotelStayDaysPerTrip=" + getAvgHotelStayDaysPerTrip() +
            "}";
    }
}
