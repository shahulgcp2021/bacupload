package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PurchasedQtyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchasedQty getPurchasedQtySample1() {
        return new PurchasedQty().id(1L);
    }

    public static PurchasedQty getPurchasedQtySample2() {
        return new PurchasedQty().id(2L);
    }

    public static PurchasedQty getPurchasedQtyRandomSampleGenerator() {
        return new PurchasedQty().id(longCount.incrementAndGet());
    }
}
