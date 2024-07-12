package com.bacuti.domain;

import static com.bacuti.domain.ShipmentLaneTestSamples.*;
import static com.bacuti.domain.ShipmentLegTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bacuti.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShipmentLegTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentLeg.class);
        ShipmentLeg shipmentLeg1 = getShipmentLegSample1();
        ShipmentLeg shipmentLeg2 = new ShipmentLeg();
        assertThat(shipmentLeg1).isNotEqualTo(shipmentLeg2);

        shipmentLeg2.setId(shipmentLeg1.getId());
        assertThat(shipmentLeg1).isEqualTo(shipmentLeg2);

        shipmentLeg2 = getShipmentLegSample2();
        assertThat(shipmentLeg1).isNotEqualTo(shipmentLeg2);
    }

    @Test
    void shipmentLaneTest() {
        ShipmentLeg shipmentLeg = getShipmentLegRandomSampleGenerator();
        ShipmentLane shipmentLaneBack = getShipmentLaneRandomSampleGenerator();

        shipmentLeg.setShipmentLane(shipmentLaneBack);
        assertThat(shipmentLeg.getShipmentLane()).isEqualTo(shipmentLaneBack);

        shipmentLeg.shipmentLane(null);
        assertThat(shipmentLeg.getShipmentLane()).isNull();
    }
}
