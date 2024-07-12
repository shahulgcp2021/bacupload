package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductionQtyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductionQty getProductionQtySample1() {
        return new ProductionQty().id(1L).heatNumber("heatNumber1");
    }

    public static ProductionQty getProductionQtySample2() {
        return new ProductionQty().id(2L).heatNumber("heatNumber2");
    }

    public static ProductionQty getProductionQtyRandomSampleGenerator() {
        return new ProductionQty().id(longCount.incrementAndGet()).heatNumber(UUID.randomUUID().toString());
    }
}
