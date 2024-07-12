package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BillofMaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BillofMaterial getBillofMaterialSample1() {
        return new BillofMaterial().id(1L);
    }

    public static BillofMaterial getBillofMaterialSample2() {
        return new BillofMaterial().id(2L);
    }

    public static BillofMaterial getBillofMaterialRandomSampleGenerator() {
        return new BillofMaterial().id(longCount.incrementAndGet());
    }
}
