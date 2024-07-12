package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Item getItemSample1() {
        return new Item()
            .id(1L)
            .itemName("itemName1")
            .description("description1")
            .itemCategory("itemCategory1")
            .cnCode("cnCode1")
            .cnName("cnName1");
    }

    public static Item getItemSample2() {
        return new Item()
            .id(2L)
            .itemName("itemName2")
            .description("description2")
            .itemCategory("itemCategory2")
            .cnCode("cnCode2")
            .cnName("cnName2");
    }

    public static Item getItemRandomSampleGenerator() {
        return new Item()
            .id(longCount.incrementAndGet())
            .itemName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .itemCategory(UUID.randomUUID().toString())
            .cnCode(UUID.randomUUID().toString())
            .cnName(UUID.randomUUID().toString());
    }
}
