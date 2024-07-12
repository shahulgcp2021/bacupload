package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AggregateEnergyUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AggregateEnergyUsage getAggregateEnergyUsageSample1() {
        return new AggregateEnergyUsage().id(1L);
    }

    public static AggregateEnergyUsage getAggregateEnergyUsageSample2() {
        return new AggregateEnergyUsage().id(2L);
    }

    public static AggregateEnergyUsage getAggregateEnergyUsageRandomSampleGenerator() {
        return new AggregateEnergyUsage().id(longCount.incrementAndGet());
    }
}
