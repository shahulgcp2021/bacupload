package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Company getCompanySample1() {
        return new Company()
            .id(1L)
            .companyName("companyName1")
            .description("description1")
            .industry("industry1")
            .country("country1")
            .admin("admin1");
    }

    public static Company getCompanySample2() {
        return new Company()
            .id(2L)
            .companyName("companyName2")
            .description("description2")
            .industry("industry2")
            .country("country2")
            .admin("admin2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company()
            .id(longCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .industry(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .admin(UUID.randomUUID().toString());
    }
}
