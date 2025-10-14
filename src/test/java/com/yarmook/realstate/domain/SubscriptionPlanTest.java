package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.SubscriptionPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionPlan.class);
        SubscriptionPlan subscriptionPlan1 = getSubscriptionPlanSample1();
        SubscriptionPlan subscriptionPlan2 = new SubscriptionPlan();
        assertThat(subscriptionPlan1).isNotEqualTo(subscriptionPlan2);

        subscriptionPlan2.setId(subscriptionPlan1.getId());
        assertThat(subscriptionPlan1).isEqualTo(subscriptionPlan2);

        subscriptionPlan2 = getSubscriptionPlanSample2();
        assertThat(subscriptionPlan1).isNotEqualTo(subscriptionPlan2);
    }
}
