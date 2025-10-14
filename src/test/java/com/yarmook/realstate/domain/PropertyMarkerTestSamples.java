package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PropertyMarkerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PropertyMarker getPropertyMarkerSample1() {
        return new PropertyMarker()
            .id(1L)
            .extId(1L)
            .area("area1")
            .name("name1")
            .developer("developer1")
            .status("status1")
            .saleStatus("saleStatus1")
            .coverUrl("coverUrl1");
    }

    public static PropertyMarker getPropertyMarkerSample2() {
        return new PropertyMarker()
            .id(2L)
            .extId(2L)
            .area("area2")
            .name("name2")
            .developer("developer2")
            .status("status2")
            .saleStatus("saleStatus2")
            .coverUrl("coverUrl2");
    }

    public static PropertyMarker getPropertyMarkerRandomSampleGenerator() {
        return new PropertyMarker()
            .id(longCount.incrementAndGet())
            .extId(longCount.incrementAndGet())
            .area(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .developer(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .saleStatus(UUID.randomUUID().toString())
            .coverUrl(UUID.randomUUID().toString());
    }
}
