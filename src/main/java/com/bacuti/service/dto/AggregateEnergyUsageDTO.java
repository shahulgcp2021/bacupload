package com.bacuti.service.dto;

import com.bacuti.domain.EnergySource;
import com.bacuti.domain.Site;
import com.bacuti.domain.UnitOfMeasure;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class AggregateEnergyUsageDTO {

    private Long id;

    private Instant date;

    private BigDecimal usage;

    private Site site;

    private EnergySource energySource;

    private UnitOfMeasure unitOfMeasure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getUsage() {
        return usage;
    }

    public void setUsage(BigDecimal usage) {
        this.usage = usage;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public EnergySource getEnergySource() {
        return energySource;
    }

    public void setEnergySource(EnergySource energySource) {
        this.energySource = energySource;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateEnergyUsageDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(usage, that.usage) && Objects.equals(site, that.site) && Objects.equals(energySource, that.energySource) && Objects.equals(unitOfMeasure, that.unitOfMeasure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, usage, site, energySource, unitOfMeasure);
    }

    @Override
    public String toString() {
        return "AggregateEnergyUsageDTO{" +
            "id=" + id +
            ", date=" + date +
            ", usage=" + usage +
            ", site=" + site +
            ", energySource=" + energySource +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
