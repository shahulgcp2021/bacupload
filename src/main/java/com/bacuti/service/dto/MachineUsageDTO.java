package com.bacuti.service.dto;

import com.bacuti.domain.Site;
import com.bacuti.domain.UnitOfMeasure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class MachineUsageDTO {

    private Long id;

    private LocalDate date;

    private Site site;

    private BigDecimal usage;

    private UnitOfMeasure unitOfMeasure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public BigDecimal getUsage() {
        return usage;
    }

    public void setUsage(BigDecimal usage) {
        this.usage = usage;
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
        if (!(o instanceof MachineUsageDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(site, that.site) && Objects.equals(usage, that.usage) && Objects.equals(unitOfMeasure, that.unitOfMeasure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, site, date, usage, unitOfMeasure);
    }

    @Override
    public String toString() {
        return "MachineUsageDTO{" +
            "id=" + id +
            ", date=" + date +
            ", site=" + site +
            ", usage=" + usage +
            ", unitOfMeasure=" + unitOfMeasure +
            '}';
    }
}
