package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Site getSiteSample1() {
        return new Site()
            .id(1L)
            .siteName("siteName1")
            .description("description1")
            .employeeCount(1)
            .country("country1")
            .state("state1")
            .address("address1")
            .unlocode("unlocode1")
            .dataQualityDesc("dataQualityDesc1")
            .defaultValueUsageJustfn("defaultValueUsageJustfn1")
            .dataQAInfo("dataQAInfo1")
            .defaultHeatNumber("defaultHeatNumber1");
    }

    public static Site getSiteSample2() {
        return new Site()
            .id(2L)
            .siteName("siteName2")
            .description("description2")
            .employeeCount(2)
            .country("country2")
            .state("state2")
            .address("address2")
            .unlocode("unlocode2")
            .dataQualityDesc("dataQualityDesc2")
            .defaultValueUsageJustfn("defaultValueUsageJustfn2")
            .dataQAInfo("dataQAInfo2")
            .defaultHeatNumber("defaultHeatNumber2");
    }

    public static Site getSiteRandomSampleGenerator() {
        return new Site()
            .id(longCount.incrementAndGet())
            .siteName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .employeeCount(intCount.incrementAndGet())
            .country(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .unlocode(UUID.randomUUID().toString())
            .dataQualityDesc(UUID.randomUUID().toString())
            .defaultValueUsageJustfn(UUID.randomUUID().toString())
            .dataQAInfo(UUID.randomUUID().toString())
            .defaultHeatNumber(UUID.randomUUID().toString());
    }
}
