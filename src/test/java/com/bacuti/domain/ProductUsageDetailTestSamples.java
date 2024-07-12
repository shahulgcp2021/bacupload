package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductUsageDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductUsageDetail getProductUsageDetailSample1() {
        return new ProductUsageDetail().id(1L).detail("detail1");
    }

    public static ProductUsageDetail getProductUsageDetailSample2() {
        return new ProductUsageDetail().id(2L).detail("detail2");
    }

    public static ProductUsageDetail getProductUsageDetailRandomSampleGenerator() {
        return new ProductUsageDetail().id(longCount.incrementAndGet()).detail(UUID.randomUUID().toString());
    }
}
