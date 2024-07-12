package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnergySourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EnergySource getEnergySourceSample1() {
        return new EnergySource().id(1L).description("description1").supplier("supplier1").sourceForEf("sourceForEf1");
    }

    public static EnergySource getEnergySourceSample2() {
        return new EnergySource().id(2L).description("description2").supplier("supplier2").sourceForEf("sourceForEf2");
    }

    public static EnergySource getEnergySourceRandomSampleGenerator() {
        return new EnergySource()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .supplier(UUID.randomUUID().toString())
            .sourceForEf(UUID.randomUUID().toString());
    }
}
