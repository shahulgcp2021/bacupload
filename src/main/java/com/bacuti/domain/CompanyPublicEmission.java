package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A CompanyPublicEmission.
 */
@Entity
@Table(name = "company_public_emission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyPublicEmission extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_public_emission_seq")
    @SequenceGenerator(name = "company_public_emission_seq", sequenceName = "company_public_emission_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "reporting_company")
    private String reportingCompany;

    @Column(name = "reporting_year")
    private Integer reportingYear;

    @Column(name = "revenue", precision = 21, scale = 2)
    private BigDecimal revenue;

    @Column(name = "total_energy_in_mwh", precision = 21, scale = 2)
    private BigDecimal totalEnergyInMwh;

    @Column(name = "permanent_employees")
    private Integer permanentEmployees;

    @Column(name = "scope_1", precision = 21, scale = 2)
    private BigDecimal scope1;

    @Column(name = "scope_2_location", precision = 21, scale = 2)
    private BigDecimal scope2Location;

    @Column(name = "scope_2_market", precision = 21, scale = 2)
    private BigDecimal scope2Market;

    @Column(name = "scope_3", precision = 21, scale = 2)
    private BigDecimal scope3;

    @Column(name = "category_1", precision = 21, scale = 2)
    private BigDecimal category1;

    @Column(name = "category_2", precision = 21, scale = 2)
    private BigDecimal category2;

    @Column(name = "category_3", precision = 21, scale = 2)
    private BigDecimal category3;

    @Column(name = "category_4", precision = 21, scale = 2)
    private BigDecimal category4;

    @Column(name = "category_5", precision = 21, scale = 2)
    private BigDecimal category5;

    @Column(name = "category_6", precision = 21, scale = 2)
    private BigDecimal category6;

    @Column(name = "category_7", precision = 21, scale = 2)
    private BigDecimal category7;

    @Column(name = "category_8", precision = 21, scale = 2)
    private BigDecimal category8;

    @Column(name = "category_9", precision = 21, scale = 2)
    private BigDecimal category9;

    @Column(name = "category_10", precision = 21, scale = 2)
    private BigDecimal category10;

    @Column(name = "category_11", precision = 21, scale = 2)
    private BigDecimal category11;

    @Column(name = "category_12", precision = 21, scale = 2)
    private BigDecimal category12;

    @Column(name = "category_13", precision = 21, scale = 2)
    private BigDecimal category13;

    @Column(name = "category_14", precision = 21, scale = 2)
    private BigDecimal category14;

    @Column(name = "category_15", precision = 21, scale = 2)
    private BigDecimal category15;

    @Column(name = "intensity_scope_1", precision = 21, scale = 2)
    private BigDecimal intensityScope1;

    @Column(name = "intensity_scope_1_loction", precision = 21, scale = 2)
    private BigDecimal intensityScope1Loction;

    @Column(name = "intensityscope_2_market", precision = 21, scale = 2)
    private BigDecimal intensityscope2Market;

    @Column(name = "intensity_scope_3", precision = 21, scale = 2)
    private BigDecimal intensityScope3;

    @Column(name = "intensity_scope_12", precision = 21, scale = 2)
    private BigDecimal intensityScope12;

    @Column(name = "intensity_scope_123", precision = 21, scale = 2)
    private BigDecimal intensityScope123;

    @Column(name = "activity_level", precision = 21, scale = 2)
    private BigDecimal activityLevel;

    @Column(name = "data_source_type")
    private String dataSourceType;

    @Column(name = "disclosure_type")
    private String disclosureType;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "industry_codes")
    private String industryCodes;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "company_website")
    private String companyWebsite;

    @Column(name = "company_activities")
    private String companyActivities;

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
    private UnitOfMeasure revenueUnitOfMeasure;

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
    private UnitOfMeasure emissionsUnitOfMeasure;

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
    private UnitOfMeasure emissionIntensityUnitOfMeasure;

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
    private UnitOfMeasure activitylevelUnitOfMeasure;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyPublicEmission")
    @JsonIgnoreProperties(
        value = { "company", "site", "companyPublicEmission", "defaultAverageEF", "aggregateEnergyUsages" },
        allowSetters = true
    )
    private Set<EnergySource> energySources = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companyPublicEmission")
    @JsonIgnoreProperties(value = { "company", "item", "supplier", "companyPublicEmission", "purchasedQties" }, allowSetters = true)
    private Set<ItemSupplier> itemSuppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompanyPublicEmission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportingCompany() {
        return this.reportingCompany;
    }

    public CompanyPublicEmission reportingCompany(String reportingCompany) {
        this.setReportingCompany(reportingCompany);
        return this;
    }

    public void setReportingCompany(String reportingCompany) {
        this.reportingCompany = reportingCompany;
    }

    public Integer getReportingYear() {
        return this.reportingYear;
    }

    public CompanyPublicEmission reportingYear(Integer reportingYear) {
        this.setReportingYear(reportingYear);
        return this;
    }

    public void setReportingYear(Integer reportingYear) {
        this.reportingYear = reportingYear;
    }

    public BigDecimal getRevenue() {
        return this.revenue;
    }

    public CompanyPublicEmission revenue(BigDecimal revenue) {
        this.setRevenue(revenue);
        return this;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getTotalEnergyInMwh() {
        return this.totalEnergyInMwh;
    }

    public CompanyPublicEmission totalEnergyInMwh(BigDecimal totalEnergyInMwh) {
        this.setTotalEnergyInMwh(totalEnergyInMwh);
        return this;
    }

    public void setTotalEnergyInMwh(BigDecimal totalEnergyInMwh) {
        this.totalEnergyInMwh = totalEnergyInMwh;
    }

    public Integer getPermanentEmployees() {
        return this.permanentEmployees;
    }

    public CompanyPublicEmission permanentEmployees(Integer permanentEmployees) {
        this.setPermanentEmployees(permanentEmployees);
        return this;
    }

    public void setPermanentEmployees(Integer permanentEmployees) {
        this.permanentEmployees = permanentEmployees;
    }

    public BigDecimal getScope1() {
        return this.scope1;
    }

    public CompanyPublicEmission scope1(BigDecimal scope1) {
        this.setScope1(scope1);
        return this;
    }

    public void setScope1(BigDecimal scope1) {
        this.scope1 = scope1;
    }

    public BigDecimal getScope2Location() {
        return this.scope2Location;
    }

    public CompanyPublicEmission scope2Location(BigDecimal scope2Location) {
        this.setScope2Location(scope2Location);
        return this;
    }

    public void setScope2Location(BigDecimal scope2Location) {
        this.scope2Location = scope2Location;
    }

    public BigDecimal getScope2Market() {
        return this.scope2Market;
    }

    public CompanyPublicEmission scope2Market(BigDecimal scope2Market) {
        this.setScope2Market(scope2Market);
        return this;
    }

    public void setScope2Market(BigDecimal scope2Market) {
        this.scope2Market = scope2Market;
    }

    public BigDecimal getScope3() {
        return this.scope3;
    }

    public CompanyPublicEmission scope3(BigDecimal scope3) {
        this.setScope3(scope3);
        return this;
    }

    public void setScope3(BigDecimal scope3) {
        this.scope3 = scope3;
    }

    public BigDecimal getCategory1() {
        return this.category1;
    }

    public CompanyPublicEmission category1(BigDecimal category1) {
        this.setCategory1(category1);
        return this;
    }

    public void setCategory1(BigDecimal category1) {
        this.category1 = category1;
    }

    public BigDecimal getCategory2() {
        return this.category2;
    }

    public CompanyPublicEmission category2(BigDecimal category2) {
        this.setCategory2(category2);
        return this;
    }

    public void setCategory2(BigDecimal category2) {
        this.category2 = category2;
    }

    public BigDecimal getCategory3() {
        return this.category3;
    }

    public CompanyPublicEmission category3(BigDecimal category3) {
        this.setCategory3(category3);
        return this;
    }

    public void setCategory3(BigDecimal category3) {
        this.category3 = category3;
    }

    public BigDecimal getCategory4() {
        return this.category4;
    }

    public CompanyPublicEmission category4(BigDecimal category4) {
        this.setCategory4(category4);
        return this;
    }

    public void setCategory4(BigDecimal category4) {
        this.category4 = category4;
    }

    public BigDecimal getCategory5() {
        return this.category5;
    }

    public CompanyPublicEmission category5(BigDecimal category5) {
        this.setCategory5(category5);
        return this;
    }

    public void setCategory5(BigDecimal category5) {
        this.category5 = category5;
    }

    public BigDecimal getCategory6() {
        return this.category6;
    }

    public CompanyPublicEmission category6(BigDecimal category6) {
        this.setCategory6(category6);
        return this;
    }

    public void setCategory6(BigDecimal category6) {
        this.category6 = category6;
    }

    public BigDecimal getCategory7() {
        return this.category7;
    }

    public CompanyPublicEmission category7(BigDecimal category7) {
        this.setCategory7(category7);
        return this;
    }

    public void setCategory7(BigDecimal category7) {
        this.category7 = category7;
    }

    public BigDecimal getCategory8() {
        return this.category8;
    }

    public CompanyPublicEmission category8(BigDecimal category8) {
        this.setCategory8(category8);
        return this;
    }

    public void setCategory8(BigDecimal category8) {
        this.category8 = category8;
    }

    public BigDecimal getCategory9() {
        return this.category9;
    }

    public CompanyPublicEmission category9(BigDecimal category9) {
        this.setCategory9(category9);
        return this;
    }

    public void setCategory9(BigDecimal category9) {
        this.category9 = category9;
    }

    public BigDecimal getCategory10() {
        return this.category10;
    }

    public CompanyPublicEmission category10(BigDecimal category10) {
        this.setCategory10(category10);
        return this;
    }

    public void setCategory10(BigDecimal category10) {
        this.category10 = category10;
    }

    public BigDecimal getCategory11() {
        return this.category11;
    }

    public CompanyPublicEmission category11(BigDecimal category11) {
        this.setCategory11(category11);
        return this;
    }

    public void setCategory11(BigDecimal category11) {
        this.category11 = category11;
    }

    public BigDecimal getCategory12() {
        return this.category12;
    }

    public CompanyPublicEmission category12(BigDecimal category12) {
        this.setCategory12(category12);
        return this;
    }

    public void setCategory12(BigDecimal category12) {
        this.category12 = category12;
    }

    public BigDecimal getCategory13() {
        return this.category13;
    }

    public CompanyPublicEmission category13(BigDecimal category13) {
        this.setCategory13(category13);
        return this;
    }

    public void setCategory13(BigDecimal category13) {
        this.category13 = category13;
    }

    public BigDecimal getCategory14() {
        return this.category14;
    }

    public CompanyPublicEmission category14(BigDecimal category14) {
        this.setCategory14(category14);
        return this;
    }

    public void setCategory14(BigDecimal category14) {
        this.category14 = category14;
    }

    public BigDecimal getCategory15() {
        return this.category15;
    }

    public CompanyPublicEmission category15(BigDecimal category15) {
        this.setCategory15(category15);
        return this;
    }

    public void setCategory15(BigDecimal category15) {
        this.category15 = category15;
    }

    public BigDecimal getIntensityScope1() {
        return this.intensityScope1;
    }

    public CompanyPublicEmission intensityScope1(BigDecimal intensityScope1) {
        this.setIntensityScope1(intensityScope1);
        return this;
    }

    public void setIntensityScope1(BigDecimal intensityScope1) {
        this.intensityScope1 = intensityScope1;
    }

    public BigDecimal getIntensityScope1Loction() {
        return this.intensityScope1Loction;
    }

    public CompanyPublicEmission intensityScope1Loction(BigDecimal intensityScope1Loction) {
        this.setIntensityScope1Loction(intensityScope1Loction);
        return this;
    }

    public void setIntensityScope1Loction(BigDecimal intensityScope1Loction) {
        this.intensityScope1Loction = intensityScope1Loction;
    }

    public BigDecimal getIntensityscope2Market() {
        return this.intensityscope2Market;
    }

    public CompanyPublicEmission intensityscope2Market(BigDecimal intensityscope2Market) {
        this.setIntensityscope2Market(intensityscope2Market);
        return this;
    }

    public void setIntensityscope2Market(BigDecimal intensityscope2Market) {
        this.intensityscope2Market = intensityscope2Market;
    }

    public BigDecimal getIntensityScope3() {
        return this.intensityScope3;
    }

    public CompanyPublicEmission intensityScope3(BigDecimal intensityScope3) {
        this.setIntensityScope3(intensityScope3);
        return this;
    }

    public void setIntensityScope3(BigDecimal intensityScope3) {
        this.intensityScope3 = intensityScope3;
    }

    public BigDecimal getIntensityScope12() {
        return this.intensityScope12;
    }

    public CompanyPublicEmission intensityScope12(BigDecimal intensityScope12) {
        this.setIntensityScope12(intensityScope12);
        return this;
    }

    public void setIntensityScope12(BigDecimal intensityScope12) {
        this.intensityScope12 = intensityScope12;
    }

    public BigDecimal getIntensityScope123() {
        return this.intensityScope123;
    }

    public CompanyPublicEmission intensityScope123(BigDecimal intensityScope123) {
        this.setIntensityScope123(intensityScope123);
        return this;
    }

    public void setIntensityScope123(BigDecimal intensityScope123) {
        this.intensityScope123 = intensityScope123;
    }

    public BigDecimal getActivityLevel() {
        return this.activityLevel;
    }

    public CompanyPublicEmission activityLevel(BigDecimal activityLevel) {
        this.setActivityLevel(activityLevel);
        return this;
    }

    public void setActivityLevel(BigDecimal activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getDataSourceType() {
        return this.dataSourceType;
    }

    public CompanyPublicEmission dataSourceType(String dataSourceType) {
        this.setDataSourceType(dataSourceType);
        return this;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDisclosureType() {
        return this.disclosureType;
    }

    public CompanyPublicEmission disclosureType(String disclosureType) {
        this.setDisclosureType(disclosureType);
        return this;
    }

    public void setDisclosureType(String disclosureType) {
        this.disclosureType = disclosureType;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public CompanyPublicEmission dataSource(String dataSource) {
        this.setDataSource(dataSource);
        return this;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getIndustryCodes() {
        return this.industryCodes;
    }

    public CompanyPublicEmission industryCodes(String industryCodes) {
        this.setIndustryCodes(industryCodes);
        return this;
    }

    public void setIndustryCodes(String industryCodes) {
        this.industryCodes = industryCodes;
    }

    public String getCodeType() {
        return this.codeType;
    }

    public CompanyPublicEmission codeType(String codeType) {
        this.setCodeType(codeType);
        return this;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCompanyWebsite() {
        return this.companyWebsite;
    }

    public CompanyPublicEmission companyWebsite(String companyWebsite) {
        this.setCompanyWebsite(companyWebsite);
        return this;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyActivities() {
        return this.companyActivities;
    }

    public CompanyPublicEmission companyActivities(String companyActivities) {
        this.setCompanyActivities(companyActivities);
        return this;
    }

    public void setCompanyActivities(String companyActivities) {
        this.companyActivities = companyActivities;
    }

    public UnitOfMeasure getRevenueUnitOfMeasure() {
        return this.revenueUnitOfMeasure;
    }

    public void setRevenueUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.revenueUnitOfMeasure = unitOfMeasure;
    }

    public CompanyPublicEmission revenueUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setRevenueUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getEmissionsUnitOfMeasure() {
        return this.emissionsUnitOfMeasure;
    }

    public void setEmissionsUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.emissionsUnitOfMeasure = unitOfMeasure;
    }

    public CompanyPublicEmission emissionsUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setEmissionsUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getEmissionIntensityUnitOfMeasure() {
        return this.emissionIntensityUnitOfMeasure;
    }

    public void setEmissionIntensityUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.emissionIntensityUnitOfMeasure = unitOfMeasure;
    }

    public CompanyPublicEmission emissionIntensityUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setEmissionIntensityUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getActivitylevelUnitOfMeasure() {
        return this.activitylevelUnitOfMeasure;
    }

    public void setActivitylevelUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.activitylevelUnitOfMeasure = unitOfMeasure;
    }

    public CompanyPublicEmission activitylevelUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.setActivitylevelUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public Set<EnergySource> getEnergySources() {
        return this.energySources;
    }

    public void setEnergySources(Set<EnergySource> energySources) {
        if (this.energySources != null) {
            this.energySources.forEach(i -> i.setCompanyPublicEmission(null));
        }
        if (energySources != null) {
            energySources.forEach(i -> i.setCompanyPublicEmission(this));
        }
        this.energySources = energySources;
    }

    public CompanyPublicEmission energySources(Set<EnergySource> energySources) {
        this.setEnergySources(energySources);
        return this;
    }

    public CompanyPublicEmission addEnergySource(EnergySource energySource) {
        this.energySources.add(energySource);
        energySource.setCompanyPublicEmission(this);
        return this;
    }

    public CompanyPublicEmission removeEnergySource(EnergySource energySource) {
        this.energySources.remove(energySource);
        energySource.setCompanyPublicEmission(null);
        return this;
    }

    public Set<ItemSupplier> getItemSuppliers() {
        return this.itemSuppliers;
    }

    public void setItemSuppliers(Set<ItemSupplier> itemSuppliers) {
        if (this.itemSuppliers != null) {
            this.itemSuppliers.forEach(i -> i.setCompanyPublicEmission(null));
        }
        if (itemSuppliers != null) {
            itemSuppliers.forEach(i -> i.setCompanyPublicEmission(this));
        }
        this.itemSuppliers = itemSuppliers;
    }

    public CompanyPublicEmission itemSuppliers(Set<ItemSupplier> itemSuppliers) {
        this.setItemSuppliers(itemSuppliers);
        return this;
    }

    public CompanyPublicEmission addItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.add(itemSupplier);
        itemSupplier.setCompanyPublicEmission(this);
        return this;
    }

    public CompanyPublicEmission removeItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.remove(itemSupplier);
        itemSupplier.setCompanyPublicEmission(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyPublicEmission)) {
            return false;
        }
        return getId() != null && getId().equals(((CompanyPublicEmission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyPublicEmission{" +
            "id=" + getId() +
            ", reportingCompany='" + getReportingCompany() + "'" +
            ", reportingYear=" + getReportingYear() +
            ", revenue=" + getRevenue() +
            ", totalEnergyInMwh=" + getTotalEnergyInMwh() +
            ", permanentEmployees=" + getPermanentEmployees() +
            ", scope1=" + getScope1() +
            ", scope2Location=" + getScope2Location() +
            ", scope2Market=" + getScope2Market() +
            ", scope3=" + getScope3() +
            ", category1=" + getCategory1() +
            ", category2=" + getCategory2() +
            ", category3=" + getCategory3() +
            ", category4=" + getCategory4() +
            ", category5=" + getCategory5() +
            ", category6=" + getCategory6() +
            ", category7=" + getCategory7() +
            ", category8=" + getCategory8() +
            ", category9=" + getCategory9() +
            ", category10=" + getCategory10() +
            ", category11=" + getCategory11() +
            ", category12=" + getCategory12() +
            ", category13=" + getCategory13() +
            ", category14=" + getCategory14() +
            ", category15=" + getCategory15() +
            ", intensityScope1=" + getIntensityScope1() +
            ", intensityScope1Loction=" + getIntensityScope1Loction() +
            ", intensityscope2Market=" + getIntensityscope2Market() +
            ", intensityScope3=" + getIntensityScope3() +
            ", intensityScope12=" + getIntensityScope12() +
            ", intensityScope123=" + getIntensityScope123() +
            ", activityLevel=" + getActivityLevel() +
            ", dataSourceType='" + getDataSourceType() + "'" +
            ", disclosureType='" + getDisclosureType() + "'" +
            ", dataSource='" + getDataSource() + "'" +
            ", industryCodes='" + getIndustryCodes() + "'" +
            ", codeType='" + getCodeType() + "'" +
            ", companyWebsite='" + getCompanyWebsite() + "'" +
            ", companyActivities='" + getCompanyActivities() + "'" +
            "}";
    }
}
