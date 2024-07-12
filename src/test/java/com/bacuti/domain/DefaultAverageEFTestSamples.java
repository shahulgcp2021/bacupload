package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultAverageEFTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DefaultAverageEF getDefaultAverageEFSample1() {
        return new DefaultAverageEF()
            .id(1L)
            .domain("domain1")
            .detail("detail1")
            .countryOrRegion("countryOrRegion1")
            .efSource("efSource1")
            .code("code1")
            .codeType("codeType1");
    }

    public static DefaultAverageEF getDefaultAverageEFSample2() {
        return new DefaultAverageEF()
            .id(2L)
            .domain("domain2")
            .detail("detail2")
            .countryOrRegion("countryOrRegion2")
            .efSource("efSource2")
            .code("code2")
            .codeType("codeType2");
    }

    public static DefaultAverageEF getDefaultAverageEFRandomSampleGenerator() {
        return new DefaultAverageEF()
            .id(longCount.incrementAndGet())
            .domain(UUID.randomUUID().toString())
            .detail(UUID.randomUUID().toString())
            .countryOrRegion(UUID.randomUUID().toString())
            .efSource(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .codeType(UUID.randomUUID().toString());
    }
}
