package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MapPointTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MapPoint getMapPointSample1() {
        return new MapPoint().id(1L).name("name1");
    }

    public static MapPoint getMapPointSample2() {
        return new MapPoint().id(2L).name("name2");
    }

    public static MapPoint getMapPointRandomSampleGenerator() {
        return new MapPoint().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
