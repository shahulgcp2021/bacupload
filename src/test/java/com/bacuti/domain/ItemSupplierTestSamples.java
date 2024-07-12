package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemSupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ItemSupplier getItemSupplierSample1() {
        return new ItemSupplier().id(1L).supplierOwnItem("supplierOwnItem1");
    }

    public static ItemSupplier getItemSupplierSample2() {
        return new ItemSupplier().id(2L).supplierOwnItem("supplierOwnItem2");
    }

    public static ItemSupplier getItemSupplierRandomSampleGenerator() {
        return new ItemSupplier().id(longCount.incrementAndGet()).supplierOwnItem(UUID.randomUUID().toString());
    }
}
