package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Facility getFacilitySample1() {
        return new Facility().id(1L).name("name1").source("source1").imageUrl("imageUrl1");
    }

    public static Facility getFacilitySample2() {
        return new Facility().id(2L).name("name2").source("source2").imageUrl("imageUrl2");
    }

    public static Facility getFacilityRandomSampleGenerator() {
        return new Facility()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString());
    }
}
