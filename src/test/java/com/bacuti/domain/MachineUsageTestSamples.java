package com.bacuti.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MachineUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MachineUsage getMachineUsageSample1() {
        return new MachineUsage().id(1L);
    }

    public static MachineUsage getMachineUsageSample2() {
        return new MachineUsage().id(2L);
    }

    public static MachineUsage getMachineUsageRandomSampleGenerator() {
        return new MachineUsage().id(longCount.incrementAndGet());
    }
}
