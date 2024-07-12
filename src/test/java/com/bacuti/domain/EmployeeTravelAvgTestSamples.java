package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTravelAvgTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmployeeTravelAvg getEmployeeTravelAvgSample1() {
        return new EmployeeTravelAvg().id(1L);
    }

    public static EmployeeTravelAvg getEmployeeTravelAvgSample2() {
        return new EmployeeTravelAvg().id(2L);
    }

    public static EmployeeTravelAvg getEmployeeTravelAvgRandomSampleGenerator() {
        return new EmployeeTravelAvg().id(longCount.incrementAndGet());
    }
}
