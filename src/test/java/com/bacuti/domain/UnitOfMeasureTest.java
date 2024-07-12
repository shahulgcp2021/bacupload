package com.bacuti.domain;

import static com.bacuti.domain.AggregateEnergyUsageTestSamples.*;
import static com.bacuti.domain.BillofMaterialTestSamples.*;
import static com.bacuti.domain.DefaultAverageEFTestSamples.*;
import static com.bacuti.domain.ItemTestSamples.*;
import static com.bacuti.domain.MachineUsageTestSamples.*;
import static com.bacuti.domain.ProductUsageDetailTestSamples.*;
import static com.bacuti.domain.PurchasedQtyTestSamples.*;
import static com.bacuti.domain.RoutingTestSamples.*;
import static com.bacuti.domain.UnitOfMeasureTestSamples.*;
import static com.bacuti.domain.WastedisposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UnitOfMeasureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasure.class);
        UnitOfMeasure unitOfMeasure1 = getUnitOfMeasureSample1();
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);

        unitOfMeasure2.setId(unitOfMeasure1.getId());
        assertThat(unitOfMeasure1).isEqualTo(unitOfMeasure2);

        unitOfMeasure2 = getUnitOfMeasureSample2();
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);
    }

    @Test
    void aggregateEnergyUsageTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        AggregateEnergyUsage aggregateEnergyUsageBack = getAggregateEnergyUsageRandomSampleGenerator();

        unitOfMeasure.addAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(unitOfMeasure.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeAggregateEnergyUsage(aggregateEnergyUsageBack);
        assertThat(unitOfMeasure.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.aggregateEnergyUsages(new HashSet<>(Set.of(aggregateEnergyUsageBack)));
        assertThat(unitOfMeasure.getAggregateEnergyUsages()).containsOnly(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setAggregateEnergyUsages(new HashSet<>());
        assertThat(unitOfMeasure.getAggregateEnergyUsages()).doesNotContain(aggregateEnergyUsageBack);
        assertThat(aggregateEnergyUsageBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void billofMaterialTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        BillofMaterial billofMaterialBack = getBillofMaterialRandomSampleGenerator();

        unitOfMeasure.addBillofMaterial(billofMaterialBack);
        assertThat(unitOfMeasure.getBillofMaterials()).containsOnly(billofMaterialBack);
        assertThat(billofMaterialBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeBillofMaterial(billofMaterialBack);
        assertThat(unitOfMeasure.getBillofMaterials()).doesNotContain(billofMaterialBack);
        assertThat(billofMaterialBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.billofMaterials(new HashSet<>(Set.of(billofMaterialBack)));
        assertThat(unitOfMeasure.getBillofMaterials()).containsOnly(billofMaterialBack);
        assertThat(billofMaterialBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setBillofMaterials(new HashSet<>());
        assertThat(unitOfMeasure.getBillofMaterials()).doesNotContain(billofMaterialBack);
        assertThat(billofMaterialBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void defaultAverageEFTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        DefaultAverageEF defaultAverageEFBack = getDefaultAverageEFRandomSampleGenerator();

        unitOfMeasure.addDefaultAverageEF(defaultAverageEFBack);
        assertThat(unitOfMeasure.getDefaultAverageEFS()).containsOnly(defaultAverageEFBack);
        assertThat(defaultAverageEFBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeDefaultAverageEF(defaultAverageEFBack);
        assertThat(unitOfMeasure.getDefaultAverageEFS()).doesNotContain(defaultAverageEFBack);
        assertThat(defaultAverageEFBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.defaultAverageEFS(new HashSet<>(Set.of(defaultAverageEFBack)));
        assertThat(unitOfMeasure.getDefaultAverageEFS()).containsOnly(defaultAverageEFBack);
        assertThat(defaultAverageEFBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setDefaultAverageEFS(new HashSet<>());
        assertThat(unitOfMeasure.getDefaultAverageEFS()).doesNotContain(defaultAverageEFBack);
        assertThat(defaultAverageEFBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void itemTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        unitOfMeasure.addItem(itemBack);
        assertThat(unitOfMeasure.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeItem(itemBack);
        assertThat(unitOfMeasure.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.items(new HashSet<>(Set.of(itemBack)));
        assertThat(unitOfMeasure.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setItems(new HashSet<>());
        assertThat(unitOfMeasure.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void machineUsageTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        MachineUsage machineUsageBack = getMachineUsageRandomSampleGenerator();

        unitOfMeasure.addMachineUsage(machineUsageBack);
        assertThat(unitOfMeasure.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeMachineUsage(machineUsageBack);
        assertThat(unitOfMeasure.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.machineUsages(new HashSet<>(Set.of(machineUsageBack)));
        assertThat(unitOfMeasure.getMachineUsages()).containsOnly(machineUsageBack);
        assertThat(machineUsageBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setMachineUsages(new HashSet<>());
        assertThat(unitOfMeasure.getMachineUsages()).doesNotContain(machineUsageBack);
        assertThat(machineUsageBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void productUsageDetailTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        ProductUsageDetail productUsageDetailBack = getProductUsageDetailRandomSampleGenerator();

        unitOfMeasure.addProductUsageDetail(productUsageDetailBack);
        assertThat(unitOfMeasure.getProductUsageDetails()).containsOnly(productUsageDetailBack);
        assertThat(productUsageDetailBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeProductUsageDetail(productUsageDetailBack);
        assertThat(unitOfMeasure.getProductUsageDetails()).doesNotContain(productUsageDetailBack);
        assertThat(productUsageDetailBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.productUsageDetails(new HashSet<>(Set.of(productUsageDetailBack)));
        assertThat(unitOfMeasure.getProductUsageDetails()).containsOnly(productUsageDetailBack);
        assertThat(productUsageDetailBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setProductUsageDetails(new HashSet<>());
        assertThat(unitOfMeasure.getProductUsageDetails()).doesNotContain(productUsageDetailBack);
        assertThat(productUsageDetailBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void purchasedQtyTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        PurchasedQty purchasedQtyBack = getPurchasedQtyRandomSampleGenerator();

        unitOfMeasure.addPurchasedQty(purchasedQtyBack);
        assertThat(unitOfMeasure.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removePurchasedQty(purchasedQtyBack);
        assertThat(unitOfMeasure.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.purchasedQties(new HashSet<>(Set.of(purchasedQtyBack)));
        assertThat(unitOfMeasure.getPurchasedQties()).containsOnly(purchasedQtyBack);
        assertThat(purchasedQtyBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setPurchasedQties(new HashSet<>());
        assertThat(unitOfMeasure.getPurchasedQties()).doesNotContain(purchasedQtyBack);
        assertThat(purchasedQtyBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void routingTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        Routing routingBack = getRoutingRandomSampleGenerator();

        unitOfMeasure.addRouting(routingBack);
        assertThat(unitOfMeasure.getRoutings()).containsOnly(routingBack);
        assertThat(routingBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeRouting(routingBack);
        assertThat(unitOfMeasure.getRoutings()).doesNotContain(routingBack);
        assertThat(routingBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.routings(new HashSet<>(Set.of(routingBack)));
        assertThat(unitOfMeasure.getRoutings()).containsOnly(routingBack);
        assertThat(routingBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setRoutings(new HashSet<>());
        assertThat(unitOfMeasure.getRoutings()).doesNotContain(routingBack);
        assertThat(routingBack.getUnitOfMeasure()).isNull();
    }

    @Test
    void wastedisposalTest() {
        UnitOfMeasure unitOfMeasure = getUnitOfMeasureRandomSampleGenerator();
        Wastedisposal wastedisposalBack = getWastedisposalRandomSampleGenerator();

        unitOfMeasure.addWastedisposal(wastedisposalBack);
        assertThat(unitOfMeasure.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.removeWastedisposal(wastedisposalBack);
        assertThat(unitOfMeasure.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getUnitOfMeasure()).isNull();

        unitOfMeasure.wastedisposals(new HashSet<>(Set.of(wastedisposalBack)));
        assertThat(unitOfMeasure.getWastedisposals()).containsOnly(wastedisposalBack);
        assertThat(wastedisposalBack.getUnitOfMeasure()).isEqualTo(unitOfMeasure);

        unitOfMeasure.setWastedisposals(new HashSet<>());
        assertThat(unitOfMeasure.getWastedisposals()).doesNotContain(wastedisposalBack);
        assertThat(wastedisposalBack.getUnitOfMeasure()).isNull();
    }
}
