package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UnitBlockTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UnitBlock getUnitBlockSample1() {
        return new UnitBlock()
            .id(1L)
            .normalizedType("normalizedType1")
            .unitType("unitType1")
            .bedroomsAmount("bedroomsAmount1")
            .unitBedrooms("unitBedrooms1")
            .areaUnit("areaUnit1")
            .unitsAmount(1)
            .unitsAreaFromM2("unitsAreaFromM21")
            .unitsAreaToM2("unitsAreaToM21")
            .priceCurrency("priceCurrency1")
            .typicalImageUrl("typicalImageUrl1");
    }

    public static UnitBlock getUnitBlockSample2() {
        return new UnitBlock()
            .id(2L)
            .normalizedType("normalizedType2")
            .unitType("unitType2")
            .bedroomsAmount("bedroomsAmount2")
            .unitBedrooms("unitBedrooms2")
            .areaUnit("areaUnit2")
            .unitsAmount(2)
            .unitsAreaFromM2("unitsAreaFromM22")
            .unitsAreaToM2("unitsAreaToM22")
            .priceCurrency("priceCurrency2")
            .typicalImageUrl("typicalImageUrl2");
    }

    public static UnitBlock getUnitBlockRandomSampleGenerator() {
        return new UnitBlock()
            .id(longCount.incrementAndGet())
            .normalizedType(UUID.randomUUID().toString())
            .unitType(UUID.randomUUID().toString())
            .bedroomsAmount(UUID.randomUUID().toString())
            .unitBedrooms(UUID.randomUUID().toString())
            .areaUnit(UUID.randomUUID().toString())
            .unitsAmount(intCount.incrementAndGet())
            .unitsAreaFromM2(UUID.randomUUID().toString())
            .unitsAreaToM2(UUID.randomUUID().toString())
            .priceCurrency(UUID.randomUUID().toString())
            .typicalImageUrl(UUID.randomUUID().toString());
    }
}
