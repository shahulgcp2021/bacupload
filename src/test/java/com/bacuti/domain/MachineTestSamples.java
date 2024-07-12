package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MachineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Machine getMachineSample1() {
        return new Machine().id(1L).machineName("machineName1").description("description1");
    }

    public static Machine getMachineSample2() {
        return new Machine().id(2L).machineName("machineName2").description("description2");
    }

    public static Machine getMachineRandomSampleGenerator() {
        return new Machine()
            .id(longCount.incrementAndGet())
            .machineName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
