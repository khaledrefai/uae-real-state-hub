package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentPlanItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PaymentPlanItem getPaymentPlanItemSample1() {
        return new PaymentPlanItem().id(1L).orderNo(1).paymentTime("paymentTime1").percentOfPayment("percentOfPayment1");
    }

    public static PaymentPlanItem getPaymentPlanItemSample2() {
        return new PaymentPlanItem().id(2L).orderNo(2).paymentTime("paymentTime2").percentOfPayment("percentOfPayment2");
    }

    public static PaymentPlanItem getPaymentPlanItemRandomSampleGenerator() {
        return new PaymentPlanItem()
            .id(longCount.incrementAndGet())
            .orderNo(intCount.incrementAndGet())
            .paymentTime(UUID.randomUUID().toString())
            .percentOfPayment(UUID.randomUUID().toString());
    }
}
