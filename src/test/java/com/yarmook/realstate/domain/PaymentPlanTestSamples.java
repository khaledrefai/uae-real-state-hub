package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PaymentPlan getPaymentPlanSample1() {
        return new PaymentPlan().id(1L).name("name1").monthsAfterHandover(1);
    }

    public static PaymentPlan getPaymentPlanSample2() {
        return new PaymentPlan().id(2L).name("name2").monthsAfterHandover(2);
    }

    public static PaymentPlan getPaymentPlanRandomSampleGenerator() {
        return new PaymentPlan()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .monthsAfterHandover(intCount.incrementAndGet());
    }
}
