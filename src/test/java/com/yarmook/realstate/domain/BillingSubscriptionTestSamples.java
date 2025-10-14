package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BillingSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BillingSubscription getBillingSubscriptionSample1() {
        return new BillingSubscription()
            .id(1L)
            .status("status1")
            .stripeCustomerId("stripeCustomerId1")
            .stripeSubscriptionId("stripeSubscriptionId1");
    }

    public static BillingSubscription getBillingSubscriptionSample2() {
        return new BillingSubscription()
            .id(2L)
            .status("status2")
            .stripeCustomerId("stripeCustomerId2")
            .stripeSubscriptionId("stripeSubscriptionId2");
    }

    public static BillingSubscription getBillingSubscriptionRandomSampleGenerator() {
        return new BillingSubscription()
            .id(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .stripeCustomerId(UUID.randomUUID().toString())
            .stripeSubscriptionId(UUID.randomUUID().toString());
    }
}
