package com.bacuti.domain;

import static com.bacuti.domain.AggregateEnergyUsageTestSamples.*;
import static com.bacuti.domain.CapitalGoodTestSamples.*;
import static com.bacuti.domain.CarbonPricePaymentTestSamples.*;
import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.EmployeeTravelAvgTestSamples.*;
import static com.bacuti.domain.EnergySourceTestSamples.*;
import static com.bacuti.domain.ItemShipmentTestSamples.*;
import static com.bacuti.domain.MachineUsageTestSamples.*;
import static com.bacuti.domain.ProductionQtyTestSamples.*;
import static com.bacuti.domain.SiteFinishedGoodTestSamples.*;
import static com.bacuti.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Site.class);
        Site site1 = getSiteSample1();
        Site site2 = new Site();
        assertThat(site1).isNotEqualTo(site2);

        site2.setId(site1.getId());
        assertThat(site1).isEqualTo(site2);

        site2 = getSiteSample2();
        assertThat(site1).isNotEqualTo(site2);
    }

    @Test
    void companyTest() {
        Site site = getSiteRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        site.setCompany(companyBack);
        assertThat(site.getCompany()).isEqualTo(companyBack);

        site.company(null);
        assertThat(site.getCompany()).isNull();
    }

    @Test
    void aggregateEnergyUsageTest() {
        Site site = getSiteRandomSampleGenerator();
        AggregateEnergyUsage aggregateEnergyUsageBack = getAggregateEnergyUsageRandomSampleGenerator();

        site.addAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(site.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getSite()).isEqualTo(site);

        site.removeAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(site.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getSite()).isNull();

        site.aggregateEnergyUsages(new HashSet<>(Set.of(aggregateEnergyUsageBack)));
        assertThat(site.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getSite()).isEqualTo(site);

        site.setAggregateEnergyUsages(new HashSet<>());
        assertThat(site.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getSite()).isNull();
    }

    @Test
    void capitalGoodTest() {
        Site site = getSiteRandomSampleGenerator();
        CapitalGood capitalGoodBack = getCapitalGoodRandomSampleGenerator();

        site.addCapitalGood(capitalGoodBack);
        assertThat(site.getCapitalGoods()).containsOnly(capitalGoodBack);
        assertThat(capitalGoodBack.getSite()).isEqualTo(site);

        site.removeCapitalGood(capitalGoodBack);
        assertThat(site.getCapitalGoods()).doesNotContain(capitalGoodBack);
        assertThat(capitalGoodBack.getSite()).isNull();

        site.capitalGoods(new HashSet<>(Set.of(capitalGoodBack)));
        assertThat(site.getCapitalGoods()).containsOnly(capitalGoodBack);
        assertThat(capitalGoodBack.getSite()).isEqualTo(site);

        site.setCapitalGoods(new HashSet<>());
        assertThat(site.getCapitalGoods()).doesNotContain(capitalGoodBack);
        assertThat(capitalGoodBack.getSite()).isNull();
    }

    @Test
    void carbonPricePaymentTest() {
        Site site = getSiteRandomSampleGenerator();
        CarbonPricePayment carbonPricePaymentBack = getCarbonPricePaymentRandomSampleGenerator();

        site.addCarbonPricePayment(carbonPricePaymentBack);
        assertThat(site.getCarbonPricePayments()).containsOnly(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getSite()).isEqualTo(site);

        site.removeCarbonPricePayment(carbonPricePaymentBack);
        assertThat(site.getCarbonPricePayments()).doesNotContain(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getSite()).isNull();

        site.carbonPricePayments(new HashSet<>(Set.of(carbonPricePaymentBack)));
        assertThat(site.getCarbonPricePayments()).containsOnly(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getSite()).isEqualTo(site);

        site.setCarbonPricePayments(new HashSet<>());
        assertThat(site.getCarbonPricePayments()).doesNotContain(carbonPricePaymentBack);
        assertThat(carbonPricePaymentBack.getSite()).isNull();
    }

    @Test
    void employeeTravelAvgTest() {
        Site site = getSiteRandomSampleGenerator();
        EmployeeTravelAvg employeeTravelAvgBack = getEmployeeTravelAvgRandomSampleGenerator();

        site.addEmployeeTravelAvg(employeeTravelAvgBack);
        assertThat(site.getEmployeeTravelAvgs()).containsOnly(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getSite()).isEqualTo(site);

        site.removeEmployeeTravelAvg(employeeTravelAvgBack);
        assertThat(site.getEmployeeTravelAvgs()).doesNotContain(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getSite()).isNull();

        site.employeeTravelAvgs(new HashSet<>(Set.of(employeeTravelAvgBack)));
        assertThat(site.getEmployeeTravelAvgs()).containsOnly(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getSite()).isEqualTo(site);

        site.setEmployeeTravelAvgs(new HashSet<>());
        assertThat(site.getEmployeeTravelAvgs()).doesNotContain(employeeTravelAvgBack);
        assertThat(employeeTravelAvgBack.getSite()).isNull();
    }

    @Test
    void energySourceTest() {
        Site site = getSiteRandomSampleGenerator();
        EnergySource energySourceBack = getEnergySourceRandomSampleGenerator();

        site.addEnergySource(energySourceBack);
        assertThat(site.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getSite()).isEqualTo(site);

        site.removeEnergySource(energySourceBack);
        assertThat(site.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getSite()).isNull();

        site.energySources(new HashSet<>(Set.of(energySourceBack)));
        assertThat(site.getEnergySources()).containsOnly(energySourceBack);
        assertThat(energySourceBack.getSite()).isEqualTo(site);

        site.setEnergySources(new HashSet<>());
        assertThat(site.getEnergySources()).doesNotContain(energySourceBack);
        assertThat(energySourceBack.getSite()).isNull();
    }

    @Test
    void itemShipmentTest() {
        Site site = getSiteRandomSampleGenerator();
        ItemShipment itemShipmentBack = getItemShipmentRandomSampleGenerator();

        site.addItemShipment(itemShipmentBack);
        assertThat(site.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getSite()).isEqualTo(site);

        site.removeItemShipment(itemShipmentBack);
        assertThat(site.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getSite()).isNull();

        site.itemShipments(new HashSet<>(Set.of(itemShipmentBack)));
        assertThat(site.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getSite()).isEqualTo(site);

        site.setItemShipments(new HashSet<>());
        assertThat(site.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getSite()).isNull();
    }

    @Test
    void machineUsageTest() {
        Site site = getSiteRandomSampleGenerator();
        MachineUsage machineUsageBack = getMachineUsageRandomSampleGenerator();

        site.addMachineUsage(machineUsageBack);
        assertThat(site.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getSite()).isEqualTo(site);

        site.removeMachineUsage(machineUsageBack);
        assertThat(site.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getSite()).isNull();

        site.machineUsages(new HashSet<>(Set.of(machineUsageBack)));
        assertThat(site.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getSite()).isEqualTo(site);

        site.setMachineUsages(new HashSet<>());
        assertThat(site.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getSite()).isNull();
    }

    @Test
    void productionQtyTest() {
        Site site = getSiteRandomSampleGenerator();
        ProductionQty productionQtyBack = getProductionQtyRandomSampleGenerator();

        site.addProductionQty(productionQtyBack);
        assertThat(site.getProductionQties()).containsOnly(productionQtyBack);
        assertThat(productionQtyBack.getSite()).isEqualTo(site);

        site.removeProductionQty(productionQtyBack);
        assertThat(site.getProductionQties()).doesNotContain(productionQtyBack);
        assertThat(productionQtyBack.getSite()).isNull();

        site.productionQties(new HashSet<>(Set.of(productionQtyBack)));
        assertThat(site.getProductionQties()).containsOnly(productionQtyBack);
        assertThat(productionQtyBack.getSite()).isEqualTo(site);

        site.setProductionQties(new HashSet<>());
        assertThat(site.getProductionQties()).doesNotContain(productionQtyBack);
        assertThat(productionQtyBack.getSite()).isNull();
    }

    @Test
    void siteFinishedGoodTest() {
        Site site = getSiteRandomSampleGenerator();
        SiteFinishedGood siteFinishedGoodBack = getSiteFinishedGoodRandomSampleGenerator();

        site.addSiteFinishedGood(siteFinishedGoodBack);
        assertThat(site.getSiteFinishedGoods()).containsOnly(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getSite()).isEqualTo(site);

        site.removeSiteFinishedGood(siteFinishedGoodBack);
        assertThat(site.getSiteFinishedGoods()).doesNotContain(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getSite()).isNull();

        site.siteFinishedGoods(new HashSet<>(Set.of(siteFinishedGoodBack)));
        assertThat(site.getSiteFinishedGoods()).containsOnly(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getSite()).isEqualTo(site);

        site.setSiteFinishedGoods(new HashSet<>());
        assertThat(site.getSiteFinishedGoods()).doesNotContain(siteFinishedGoodBack);
        assertThat(siteFinishedGoodBack.getSite()).isNull();
    }
}
