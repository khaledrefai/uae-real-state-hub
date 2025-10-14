package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.PaymentPlanTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentPlan.class);
        PaymentPlan paymentPlan1 = getPaymentPlanSample1();
        PaymentPlan paymentPlan2 = new PaymentPlan();
        assertThat(paymentPlan1).isNotEqualTo(paymentPlan2);

        paymentPlan2.setId(paymentPlan1.getId());
        assertThat(paymentPlan1).isEqualTo(paymentPlan2);

        paymentPlan2 = getPaymentPlanSample2();
        assertThat(paymentPlan1).isNotEqualTo(paymentPlan2);
    }

    @Test
    void propertyTest() {
        PaymentPlan paymentPlan = getPaymentPlanRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        paymentPlan.setProperty(propertyBack);
        assertThat(paymentPlan.getProperty()).isEqualTo(propertyBack);

        paymentPlan.property(null);
        assertThat(paymentPlan.getProperty()).isNull();
    }
}
