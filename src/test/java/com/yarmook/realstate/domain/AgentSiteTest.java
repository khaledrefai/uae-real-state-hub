package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.AgentProfileTestSamples.*;
import static com.yarmook.realstate.domain.AgentSiteTestSamples.*;
import static com.yarmook.realstate.domain.SubscriptionPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentSiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentSite.class);
        AgentSite agentSite1 = getAgentSiteSample1();
        AgentSite agentSite2 = new AgentSite();
        assertThat(agentSite1).isNotEqualTo(agentSite2);

        agentSite2.setId(agentSite1.getId());
        assertThat(agentSite1).isEqualTo(agentSite2);

        agentSite2 = getAgentSiteSample2();
        assertThat(agentSite1).isNotEqualTo(agentSite2);
    }

    @Test
    void ownerTest() {
        AgentSite agentSite = getAgentSiteRandomSampleGenerator();
        AgentProfile agentProfileBack = getAgentProfileRandomSampleGenerator();

        agentSite.setOwner(agentProfileBack);
        assertThat(agentSite.getOwner()).isEqualTo(agentProfileBack);

        agentSite.owner(null);
        assertThat(agentSite.getOwner()).isNull();
    }

    @Test
    void planTest() {
        AgentSite agentSite = getAgentSiteRandomSampleGenerator();
        SubscriptionPlan subscriptionPlanBack = getSubscriptionPlanRandomSampleGenerator();

        agentSite.setPlan(subscriptionPlanBack);
        assertThat(agentSite.getPlan()).isEqualTo(subscriptionPlanBack);

        agentSite.plan(null);
        assertThat(agentSite.getPlan()).isNull();
    }
}
