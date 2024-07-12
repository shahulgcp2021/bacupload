package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentLaneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentLane getShipmentLaneSample1() {
        return new ShipmentLane().id(1L).lane("lane1").description("description1");
    }

    public static ShipmentLane getShipmentLaneSample2() {
        return new ShipmentLane().id(2L).lane("lane2").description("description2");
    }

    public static ShipmentLane getShipmentLaneRandomSampleGenerator() {
        return new ShipmentLane()
            .id(longCount.incrementAndGet())
            .lane(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
