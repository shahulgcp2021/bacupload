package com.bacuti.service.dto;
import com.bacuti.domain.enumeration.TravelMode;
import com.bacuti.domain.enumeration.TravelType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeTravelAvgDTO implements Serializable {

    private Long id;

    private TravelType travelType;

    @NotNull
    private TravelMode travelMode;


    private LocalDate periodFrom;


    private LocalDate periodTo;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal pctEmployees;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal commuteDaysPerWeek;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal pctDaysTravelled;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal avgTripsInPeriod;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal avgTravelDistanceInKm;


    @Digits(integer = 19, fraction = 2)
    private BigDecimal avgHotelStayDaysPerTrip;

    private CompanyDTO company;

    private SiteDTO site;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TravelType getTravelType() {
        return travelType;
    }

    public void setTravelType(TravelType travelType) {
        this.travelType = travelType;
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public LocalDate getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }

    public BigDecimal getPctEmployees() {
        return pctEmployees;
    }

    public void setPctEmployees(BigDecimal pctEmployees) {
        this.pctEmployees = pctEmployees;
    }

    public BigDecimal getCommuteDaysPerWeek() {
        return commuteDaysPerWeek;
    }

    public void setCommuteDaysPerWeek(BigDecimal commuteDaysPerWeek) {
        this.commuteDaysPerWeek = commuteDaysPerWeek;
    }

    public BigDecimal getPctDaysTravelled() {
        return pctDaysTravelled;
    }

    public void setPctDaysTravelled(BigDecimal pctDaysTravelled) {
        this.pctDaysTravelled = pctDaysTravelled;
    }

    public BigDecimal getAvgTripsInPeriod() {
        return avgTripsInPeriod;
    }

    public void setAvgTripsInPeriod(BigDecimal avgTripsInPeriod) {
        this.avgTripsInPeriod = avgTripsInPeriod;
    }

    public BigDecimal getAvgTravelDistanceInKm() {
        return avgTravelDistanceInKm;
    }

    public void setAvgTravelDistanceInKm(BigDecimal avgTravelDistanceInKm) {
        this.avgTravelDistanceInKm = avgTravelDistanceInKm;
    }

    public BigDecimal getAvgHotelStayDaysPerTrip() {
        return avgHotelStayDaysPerTrip;
    }

    public void setAvgHotelStayDaysPerTrip(BigDecimal avgHotelStayDaysPerTrip) {
        this.avgHotelStayDaysPerTrip = avgHotelStayDaysPerTrip;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }
}
