package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionPlan getSubscriptionPlanSample1() {
        return new SubscriptionPlan()
            .id(1L)
            .code("code1")
            .name("name1")
            .stripePriceMonthlyId("stripePriceMonthlyId1")
            .stripePriceYearlyId("stripePriceYearlyId1")
            .maxListings(1);
    }

    public static SubscriptionPlan getSubscriptionPlanSample2() {
        return new SubscriptionPlan()
            .id(2L)
            .code("code2")
            .name("name2")
            .stripePriceMonthlyId("stripePriceMonthlyId2")
            .stripePriceYearlyId("stripePriceYearlyId2")
            .maxListings(2);
    }

    public static SubscriptionPlan getSubscriptionPlanRandomSampleGenerator() {
        return new SubscriptionPlan()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .stripePriceMonthlyId(UUID.randomUUID().toString())
            .stripePriceYearlyId(UUID.randomUUID().toString())
            .maxListings(intCount.incrementAndGet());
    }
}
