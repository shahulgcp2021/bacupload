package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CapitalGoodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CapitalGood getCapitalGoodSample1() {
        return new CapitalGood().id(1L).assetName("assetName1").description("description1").supplier("supplier1");
    }

    public static CapitalGood getCapitalGoodSample2() {
        return new CapitalGood().id(2L).assetName("assetName2").description("description2").supplier("supplier2");
    }

    public static CapitalGood getCapitalGoodRandomSampleGenerator() {
        return new CapitalGood()
            .id(longCount.incrementAndGet())
            .assetName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .supplier(UUID.randomUUID().toString());
    }
}
