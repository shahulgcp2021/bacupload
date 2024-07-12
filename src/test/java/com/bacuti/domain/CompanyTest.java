package com.bacuti.domain;

import static com.bacuti.domain.AggregateEnergyUsageTestSamples.*;
import static com.bacuti.domain.BillofMaterialTestSamples.*;
import static com.bacuti.domain.CapitalGoodTestSamples.*;
import static com.bacuti.domain.CarbonPricePaymentTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.CustomerTestSamples.*;
import static com.bacuti.domain.EmployeeTravelAvgTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.ItemShipmentTestSamples.*;
import static com.bacuti.domain.ItemSupplierTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.MachineTestSamples.*;
import static com.bacuti.domain.MachineUsageTestSamples.*;
import static com.bacuti.domain.ProductUsageDetailTestSamples.*;
import static com.bacuti.domain.ProductionQtyTestSamples.*;
import static com.bacuti.domain.PurchasedQtyTestSamples.*;
import static com.bacuti.domain.RoutingTestSamples.*;
import static com.bacuti.domain.ShipmentLaneTestSamples.*;
import static com.bacuti.domain.SiteFinishedGoodTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static com.bacuti.domain.SupplierTestSamples.*;
import static com.bacuti.domain.WastedisposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void aggregateEnergyUsageTest() {
        Company company = getCompanyRandomSampleGenerator();
        AggregateEnergyUsage aggregateEnergyUsageBack = getAggregateEnergyUsageRandomSampleGenerator();

        company.addAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(company.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getCompany()).isEqualTo(company);

        company.removeAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(company.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getCompany()).isNull();

        company.aggregateEnergyUsages(new HashSet<>(Set.of(aggregateEnergyUsageBack)));
        assertThat(company.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getCompany()).isEqualTo(company);

        company.setAggregateEnergyUsages(new HashSet<>());
        assertThat(company.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getCompany()).isNull();
    }

    @Test
    void billofMaterialTest() {
        Company company = getCompanyRandomSampleGenerator();
        BillofMaterial billofMaterialBack = getBillofMaterialRandomSampleGenerator();

        company.addBillofMaterial(billofMaterialBack);
        assertThat(company.getBillofMaterials()).containsOnly(billofMaterialBack);
        assertThat(billofMaterialBack.getCompany()).isEqualTo(company);

        company.removeBillofMaterial(billofMaterialBack);
        assertThat(company.getBillofMaterials()).doesNotContain(billofMaterialBack);
        assertThat(billofMaterialBack.getCompany()).isNull();

        company.billofMaterials(new HashSet<>(Set.of(billofMaterialBack)));
        assertThat(company.getBillofMaterials()).containsOnly(billofMaterialBack);
        assertThat(billofMaterialBack.getCompany()).isEqualTo(company);

        company.setBillofMaterials(new HashSet<>());
        assertThat(company.getBillofMaterials()).doesNotContain(billofMaterialBack);
        assertThat(billofMaterialBack.getCompany()).isNull();
    }

    @Test
    void capitalGoodTest() {
        Company company = getCompanyRandomSampleGenerator();
        CapitalGood capitalGoodBack = getCapitalGoodRandomSampleGenerator();

        company.addCapitalGood(capitalGoodBack);
        assertThat(company.getCapitalGoods()).containsOnly(capitalGoodBack);
        assertThat(capitalGoodBack.getCompany()).isEqualTo(company);

        company.removeCapitalGood(capitalGoodBack);
        assertThat(company.getCapitalGoods()).doesNotContain(capitalGoodBack);
        assertThat(capitalGoodBack.getCompany()).isNull();

        company.capitalGoods(new HashSet<>(Set.of(capitalGoodBack)));
        assertThat(company.getCapitalGoods()).containsOnly(capitalGoodBack);
        assertThat(capitalGoodBack.getCompany()).isEqualTo(company);

        company.setCapitalGoods(new HashSet<>());
        assertThat(company.getCapitalGoods()).doesNotContain(capitalGoodBack);
        assertThat(capitalGoodBack.getCompany()).isNull();
    }

    @Test
    void carbonPricePaymentTest() {
        Company company = getCompanyRandomSampleGenerator();
        CarbonPricePayment carbonPricePaymentBack = getCarbonPricePaymentRandomSampleGenerator();

        company.addCarbonPricePayment(carbonPricePaymentBack);
        assertThat(company.getCarbonPricePayments()).containsOnly(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getCompany()).isEqualTo(company);

        company.removeCarbonPricePayment(carbonPricePaymentBack);
        assertThat(company.getCarbonPricePayments()).doesNotContain(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getCompany()).isNull();

        company.carbonPricePayments(new HashSet<>(Set.of(carbonPricePaymentBack)));
        assertThat(company.getCarbonPricePayments()).containsOnly(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getCompany()).isEqualTo(company);

        company.setCarbonPricePayments(new HashSet<>());
        assertThat(company.getCarbonPricePayments()).doesNotContain(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getCompany()).isNull();
    }

    @Test
    void customerTest() {
        Company company = getCompanyRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        company.addCustomer(customerBack);
        assertThat(company.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getCompany()).isEqualTo(company);

        company.removeCustomer(customerBack);
        assertThat(company.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getCompany()).isNull();

        company.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(company.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getCompany()).isEqualTo(company);

        company.setCustomers(new HashSet<>());
        assertThat(company.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getCompany()).isNull();
    }

    @Test
    void employeeTravelAvgTest() {
        Company company = getCompanyRandomSampleGenerator();
        EmployeeTravelAvg employeeTravelAvgBack = getEmployeeTravelAvgRandomSampleGenerator();

        company.addEmployeeTravelAvg(employeeTravelAvgBack);
        assertThat(company.getEmployeeTravelAvgs()).containsOnly(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getCompany()).isEqualTo(company);

        company.removeEmployeeTravelAvg(employeeTravelAvgBack);
        assertThat(company.getEmployeeTravelAvgs()).doesNotContain(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getCompany()).isNull();

        company.employeeTravelAvgs(new HashSet<>(Set.of(employeeTravelAvgBack)));
        assertThat(company.getEmployeeTravelAvgs()).containsOnly(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getCompany()).isEqualTo(company);

        company.setEmployeeTravelAvgs(new HashSet<>());
        assertThat(company.getEmployeeTravelAvgs()).doesNotContain(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getCompany()).isNull();
    }

    @Test
    void energySourceTest() {
        Company company = getCompanyRandomSampleGenerator();
        EnergySource energySourceBack = getEnergySourceRandomSampleGenerator();

        company.addEnergySource(energySourceBack);
        assertThat(company.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getCompany()).isEqualTo(company);

        company.removeEnergySource(energySourceBack);
        assertThat(company.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getCompany()).isNull();

        company.energySources(new HashSet<>(Set.of(energySourceBack)));
        assertThat(company.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getCompany()).isEqualTo(company);

        company.setEnergySources(new HashSet<>());
        assertThat(company.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getCompany()).isNull();
    }

    @Test
    void itemShipmentTest() {
        Company company = getCompanyRandomSampleGenerator();
        ItemShipment itemShipmentBack = getItemShipmentRandomSampleGenerator();

        company.addItemShipment(itemShipmentBack);
        assertThat(company.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getCompany()).isEqualTo(company);

        company.removeItemShipment(itemShipmentBack);
        assertThat(company.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getCompany()).isNull();

        company.itemShipments(new HashSet<>(Set.of(itemShipmentBack)));
        assertThat(company.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getCompany()).isEqualTo(company);

        company.setItemShipments(new HashSet<>());
        assertThat(company.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getCompany()).isNull();
    }

    @Test
    void itemSupplierTest() {
        Company company = getCompanyRandomSampleGenerator();
        ItemSupplier itemSupplierBack = getItemSupplierRandomSampleGenerator();

        company.addItemSupplier(itemSupplierBack);
        assertThat(company.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getCompany()).isEqualTo(company);

        company.removeItemSupplier(itemSupplierBack);
        assertThat(company.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getCompany()).isNull();

        company.itemSuppliers(new HashSet<>(Set.of(itemSupplierBack)));
        assertThat(company.getItemSuppliers()).containsOnly(itemSupplierBack);
        assertThat(itemSupplierBack.getCompany()).isEqualTo(company);

        company.setItemSuppliers(new HashSet<>());
        assertThat(company.getItemSuppliers()).doesNotContain(itemSupplierBack);
        assertThat(itemSupplierBack.getCompany()).isNull();
    }

    @Test
    void itemTest() {
        Company company = getCompanyRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        company.addItem(itemBack);
        assertThat(company.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getCompany()).isEqualTo(company);

        company.removeItem(itemBack);
        assertThat(company.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getCompany()).isNull();

        company.items(new HashSet<>(Set.of(itemBack)));
        assertThat(company.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getCompany()).isEqualTo(company);

        company.setItems(new HashSet<>());
        assertThat(company.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getCompany()).isNull();
    }

    @Test
    void machineTest() {
        Company company = getCompanyRandomSampleGenerator();
        Machine machineBack = getMachineRandomSampleGenerator();

        company.addMachine(machineBack);
        assertThat(company.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getCompany()).isEqualTo(company);

        company.removeMachine(machineBack);
        assertThat(company.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getCompany()).isNull();

        company.machines(new HashSet<>(Set.of(machineBack)));
        assertThat(company.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getCompany()).isEqualTo(company);

        company.setMachines(new HashSet<>());
        assertThat(company.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getCompany()).isNull();
    }

    @Test
    void machineUsageTest() {
        Company company = getCompanyRandomSampleGenerator();
        MachineUsage machineUsageBack = getMachineUsageRandomSampleGenerator();

        company.addMachineUsage(machineUsageBack);
        assertThat(company.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getCompany()).isEqualTo(company);

        company.removeMachineUsage(machineUsageBack);
        assertThat(company.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getCompany()).isNull();

        company.machineUsages(new HashSet<>(Set.of(machineUsageBack)));
        assertThat(company.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getCompany()).isEqualTo(company);

        company.setMachineUsages(new HashSet<>());
        assertThat(company.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getCompany()).isNull();
    }

    @Test
    void productUsageDetailTest() {
        Company company = getCompanyRandomSampleGenerator();
        ProductUsageDetail productUsageDetailBack = getProductUsageDetailRandomSampleGenerator();

        company.addProductUsageDetail(productUsageDetailBack);
        assertThat(company.getProductUsageDetails()).containsOnly(productUsageDetailBack);
        assertThat(productUsageDetailBack.getCompany()).isEqualTo(company);

        company.removeProductUsageDetail(productUsageDetailBack);
        assertThat(company.getProductUsageDetails()).doesNotContain(productUsageDetailBack);
        assertThat(productUsageDetailBack.getCompany()).isNull();

        company.productUsageDetails(new HashSet<>(Set.of(productUsageDetailBack)));
        assertThat(company.getProductUsageDetails()).containsOnly(productUsageDetailBack);
        assertThat(productUsageDetailBack.getCompany()).isEqualTo(company);

        company.setProductUsageDetails(new HashSet<>());
        assertThat(company.getProductUsageDetails()).doesNotContain(productUsageDetailBack);
        assertThat(productUsageDetailBack.getCompany()).isNull();
    }

    @Test
    void productionQtyTest() {
        Company company = getCompanyRandomSampleGenerator();
        ProductionQty productionQtyBack = getProductionQtyRandomSampleGenerator();

        company.addProductionQty(productionQtyBack);
        assertThat(company.getProductionQties()).containsOnly(productionQtyBack);
        assertThat(productionQtyBack.getCompany()).isEqualTo(company);

        company.removeProductionQty(productionQtyBack);
        assertThat(company.getProductionQties()).doesNotContain(productionQtyBack);
        assertThat(productionQtyBack.getCompany()).isNull();

        company.productionQties(new HashSet<>(Set.of(productionQtyBack)));
        assertThat(company.getProductionQties()).containsOnly(productionQtyBack);
        assertThat(productionQtyBack.getCompany()).isEqualTo(company);

        company.setProductionQties(new HashSet<>());
        assertThat(company.getProductionQties()).doesNotContain(productionQtyBack);
        assertThat(productionQtyBack.getCompany()).isNull();
    }

    @Test
    void purchasedQtyTest() {
        Company company = getCompanyRandomSampleGenerator();
        PurchasedQty purchasedQtyBack = getPurchasedQtyRandomSampleGenerator();

        company.addPurchasedQty(purchasedQtyBack);
        assertThat(company.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getCompany()).isEqualTo(company);

        company.removePurchasedQty(purchasedQtyBack);
        assertThat(company.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getCompany()).isNull();

        company.purchasedQties(new HashSet<>(Set.of(purchasedQtyBack)));
        assertThat(company.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getCompany()).isEqualTo(company);

        company.setPurchasedQties(new HashSet<>());
        assertThat(company.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getCompany()).isNull();
    }

    @Test
    void routingTest() {
        Company company = getCompanyRandomSampleGenerator();
        Routing routingBack = getRoutingRandomSampleGenerator();

        company.addRouting(routingBack);
        assertThat(company.getRoutings()).containsOnly(routingBack);
        assertThat(routingBack.getCompany()).isEqualTo(company);

        company.removeRouting(routingBack);
        assertThat(company.getRoutings()).doesNotContain(routingBack);
        assertThat(routingBack.getCompany()).isNull();

        company.routings(new HashSet<>(Set.of(routingBack)));
        assertThat(company.getRoutings()).containsOnly(routingBack);
        assertThat(routingBack.getCompany()).isEqualTo(company);

        company.setRoutings(new HashSet<>());
        assertThat(company.getRoutings()).doesNotContain(routingBack);
        assertThat(routingBack.getCompany()).isNull();
    }

    @Test
    void shipmentLaneTest() {
        Company company = getCompanyRandomSampleGenerator();
        ShipmentLane shipmentLaneBack = getShipmentLaneRandomSampleGenerator();

        company.addShipmentLane(shipmentLaneBack);
        assertThat(company.getShipmentLanes()).containsOnly(shipmentLaneBack);
        assertThat(shipmentLaneBack.getCompany()).isEqualTo(company);

        company.removeShipmentLane(shipmentLaneBack);
        assertThat(company.getShipmentLanes()).doesNotContain(shipmentLaneBack);
        assertThat(shipmentLaneBack.getCompany()).isNull();

        company.shipmentLanes(new HashSet<>(Set.of(shipmentLaneBack)));
        assertThat(company.getShipmentLanes()).containsOnly(shipmentLaneBack);
        assertThat(shipmentLaneBack.getCompany()).isEqualTo(company);

        company.setShipmentLanes(new HashSet<>());
        assertThat(company.getShipmentLanes()).doesNotContain(shipmentLaneBack);
        assertThat(shipmentLaneBack.getCompany()).isNull();
    }

    @Test
    void siteFinishedGoodTest() {
        Company company = getCompanyRandomSampleGenerator();
        SiteFinishedGood siteFinishedGoodBack = getSiteFinishedGoodRandomSampleGenerator();

        company.addSiteFinishedGood(siteFinishedGoodBack);
        assertThat(company.getSiteFinishedGoods()).containsOnly(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getCompany()).isEqualTo(company);

        company.removeSiteFinishedGood(siteFinishedGoodBack);
        assertThat(company.getSiteFinishedGoods()).doesNotContain(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getCompany()).isNull();

        company.siteFinishedGoods(new HashSet<>(Set.of(siteFinishedGoodBack)));
        assertThat(company.getSiteFinishedGoods()).containsOnly(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getCompany()).isEqualTo(company);

        company.setSiteFinishedGoods(new HashSet<>());
        assertThat(company.getSiteFinishedGoods()).doesNotContain(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getCompany()).isNull();
    }

    @Test
    void siteTest() {
        Company company = getCompanyRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        company.addSite(siteBack);
        assertThat(company.getSites()).containsOnly(siteBack);
        assertThat(siteBack.getCompany()).isEqualTo(company);

        company.removeSite(siteBack);
        assertThat(company.getSites()).doesNotContain(siteBack);
        assertThat(siteBack.getCompany()).isNull();

        company.sites(new HashSet<>(Set.of(siteBack)));
        assertThat(company.getSites()).containsOnly(siteBack);
        assertThat(siteBack.getCompany()).isEqualTo(company);

        company.setSites(new HashSet<>());
        assertThat(company.getSites()).doesNotContain(siteBack);
        assertThat(siteBack.getCompany()).isNull();
    }

    @Test
    void supplierTest() {
        Company company = getCompanyRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        company.addSupplier(supplierBack);
        assertThat(company.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getCompany()).isEqualTo(company);

        company.removeSupplier(supplierBack);
        assertThat(company.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getCompany()).isNull();

        company.suppliers(new HashSet<>(Set.of(supplierBack)));
        assertThat(company.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getCompany()).isEqualTo(company);

        company.setSuppliers(new HashSet<>());
        assertThat(company.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getCompany()).isNull();
    }

    @Test
    void wastedisposalTest() {
        Company company = getCompanyRandomSampleGenerator();
        Wastedisposal wastedisposalBack = getWastedisposalRandomSampleGenerator();

        company.addWastedisposal(wastedisposalBack);
        assertThat(company.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getCompany()).isEqualTo(company);

        company.removeWastedisposal(wastedisposalBack);
        assertThat(company.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getCompany()).isNull();

        company.wastedisposals(new HashSet<>(Set.of(wastedisposalBack)));
        assertThat(company.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getCompany()).isEqualTo(company);

        company.setWastedisposals(new HashSet<>());
        assertThat(company.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getCompany()).isNull();
    }
}
