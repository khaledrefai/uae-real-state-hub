package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UnitAvailabilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UnitAvailability getUnitAvailabilitySample1() {
        return new UnitAvailability()
            .id(1L)
            .buildingName("buildingName1")
            .areaUnit("areaUnit1")
            .bedroomType("bedroomType1")
            .priceCurrency("priceCurrency1")
            .unitsAvailable(1);
    }

    public static UnitAvailability getUnitAvailabilitySample2() {
        return new UnitAvailability()
            .id(2L)
            .buildingName("buildingName2")
            .areaUnit("areaUnit2")
            .bedroomType("bedroomType2")
            .priceCurrency("priceCurrency2")
            .unitsAvailable(2);
    }

    public static UnitAvailability getUnitAvailabilityRandomSampleGenerator() {
        return new UnitAvailability()
            .id(longCount.incrementAndGet())
            .buildingName(UUID.randomUUID().toString())
            .areaUnit(UUID.randomUUID().toString())
            .bedroomType(UUID.randomUUID().toString())
            .priceCurrency(UUID.randomUUID().toString())
            .unitsAvailable(intCount.incrementAndGet());
    }
}
