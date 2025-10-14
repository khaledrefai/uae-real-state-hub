package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.AgentSiteTestSamples.*;
import static com.yarmook.realstate.domain.BillingSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingSubscription.class);
        BillingSubscription billingSubscription1 = getBillingSubscriptionSample1();
        BillingSubscription billingSubscription2 = new BillingSubscription();
        assertThat(billingSubscription1).isNotEqualTo(billingSubscription2);

        billingSubscription2.setId(billingSubscription1.getId());
        assertThat(billingSubscription1).isEqualTo(billingSubscription2);

        billingSubscription2 = getBillingSubscriptionSample2();
        assertThat(billingSubscription1).isNotEqualTo(billingSubscription2);
    }

    @Test
    void siteTest() {
        BillingSubscription billingSubscription = getBillingSubscriptionRandomSampleGenerator();
        AgentSite agentSiteBack = getAgentSiteRandomSampleGenerator();

        billingSubscription.setSite(agentSiteBack);
        assertThat(billingSubscription.getSite()).isEqualTo(agentSiteBack);

        billingSubscription.site(null);
        assertThat(billingSubscription.getSite()).isNull();
    }
}
