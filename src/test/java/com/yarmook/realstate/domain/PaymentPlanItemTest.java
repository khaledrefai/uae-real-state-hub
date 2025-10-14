package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.PaymentPlanItemTestSamples.*;
import static com.yarmook.realstate.domain.PaymentPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentPlanItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentPlanItem.class);
        PaymentPlanItem paymentPlanItem1 = getPaymentPlanItemSample1();
        PaymentPlanItem paymentPlanItem2 = new PaymentPlanItem();
        assertThat(paymentPlanItem1).isNotEqualTo(paymentPlanItem2);

        paymentPlanItem2.setId(paymentPlanItem1.getId());
        assertThat(paymentPlanItem1).isEqualTo(paymentPlanItem2);

        paymentPlanItem2 = getPaymentPlanItemSample2();
        assertThat(paymentPlanItem1).isNotEqualTo(paymentPlanItem2);
    }

    @Test
    void planTest() {
        PaymentPlanItem paymentPlanItem = getPaymentPlanItemRandomSampleGenerator();
        PaymentPlan paymentPlanBack = getPaymentPlanRandomSampleGenerator();

        paymentPlanItem.setPlan(paymentPlanBack);
        assertThat(paymentPlanItem.getPlan()).isEqualTo(paymentPlanBack);

        paymentPlanItem.plan(null);
        assertThat(paymentPlanItem.getPlan()).isNull();
    }
}
