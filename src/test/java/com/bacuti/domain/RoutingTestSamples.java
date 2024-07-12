package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RoutingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Routing getRoutingSample1() {
        return new Routing().id(1L);
    }

    public static Routing getRoutingSample2() {
        return new Routing().id(2L);
    }

    public static Routing getRoutingRandomSampleGenerator() {
        return new Routing().id(longCount.incrementAndGet());
    }
}
