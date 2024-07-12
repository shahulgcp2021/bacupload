package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarbonPricePaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarbonPricePayment getCarbonPricePaymentSample1() {
        return new CarbonPricePayment().id(1L).formOfCarbonPrice("formOfCarbonPrice1").formOfRebate("formOfRebate1");
    }

    public static CarbonPricePayment getCarbonPricePaymentSample2() {
        return new CarbonPricePayment().id(2L).formOfCarbonPrice("formOfCarbonPrice2").formOfRebate("formOfRebate2");
    }

    public static CarbonPricePayment getCarbonPricePaymentRandomSampleGenerator() {
        return new CarbonPricePayment()
            .id(longCount.incrementAndGet())
            .formOfCarbonPrice(UUID.randomUUID().toString())
            .formOfRebate(UUID.randomUUID().toString());
    }
}
