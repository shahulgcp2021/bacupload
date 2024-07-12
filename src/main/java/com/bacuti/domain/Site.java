package com.bacuti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Site.
 */
@Entity
@Table(name = "site")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Site extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "site_seq")
    @SequenceGenerator(name = "site_seq", sequenceName = "site_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "site_name", unique = true)
    private String siteName;

    @Column(name = "description")
    private String description;

    @Column(name = "manufacturing_site")
    private Boolean manufacturingSite;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "cbam_impacted")
    private Boolean cbamImpacted;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "address")
    private String address;

    @Column(name = "lattitude", precision = 21, scale = 2)
    private BigDecimal lattitude;

    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    @Column(name = "unlocode")
    private String unlocode;

    @Column(name = "data_quality_desc")
    private String dataQualityDesc;

    @Column(name = "default_value_usage_justfn")
    private String defaultValueUsageJustfn;

    @Column(name = "data_qa_info")
    private String dataQAInfo;

    @Column(name = "default_heat_number")
    private String defaultHeatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site", "energySource", "unitOfMeasure" }, allowSetters = true)
    private Set<AggregateEnergyUsage> aggregateEnergyUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site", "emissionDb" }, allowSetters = true)
    private Set<CapitalGood> capitalGoods = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site" }, allowSetters = true)
    private Set<CarbonPricePayment> carbonPricePayments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site" }, allowSetters = true)
    private Set<EmployeeTravelAvg> employeeTravelAvgs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(
        value = { "company", "site", "companyPublicEmission", "defaultAverageEF", "aggregateEnergyUsages" },
        allowSetters = true
    )
    private Set<EnergySource> energySources = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "item", "shipmentLane", "site" }, allowSetters = true)
    private Set<ItemShipment> itemShipments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site", "unitOfMeasure" }, allowSetters = true)
    private Set<MachineUsage> machineUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site", "product" }, allowSetters = true)
    private Set<ProductionQty> productionQties = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @JsonIgnoreProperties(value = { "company", "site", "finishedGood" }, allowSetters = true)
    private Set<SiteFinishedGood> siteFinishedGoods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Site id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public Site siteName(String siteName) {
        this.setSiteName(siteName);
        return this;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDescription() {
        return this.description;
    }

    public Site description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getManufacturingSite() {
        return this.manufacturingSite;
    }

    public Site manufacturingSite(Boolean manufacturingSite) {
        this.setManufacturingSite(manufacturingSite);
        return this;
    }

    public void setManufacturingSite(Boolean manufacturingSite) {
        this.manufacturingSite = manufacturingSite;
    }

    public Integer getEmployeeCount() {
        return this.employeeCount;
    }

    public Site employeeCount(Integer employeeCount) {
        this.setEmployeeCount(employeeCount);
        return this;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Boolean getCbamImpacted() {
        return this.cbamImpacted;
    }

    public Site cbamImpacted(Boolean cbamImpacted) {
        this.setCbamImpacted(cbamImpacted);
        return this;
    }

    public void setCbamImpacted(Boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public String getCountry() {
        return this.country;
    }

    public Site country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public Site state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return this.address;
    }

    public Site address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLattitude() {
        return this.lattitude;
    }

    public Site lattitude(BigDecimal lattitude) {
        this.setLattitude(lattitude);
        return this;
    }

    public void setLattitude(BigDecimal lattitude) {
        this.lattitude = lattitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Site longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getUnlocode() {
        return this.unlocode;
    }

    public Site unlocode(String unlocode) {
        this.setUnlocode(unlocode);
        return this;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getDataQualityDesc() {
        return this.dataQualityDesc;
    }

    public Site dataQualityDesc(String dataQualityDesc) {
        this.setDataQualityDesc(dataQualityDesc);
        return this;
    }

    public void setDataQualityDesc(String dataQualityDesc) {
        this.dataQualityDesc = dataQualityDesc;
    }

    public String getDefaultValueUsageJustfn() {
        return this.defaultValueUsageJustfn;
    }

    public Site defaultValueUsageJustfn(String defaultValueUsageJustfn) {
        this.setDefaultValueUsageJustfn(defaultValueUsageJustfn);
        return this;
    }

    public void setDefaultValueUsageJustfn(String defaultValueUsageJustfn) {
        this.defaultValueUsageJustfn = defaultValueUsageJustfn;
    }

    public String getDataQAInfo() {
        return this.dataQAInfo;
    }

    public Site dataQAInfo(String dataQAInfo) {
        this.setDataQAInfo(dataQAInfo);
        return this;
    }

    public void setDataQAInfo(String dataQAInfo) {
        this.dataQAInfo = dataQAInfo;
    }

    public String getDefaultHeatNumber() {
        return this.defaultHeatNumber;
    }

    public Site defaultHeatNumber(String defaultHeatNumber) {
        this.setDefaultHeatNumber(defaultHeatNumber);
        return this;
    }

    public void setDefaultHeatNumber(String defaultHeatNumber) {
        this.defaultHeatNumber = defaultHeatNumber;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Site company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Set<AggregateEnergyUsage> getAggregateEnergyUsages() {
        return this.aggregateEnergyUsages;
    }

    public void setAggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        if (this.aggregateEnergyUsages != null) {
            this.aggregateEnergyUsages.forEach(i -> i.setSite(null));
        }
        if (aggregateEnergyUsages != null) {
            aggregateEnergyUsages.forEach(i -> i.setSite(this));
        }
        this.aggregateEnergyUsages = aggregateEnergyUsages;
    }

    public Site aggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        this.setAggregateEnergyUsages(aggregateEnergyUsages);
        return this;
    }

    public Site addAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.add(aggregateEnergyUsage);
        aggregateEnergyUsage.setSite(this);
        return this;
    }

    public Site removeAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.remove(aggregateEnergyUsage);
        aggregateEnergyUsage.setSite(null);
        return this;
    }

    public Set<CapitalGood> getCapitalGoods() {
        return this.capitalGoods;
    }

    public void setCapitalGoods(Set<CapitalGood> capitalGoods) {
        if (this.capitalGoods != null) {
            this.capitalGoods.forEach(i -> i.setSite(null));
        }
        if (capitalGoods != null) {
            capitalGoods.forEach(i -> i.setSite(this));
        }
        this.capitalGoods = capitalGoods;
    }

    public Site capitalGoods(Set<CapitalGood> capitalGoods) {
        this.setCapitalGoods(capitalGoods);
        return this;
    }

    public Site addCapitalGood(CapitalGood capitalGood) {
        this.capitalGoods.add(capitalGood);
        capitalGood.setSite(this);
        return this;
    }

    public Site removeCapitalGood(CapitalGood capitalGood) {
        this.capitalGoods.remove(capitalGood);
        capitalGood.setSite(null);
        return this;
    }

    public Set<CarbonPricePayment> getCarbonPricePayments() {
        return this.carbonPricePayments;
    }

    public void setCarbonPricePayments(Set<CarbonPricePayment> carbonPricePayments) {
        if (this.carbonPricePayments != null) {
            this.carbonPricePayments.forEach(i -> i.setSite(null));
        }
        if (carbonPricePayments != null) {
            carbonPricePayments.forEach(i -> i.setSite(this));
        }
        this.carbonPricePayments = carbonPricePayments;
    }

    public Site carbonPricePayments(Set<CarbonPricePayment> carbonPricePayments) {
        this.setCarbonPricePayments(carbonPricePayments);
        return this;
    }

    public Site addCarbonPricePayment(CarbonPricePayment carbonPricePayment) {
        this.carbonPricePayments.add(carbonPricePayment);
        carbonPricePayment.setSite(this);
        return this;
    }

    public Site removeCarbonPricePayment(CarbonPricePayment carbonPricePayment) {
        this.carbonPricePayments.remove(carbonPricePayment);
        carbonPricePayment.setSite(null);
        return this;
    }

    public Set<EmployeeTravelAvg> getEmployeeTravelAvgs() {
        return this.employeeTravelAvgs;
    }

    public void setEmployeeTravelAvgs(Set<EmployeeTravelAvg> employeeTravelAvgs) {
        if (this.employeeTravelAvgs != null) {
            this.employeeTravelAvgs.forEach(i -> i.setSite(null));
        }
        if (employeeTravelAvgs != null) {
            employeeTravelAvgs.forEach(i -> i.setSite(this));
        }
        this.employeeTravelAvgs = employeeTravelAvgs;
    }

    public Site employeeTravelAvgs(Set<EmployeeTravelAvg> employeeTravelAvgs) {
        this.setEmployeeTravelAvgs(employeeTravelAvgs);
        return this;
    }

    public Site addEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        this.employeeTravelAvgs.add(employeeTravelAvg);
        employeeTravelAvg.setSite(this);
        return this;
    }

    public Site removeEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        this.employeeTravelAvgs.remove(employeeTravelAvg);
        employeeTravelAvg.setSite(null);
        return this;
    }

    public Set<EnergySource> getEnergySources() {
        return this.energySources;
    }

    public void setEnergySources(Set<EnergySource> energySources) {
        if (this.energySources != null) {
            this.energySources.forEach(i -> i.setSite(null));
        }
        if (energySources != null) {
            energySources.forEach(i -> i.setSite(this));
        }
        this.energySources = energySources;
    }

    public Site energySources(Set<EnergySource> energySources) {
        this.setEnergySources(energySources);
        return this;
    }

    public Site addEnergySource(EnergySource energySource) {
        this.energySources.add(energySource);
        energySource.setSite(this);
        return this;
    }

    public Site removeEnergySource(EnergySource energySource) {
        this.energySources.remove(energySource);
        energySource.setSite(null);
        return this;
    }

    public Set<ItemShipment> getItemShipments() {
        return this.itemShipments;
    }

    public void setItemShipments(Set<ItemShipment> itemShipments) {
        if (this.itemShipments != null) {
            this.itemShipments.forEach(i -> i.setSite(null));
        }
        if (itemShipments != null) {
            itemShipments.forEach(i -> i.setSite(this));
        }
        this.itemShipments = itemShipments;
    }

    public Site itemShipments(Set<ItemShipment> itemShipments) {
        this.setItemShipments(itemShipments);
        return this;
    }

    public Site addItemShipment(ItemShipment itemShipment) {
        this.itemShipments.add(itemShipment);
        itemShipment.setSite(this);
        return this;
    }

    public Site removeItemShipment(ItemShipment itemShipment) {
        this.itemShipments.remove(itemShipment);
        itemShipment.setSite(null);
        return this;
    }

    public Set<MachineUsage> getMachineUsages() {
        return this.machineUsages;
    }

    public void setMachineUsages(Set<MachineUsage> machineUsages) {
        if (this.machineUsages != null) {
            this.machineUsages.forEach(i -> i.setSite(null));
        }
        if (machineUsages != null) {
            machineUsages.forEach(i -> i.setSite(this));
        }
        this.machineUsages = machineUsages;
    }

    public Site machineUsages(Set<MachineUsage> machineUsages) {
        this.setMachineUsages(machineUsages);
        return this;
    }

    public Site addMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.add(machineUsage);
        machineUsage.setSite(this);
        return this;
    }

    public Site removeMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.remove(machineUsage);
        machineUsage.setSite(null);
        return this;
    }

    public Set<ProductionQty> getProductionQties() {
        return this.productionQties;
    }

    public void setProductionQties(Set<ProductionQty> productionQties) {
        if (this.productionQties != null) {
            this.productionQties.forEach(i -> i.setSite(null));
        }
        if (productionQties != null) {
            productionQties.forEach(i -> i.setSite(this));
        }
        this.productionQties = productionQties;
    }

    public Site productionQties(Set<ProductionQty> productionQties) {
        this.setProductionQties(productionQties);
        return this;
    }

    public Site addProductionQty(ProductionQty productionQty) {
        this.productionQties.add(productionQty);
        productionQty.setSite(this);
        return this;
    }

    public Site removeProductionQty(ProductionQty productionQty) {
        this.productionQties.remove(productionQty);
        productionQty.setSite(null);
        return this;
    }

    public Set<SiteFinishedGood> getSiteFinishedGoods() {
        return this.siteFinishedGoods;
    }

    public void setSiteFinishedGoods(Set<SiteFinishedGood> siteFinishedGoods) {
        if (this.siteFinishedGoods != null) {
            this.siteFinishedGoods.forEach(i -> i.setSite(null));
        }
        if (siteFinishedGoods != null) {
            siteFinishedGoods.forEach(i -> i.setSite(this));
        }
        this.siteFinishedGoods = siteFinishedGoods;
    }

    public Site siteFinishedGoods(Set<SiteFinishedGood> siteFinishedGoods) {
        this.setSiteFinishedGoods(siteFinishedGoods);
        return this;
    }

    public Site addSiteFinishedGood(SiteFinishedGood siteFinishedGood) {
        this.siteFinishedGoods.add(siteFinishedGood);
        siteFinishedGood.setSite(this);
        return this;
    }

    public Site removeSiteFinishedGood(SiteFinishedGood siteFinishedGood) {
        this.siteFinishedGoods.remove(siteFinishedGood);
        siteFinishedGood.setSite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return getId() != null && getId().equals(((Site) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", siteName='" + getSiteName() + "'" +
            ", description='" + getDescription() + "'" +
            ", manufacturingSite='" + getManufacturingSite() + "'" +
            ", employeeCount=" + getEmployeeCount() +
            ", cbamImpacted='" + getCbamImpacted() + "'" +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", address='" + getAddress() + "'" +
            ", lattitude=" + getLattitude() +
            ", longitude=" + getLongitude() +
            ", unlocode='" + getUnlocode() + "'" +
            ", dataQualityDesc='" + getDataQualityDesc() + "'" +
            ", defaultValueUsageJustfn='" + getDefaultValueUsageJustfn() + "'" +
            ", dataQAInfo='" + getDataQAInfo() + "'" +
            ", defaultHeatNumber='" + getDefaultHeatNumber() + "'" +
            "}";
    }
}
