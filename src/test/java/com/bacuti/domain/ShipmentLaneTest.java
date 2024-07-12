package com.bacuti.domain;

import static com.bacuti.domain.CompanyTestSamples.*;
import static com.bacuti.domain.ItemShipmentTestSamples.*;
import static com.bacuti.domain.ShipmentLaneTestSamples.*;
import static com.bacuti.domain.ShipmentLegTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShipmentLaneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentLane.class);
        ShipmentLane shipmentLane1 = getShipmentLaneSample1();
        ShipmentLane shipmentLane2 = new ShipmentLane();
        assertThat(shipmentLane1).isNotEqualTo(shipmentLane2);

        shipmentLane2.setId(shipmentLane1.getId());
        assertThat(shipmentLane1).isEqualTo(shipmentLane2);

        shipmentLane2 = getShipmentLaneSample2();
        assertThat(shipmentLane1).isNotEqualTo(shipmentLane2);
    }

    @Test
    void companyTest() {
        ShipmentLane shipmentLane = getShipmentLaneRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        shipmentLane.setCompany(companyBack);
        assertThat(shipmentLane.getCompany()).isEqualTo(companyBack);

        shipmentLane.company(null);
        assertThat(shipmentLane.getCompany()).isNull();
    }

    @Test
    void itemShipmentTest() {
        ShipmentLane shipmentLane = getShipmentLaneRandomSampleGenerator();
        ItemShipment itemShipmentBack = getItemShipmentRandomSampleGenerator();

        shipmentLane.addItemShipment(itemShipmentBack);
        assertThat(shipmentLane.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getShipmentLane()).isEqualTo(shipmentLane);

        shipmentLane.removeItemShipment(itemShipmentBack);
        assertThat(shipmentLane.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getShipmentLane()).isNull();

        shipmentLane.itemShipments(new HashSet<>(Set.of(itemShipmentBack)));
        assertThat(shipmentLane.getItemShipments()).containsOnly(itemShipmentBack);
        assertThat(itemShipmentBack.getShipmentLane()).isEqualTo(shipmentLane);

        shipmentLane.setItemShipments(new HashSet<>());
        assertThat(shipmentLane.getItemShipments()).doesNotContain(itemShipmentBack);
        assertThat(itemShipmentBack.getShipmentLane()).isNull();
    }

    @Test
    void shipmentLegTest() {
        ShipmentLane shipmentLane = getShipmentLaneRandomSampleGenerator();
        ShipmentLeg shipmentLegBack = getShipmentLegRandomSampleGenerator();

        shipmentLane.addShipmentLeg(shipmentLegBack);
        assertThat(shipmentLane.getShipmentLegs()).containsOnly(shipmentLegBack);
        assertThat(shipmentLegBack.getShipmentLane()).isEqualTo(shipmentLane);

        shipmentLane.removeShipmentLeg(shipmentLegBack);
        assertThat(shipmentLane.getShipmentLegs()).doesNotContain(shipmentLegBack);
        assertThat(shipmentLegBack.getShipmentLane()).isNull();

        shipmentLane.shipmentLegs(new HashSet<>(Set.of(shipmentLegBack)));
        assertThat(shipmentLane.getShipmentLegs()).containsOnly(shipmentLegBack);
        assertThat(shipmentLegBack.getShipmentLane()).isEqualTo(shipmentLane);

        shipmentLane.setShipmentLegs(new HashSet<>());
        assertThat(shipmentLane.getShipmentLegs()).doesNotContain(shipmentLegBack);
        assertThat(shipmentLegBack.getShipmentLane()).isNull();
    }
}
