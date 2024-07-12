package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .customerName("customerName1")
            .description("description1")
            .website("website1")
            .country("country1")
            .sustainabilityContactName("sustainabilityContactName1")
            .sustainabilityContactEmail("sustainabilityContactEmail1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .customerName("customerName2")
            .description("description2")
            .website("website2")
            .country("country2")
            .sustainabilityContactName("sustainabilityContactName2")
            .sustainabilityContactEmail("sustainabilityContactEmail2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .customerName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .sustainabilityContactName(UUID.randomUUID().toString())
            .sustainabilityContactEmail(UUID.randomUUID().toString());
    }
}
