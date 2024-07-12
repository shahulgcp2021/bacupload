package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SiteFinishedGoodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SiteFinishedGood getSiteFinishedGoodSample1() {
        return new SiteFinishedGood().id(1L);
    }

    public static SiteFinishedGood getSiteFinishedGoodSample2() {
        return new SiteFinishedGood().id(2L);
    }

    public static SiteFinishedGood getSiteFinishedGoodRandomSampleGenerator() {
        return new SiteFinishedGood().id(longCount.incrementAndGet());
    }
}
