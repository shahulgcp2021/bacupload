package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Supplier getSupplierSample1() {
        return new Supplier()
            .id(1L)
            .supplierName("supplierName1")
            .description("description1")
            .category("category1")
            .website("website1")
            .country("country1")
            .sustainabilityContactName("sustainabilityContactName1")
            .sustainabilityContactEmail("sustainabilityContactEmail1");
    }

    public static Supplier getSupplierSample2() {
        return new Supplier()
            .id(2L)
            .supplierName("supplierName2")
            .description("description2")
            .category("category2")
            .website("website2")
            .country("country2")
            .sustainabilityContactName("sustainabilityContactName2")
            .sustainabilityContactEmail("sustainabilityContactEmail2");
    }

    public static Supplier getSupplierRandomSampleGenerator() {
        return new Supplier()
            .id(longCount.incrementAndGet())
            .supplierName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .sustainabilityContactName(UUID.randomUUID().toString())
            .sustainabilityContactEmail(UUID.randomUUID().toString());
    }
}
