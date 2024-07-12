package com.bacuti.domain;

import com.bacuti.domain.enumeration.BacutiCapsEnabled;
import com.bacuti.domain.enumeration.CompanyStatus;
import com.bacuti.domain.enumeration.IndustryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
    @SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "industry")
    private String industry;

    @Column(name = "domain")
    private String domain;

    @Column(name = "country")
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private IndustryType industryType;

    @Column(name = "admin")
    private String admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CompanyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "bacuti_caps_enabled")
    private BacutiCapsEnabled bacutiCapsEnabled;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "cbam_impacted")
    private Boolean cbamImpacted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site", "energySource", "unitOfMeasure" }, allowSetters = true)
    private Set<AggregateEnergyUsage> aggregateEnergyUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "product", "component", "unitOfMeasure" }, allowSetters = true)
    private Set<BillofMaterial> billofMaterials = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site", "emissionDb" }, allowSetters = true)
    private Set<CapitalGood> capitalGoods = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site" }, allowSetters = true)
    private Set<CarbonPricePayment> carbonPricePayments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site" }, allowSetters = true)
    private Set<EmployeeTravelAvg> employeeTravelAvgs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(
        value = { "company", "site", "companyPublicEmission", "defaultAverageEF", "aggregateEnergyUsages" },
        allowSetters = true
    )
    private Set<EnergySource> energySources = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "item", "shipmentLane", "site" }, allowSetters = true)
    private Set<ItemShipment> itemShipments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "item", "supplier", "companyPublicEmission", "purchasedQties" }, allowSetters = true)
    private Set<ItemSupplier> itemSuppliers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(
        value = { "company", "defaultAverageEF", "unitOfMeasure", "itemShipments", "itemSuppliers", "purchasedQties" },
        allowSetters = true
    )
    private Set<Item> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "routing" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site", "unitOfMeasure" }, allowSetters = true)
    private Set<MachineUsage> machineUsages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "product", "unitOfMeasure" }, allowSetters = true)
    private Set<ProductUsageDetail> productUsageDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site", "product" }, allowSetters = true)
    private Set<ProductionQty> productionQties = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "item", "itemSupplier", "unitOfMeasure" }, allowSetters = true)
    private Set<PurchasedQty> purchasedQties = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "product", "unitOfMeasure", "machines" }, allowSetters = true)
    private Set<Routing> routings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "itemShipments", "shipmentLegs" }, allowSetters = true)
    private Set<ShipmentLane> shipmentLanes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "site", "finishedGood" }, allowSetters = true)
    private Set<SiteFinishedGood> siteFinishedGoods = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
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
    private Set<Site> sites;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "itemSuppliers" }, allowSetters = true)
    private Set<Supplier> suppliers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "product", "defaultAverageEF", "unitOfMeasure" }, allowSetters = true)
    private Set<Wastedisposal> wastedisposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Company companyName(String companyName) {
        this.setName(companyName);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Company description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return this.industry;
    }

    public Company industry(String industry) {
        this.setIndustry(industry);
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCountry() {
        return this.country;
    }

    public Company country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public IndustryType getIndustryType() {
        return this.industryType;
    }

    public Company industryType(IndustryType industryType) {
        this.setIndustryType(industryType);
        return this;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public String getAdmin() {
        return this.admin;
    }

    public Company admin(String admin) {
        this.setAdmin(admin);
        return this;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public CompanyStatus getStatus() {
        return this.status;
    }

    public Company status(CompanyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public BacutiCapsEnabled getBacutiCapsEnabled() {
        return this.bacutiCapsEnabled;
    }

    public Company bacutiCapsEnabled(BacutiCapsEnabled bacutiCapsEnabled) {
        this.setBacutiCapsEnabled(bacutiCapsEnabled);
        return this;
    }

    public void setBacutiCapsEnabled(BacutiCapsEnabled bacutiCapsEnabled) {
        this.bacutiCapsEnabled = bacutiCapsEnabled;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Company imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Boolean getCbamImpacted() {
        return this.cbamImpacted;
    }

    public Company cbamImpacted(Boolean cbamImpacted) {
        this.setCbamImpacted(cbamImpacted);
        return this;
    }

    public void setCbamImpacted(Boolean cbamImpacted) {
        this.cbamImpacted = cbamImpacted;
    }

    public Set<AggregateEnergyUsage> getAggregateEnergyUsages() {
        return this.aggregateEnergyUsages;
    }

    public void setAggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        if (this.aggregateEnergyUsages != null) {
            this.aggregateEnergyUsages.forEach(i -> i.setCompany(null));
        }
        if (aggregateEnergyUsages != null) {
            aggregateEnergyUsages.forEach(i -> i.setCompany(this));
        }
        this.aggregateEnergyUsages = aggregateEnergyUsages;
    }

    public Company aggregateEnergyUsages(Set<AggregateEnergyUsage> aggregateEnergyUsages) {
        this.setAggregateEnergyUsages(aggregateEnergyUsages);
        return this;
    }

    public Company addAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.add(aggregateEnergyUsage);
        aggregateEnergyUsage.setCompany(this);
        return this;
    }

    public Company removeAggregateEnergyUsage(AggregateEnergyUsage aggregateEnergyUsage) {
        this.aggregateEnergyUsages.remove(aggregateEnergyUsage);
        aggregateEnergyUsage.setCompany(null);
        return this;
    }

    public Set<BillofMaterial> getBillofMaterials() {
        return this.billofMaterials;
    }

    public void setBillofMaterials(Set<BillofMaterial> billofMaterials) {
        if (this.billofMaterials != null) {
            this.billofMaterials.forEach(i -> i.setCompany(null));
        }
        if (billofMaterials != null) {
            billofMaterials.forEach(i -> i.setCompany(this));
        }
        this.billofMaterials = billofMaterials;
    }

    public Company billofMaterials(Set<BillofMaterial> billofMaterials) {
        this.setBillofMaterials(billofMaterials);
        return this;
    }

    public Company addBillofMaterial(BillofMaterial billofMaterial) {
        this.billofMaterials.add(billofMaterial);
        billofMaterial.setCompany(this);
        return this;
    }

    public Company removeBillofMaterial(BillofMaterial billofMaterial) {
        this.billofMaterials.remove(billofMaterial);
        billofMaterial.setCompany(null);
        return this;
    }

    public Set<CapitalGood> getCapitalGoods() {
        return this.capitalGoods;
    }

    public void setCapitalGoods(Set<CapitalGood> capitalGoods) {
        if (this.capitalGoods != null) {
            this.capitalGoods.forEach(i -> i.setCompany(null));
        }
        if (capitalGoods != null) {
            capitalGoods.forEach(i -> i.setCompany(this));
        }
        this.capitalGoods = capitalGoods;
    }

    public Company capitalGoods(Set<CapitalGood> capitalGoods) {
        this.setCapitalGoods(capitalGoods);
        return this;
    }

    public Company addCapitalGood(CapitalGood capitalGood) {
        this.capitalGoods.add(capitalGood);
        capitalGood.setCompany(this);
        return this;
    }

    public Company removeCapitalGood(CapitalGood capitalGood) {
        this.capitalGoods.remove(capitalGood);
        capitalGood.setCompany(null);
        return this;
    }

    public Set<CarbonPricePayment> getCarbonPricePayments() {
        return this.carbonPricePayments;
    }

    public void setCarbonPricePayments(Set<CarbonPricePayment> carbonPricePayments) {
        if (this.carbonPricePayments != null) {
            this.carbonPricePayments.forEach(i -> i.setCompany(null));
        }
        if (carbonPricePayments != null) {
            carbonPricePayments.forEach(i -> i.setCompany(this));
        }
        this.carbonPricePayments = carbonPricePayments;
    }

    public Company carbonPricePayments(Set<CarbonPricePayment> carbonPricePayments) {
        this.setCarbonPricePayments(carbonPricePayments);
        return this;
    }

    public Company addCarbonPricePayment(CarbonPricePayment carbonPricePayment) {
        this.carbonPricePayments.add(carbonPricePayment);
        carbonPricePayment.setCompany(this);
        return this;
    }

    public Company removeCarbonPricePayment(CarbonPricePayment carbonPricePayment) {
        this.carbonPricePayments.remove(carbonPricePayment);
        carbonPricePayment.setCompany(null);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setCompany(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setCompany(this));
        }
        this.customers = customers;
    }

    public Company customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Company addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setCompany(this);
        return this;
    }

    public Company removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setCompany(null);
        return this;
    }

    public Set<EmployeeTravelAvg> getEmployeeTravelAvgs() {
        return this.employeeTravelAvgs;
    }

    public void setEmployeeTravelAvgs(Set<EmployeeTravelAvg> employeeTravelAvgs) {
        if (this.employeeTravelAvgs != null) {
            this.employeeTravelAvgs.forEach(i -> i.setCompany(null));
        }
        if (employeeTravelAvgs != null) {
            employeeTravelAvgs.forEach(i -> i.setCompany(this));
        }
        this.employeeTravelAvgs = employeeTravelAvgs;
    }

    public Company employeeTravelAvgs(Set<EmployeeTravelAvg> employeeTravelAvgs) {
        this.setEmployeeTravelAvgs(employeeTravelAvgs);
        return this;
    }

    public Company addEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        this.employeeTravelAvgs.add(employeeTravelAvg);
        employeeTravelAvg.setCompany(this);
        return this;
    }

    public Company removeEmployeeTravelAvg(EmployeeTravelAvg employeeTravelAvg) {
        this.employeeTravelAvgs.remove(employeeTravelAvg);
        employeeTravelAvg.setCompany(null);
        return this;
    }

    public Set<EnergySource> getEnergySources() {
        return this.energySources;
    }

    public void setEnergySources(Set<EnergySource> energySources) {
        if (this.energySources != null) {
            this.energySources.forEach(i -> i.setCompany(null));
        }
        if (energySources != null) {
            energySources.forEach(i -> i.setCompany(this));
        }
        this.energySources = energySources;
    }

    public Company energySources(Set<EnergySource> energySources) {
        this.setEnergySources(energySources);
        return this;
    }

    public Company addEnergySource(EnergySource energySource) {
        this.energySources.add(energySource);
        energySource.setCompany(this);
        return this;
    }

    public Company removeEnergySource(EnergySource energySource) {
        this.energySources.remove(energySource);
        energySource.setCompany(null);
        return this;
    }

    public Set<ItemShipment> getItemShipments() {
        return this.itemShipments;
    }

    public void setItemShipments(Set<ItemShipment> itemShipments) {
        if (this.itemShipments != null) {
            this.itemShipments.forEach(i -> i.setCompany(null));
        }
        if (itemShipments != null) {
            itemShipments.forEach(i -> i.setCompany(this));
        }
        this.itemShipments = itemShipments;
    }

    public Company itemShipments(Set<ItemShipment> itemShipments) {
        this.setItemShipments(itemShipments);
        return this;
    }

    public Company addItemShipment(ItemShipment itemShipment) {
        this.itemShipments.add(itemShipment);
        itemShipment.setCompany(this);
        return this;
    }

    public Company removeItemShipment(ItemShipment itemShipment) {
        this.itemShipments.remove(itemShipment);
        itemShipment.setCompany(null);
        return this;
    }

    public Set<ItemSupplier> getItemSuppliers() {
        return this.itemSuppliers;
    }

    public void setItemSuppliers(Set<ItemSupplier> itemSuppliers) {
        if (this.itemSuppliers != null) {
            this.itemSuppliers.forEach(i -> i.setCompany(null));
        }
        if (itemSuppliers != null) {
            itemSuppliers.forEach(i -> i.setCompany(this));
        }
        this.itemSuppliers = itemSuppliers;
    }

    public Company itemSuppliers(Set<ItemSupplier> itemSuppliers) {
        this.setItemSuppliers(itemSuppliers);
        return this;
    }

    public Company addItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.add(itemSupplier);
        itemSupplier.setCompany(this);
        return this;
    }

    public Company removeItemSupplier(ItemSupplier itemSupplier) {
        this.itemSuppliers.remove(itemSupplier);
        itemSupplier.setCompany(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCompany(null));
        }
        if (items != null) {
            items.forEach(i -> i.setCompany(this));
        }
        this.items = items;
    }

    public Company items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Company addItem(Item item) {
        this.items.add(item);
        item.setCompany(this);
        return this;
    }

    public Company removeItem(Item item) {
        this.items.remove(item);
        item.setCompany(null);
        return this;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public void setMachines(Set<Machine> machines) {
        if (this.machines != null) {
            this.machines.forEach(i -> i.setCompany(null));
        }
        if (machines != null) {
            machines.forEach(i -> i.setCompany(this));
        }
        this.machines = machines;
    }

    public Company machines(Set<Machine> machines) {
        this.setMachines(machines);
        return this;
    }

    public Company addMachine(Machine machine) {
        this.machines.add(machine);
        machine.setCompany(this);
        return this;
    }

    public Company removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.setCompany(null);
        return this;
    }

    public Set<MachineUsage> getMachineUsages() {
        return this.machineUsages;
    }

    public void setMachineUsages(Set<MachineUsage> machineUsages) {
        if (this.machineUsages != null) {
            this.machineUsages.forEach(i -> i.setCompany(null));
        }
        if (machineUsages != null) {
            machineUsages.forEach(i -> i.setCompany(this));
        }
        this.machineUsages = machineUsages;
    }

    public Company machineUsages(Set<MachineUsage> machineUsages) {
        this.setMachineUsages(machineUsages);
        return this;
    }

    public Company addMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.add(machineUsage);
        machineUsage.setCompany(this);
        return this;
    }

    public Company removeMachineUsage(MachineUsage machineUsage) {
        this.machineUsages.remove(machineUsage);
        machineUsage.setCompany(null);
        return this;
    }

    public Set<ProductUsageDetail> getProductUsageDetails() {
        return this.productUsageDetails;
    }

    public void setProductUsageDetails(Set<ProductUsageDetail> productUsageDetails) {
        if (this.productUsageDetails != null) {
            this.productUsageDetails.forEach(i -> i.setCompany(null));
        }
        if (productUsageDetails != null) {
            productUsageDetails.forEach(i -> i.setCompany(this));
        }
        this.productUsageDetails = productUsageDetails;
    }

    public Company productUsageDetails(Set<ProductUsageDetail> productUsageDetails) {
        this.setProductUsageDetails(productUsageDetails);
        return this;
    }

    public Company addProductUsageDetail(ProductUsageDetail productUsageDetail) {
        this.productUsageDetails.add(productUsageDetail);
        productUsageDetail.setCompany(this);
        return this;
    }

    public Company removeProductUsageDetail(ProductUsageDetail productUsageDetail) {
        this.productUsageDetails.remove(productUsageDetail);
        productUsageDetail.setCompany(null);
        return this;
    }

    public Set<ProductionQty> getProductionQties() {
        return this.productionQties;
    }

    public void setProductionQties(Set<ProductionQty> productionQties) {
        if (this.productionQties != null) {
            this.productionQties.forEach(i -> i.setCompany(null));
        }
        if (productionQties != null) {
            productionQties.forEach(i -> i.setCompany(this));
        }
        this.productionQties = productionQties;
    }

    public Company productionQties(Set<ProductionQty> productionQties) {
        this.setProductionQties(productionQties);
        return this;
    }

    public Company addProductionQty(ProductionQty productionQty) {
        this.productionQties.add(productionQty);
        productionQty.setCompany(this);
        return this;
    }

    public Company removeProductionQty(ProductionQty productionQty) {
        this.productionQties.remove(productionQty);
        productionQty.setCompany(null);
        return this;
    }

    public Set<PurchasedQty> getPurchasedQties() {
        return this.purchasedQties;
    }

    public void setPurchasedQties(Set<PurchasedQty> purchasedQties) {
        if (this.purchasedQties != null) {
            this.purchasedQties.forEach(i -> i.setCompany(null));
        }
        if (purchasedQties != null) {
            purchasedQties.forEach(i -> i.setCompany(this));
        }
        this.purchasedQties = purchasedQties;
    }

    public Company purchasedQties(Set<PurchasedQty> purchasedQties) {
        this.setPurchasedQties(purchasedQties);
        return this;
    }

    public Company addPurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.add(purchasedQty);
        purchasedQty.setCompany(this);
        return this;
    }

    public Company removePurchasedQty(PurchasedQty purchasedQty) {
        this.purchasedQties.remove(purchasedQty);
        purchasedQty.setCompany(null);
        return this;
    }

    public Set<Routing> getRoutings() {
        return this.routings;
    }

    public void setRoutings(Set<Routing> routings) {
        if (this.routings != null) {
            this.routings.forEach(i -> i.setCompany(null));
        }
        if (routings != null) {
            routings.forEach(i -> i.setCompany(this));
        }
        this.routings = routings;
    }

    public Company routings(Set<Routing> routings) {
        this.setRoutings(routings);
        return this;
    }

    public Company addRouting(Routing routing) {
        this.routings.add(routing);
        routing.setCompany(this);
        return this;
    }

    public Company removeRouting(Routing routing) {
        this.routings.remove(routing);
        routing.setCompany(null);
        return this;
    }

    public Set<ShipmentLane> getShipmentLanes() {
        return this.shipmentLanes;
    }

    public void setShipmentLanes(Set<ShipmentLane> shipmentLanes) {
        if (this.shipmentLanes != null) {
            this.shipmentLanes.forEach(i -> i.setCompany(null));
        }
        if (shipmentLanes != null) {
            shipmentLanes.forEach(i -> i.setCompany(this));
        }
        this.shipmentLanes = shipmentLanes;
    }

    public Company shipmentLanes(Set<ShipmentLane> shipmentLanes) {
        this.setShipmentLanes(shipmentLanes);
        return this;
    }

    public Company addShipmentLane(ShipmentLane shipmentLane) {
        this.shipmentLanes.add(shipmentLane);
        shipmentLane.setCompany(this);
        return this;
    }

    public Company removeShipmentLane(ShipmentLane shipmentLane) {
        this.shipmentLanes.remove(shipmentLane);
        shipmentLane.setCompany(null);
        return this;
    }

    public Set<SiteFinishedGood> getSiteFinishedGoods() {
        return this.siteFinishedGoods;
    }

    public void setSiteFinishedGoods(Set<SiteFinishedGood> siteFinishedGoods) {
        if (this.siteFinishedGoods != null) {
            this.siteFinishedGoods.forEach(i -> i.setCompany(null));
        }
        if (siteFinishedGoods != null) {
            siteFinishedGoods.forEach(i -> i.setCompany(this));
        }
        this.siteFinishedGoods = siteFinishedGoods;
    }

    public Company siteFinishedGoods(Set<SiteFinishedGood> siteFinishedGoods) {
        this.setSiteFinishedGoods(siteFinishedGoods);
        return this;
    }

    public Company addSiteFinishedGood(SiteFinishedGood siteFinishedGood) {
        this.siteFinishedGoods.add(siteFinishedGood);
        siteFinishedGood.setCompany(this);
        return this;
    }

    public Company removeSiteFinishedGood(SiteFinishedGood siteFinishedGood) {
        this.siteFinishedGoods.remove(siteFinishedGood);
        siteFinishedGood.setCompany(null);
        return this;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        if (this.sites != null) {
            this.sites.forEach(i -> i.setCompany(null));
        }
        if (sites != null) {
            sites.forEach(i -> i.setCompany(this));
        }
        this.sites = sites;
    }

    public Company sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public Company addSite(Site site) {
        this.sites.add(site);
        site.setCompany(this);
        return this;
    }

    public Company removeSite(Site site) {
        this.sites.remove(site);
        site.setCompany(null);
        return this;
    }

    public Set<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        if (this.suppliers != null) {
            this.suppliers.forEach(i -> i.setCompany(null));
        }
        if (suppliers != null) {
            suppliers.forEach(i -> i.setCompany(this));
        }
        this.suppliers = suppliers;
    }

    public Company suppliers(Set<Supplier> suppliers) {
        this.setSuppliers(suppliers);
        return this;
    }

    public Company addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.setCompany(this);
        return this;
    }

    public Company removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.setCompany(null);
        return this;
    }

    public Set<Wastedisposal> getWastedisposals() {
        return this.wastedisposals;
    }

    public void setWastedisposals(Set<Wastedisposal> wastedisposals) {
        if (this.wastedisposals != null) {
            this.wastedisposals.forEach(i -> i.setCompany(null));
        }
        if (wastedisposals != null) {
            wastedisposals.forEach(i -> i.setCompany(this));
        }
        this.wastedisposals = wastedisposals;
    }

    public Company wastedisposals(Set<Wastedisposal> wastedisposals) {
        this.setWastedisposals(wastedisposals);
        return this;
    }

    public Company addWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.add(wastedisposal);
        wastedisposal.setCompany(this);
        return this;
    }

    public Company removeWastedisposal(Wastedisposal wastedisposal) {
        this.wastedisposals.remove(wastedisposal);
        wastedisposal.setCompany(null);
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return getId() != null && getId().equals(((Company) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", country='" + getCountry() + "'" +
            ", industryType='" + getIndustryType() + "'" +
            ", admin='" + getAdmin() + "'" +
            ", domain='" + getDomain() + "'" +
            ", status='" + getStatus() + "'" +
            ", bacutiCapsEnabled='" + getBacutiCapsEnabled() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", cbamImpacted='" + getCbamImpacted() + "'" +
            "}";
    }
}
