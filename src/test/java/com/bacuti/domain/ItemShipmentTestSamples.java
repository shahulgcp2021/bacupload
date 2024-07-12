package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ItemShipment getItemShipmentSample1() {
        return new ItemShipment().id(1L).shipper("shipper1");
    }

    public static ItemShipment getItemShipmentSample2() {
        return new ItemShipment().id(2L).shipper("shipper2");
    }

    public static ItemShipment getItemShipmentRandomSampleGenerator() {
        return new ItemShipment().id(longCount.incrementAndGet()).shipper(UUID.randomUUID().toString());
    }
}
