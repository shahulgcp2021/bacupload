package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WastedisposalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Wastedisposal getWastedisposalSample1() {
        return new Wastedisposal().id(1L).wasteComponent("wasteComponent1");
    }

    public static Wastedisposal getWastedisposalSample2() {
        return new Wastedisposal().id(2L).wasteComponent("wasteComponent2");
    }

    public static Wastedisposal getWastedisposalRandomSampleGenerator() {
        return new Wastedisposal().id(longCount.incrementAndGet()).wasteComponent(UUID.randomUUID().toString());
    }
}
