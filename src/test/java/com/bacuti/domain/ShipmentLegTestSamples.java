package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShipmentLegTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShipmentLeg getShipmentLegSample1() {
        return new ShipmentLeg().id(1L).segment(1L).carrier("carrier1").fromIata("fromIata1").toIata("toIata1").efSource("efSource1");
    }

    public static ShipmentLeg getShipmentLegSample2() {
        return new ShipmentLeg().id(2L).segment(2L).carrier("carrier2").fromIata("fromIata2").toIata("toIata2").efSource("efSource2");
    }

    public static ShipmentLeg getShipmentLegRandomSampleGenerator() {
        return new ShipmentLeg()
            .id(longCount.incrementAndGet())
            .segment(longCount.incrementAndGet())
            .carrier(UUID.randomUUID().toString())
            .fromIata(UUID.randomUUID().toString())
            .toIata(UUID.randomUUID().toString())
            .efSource(UUID.randomUUID().toString());
    }
}
