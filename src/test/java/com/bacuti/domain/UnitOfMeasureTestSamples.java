package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UnitOfMeasureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UnitOfMeasure getUnitOfMeasureSample1() {
        return new UnitOfMeasure().id(1L).name("name1").key("key1").value("value1");
    }

    public static UnitOfMeasure getUnitOfMeasureSample2() {
        return new UnitOfMeasure().id(2L).name("name2").key("key2").value("value2");
    }

    public static UnitOfMeasure getUnitOfMeasureRandomSampleGenerator() {
        return new UnitOfMeasure()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .key(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
