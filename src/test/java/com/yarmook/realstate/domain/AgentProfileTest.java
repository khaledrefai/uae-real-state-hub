package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.AgentProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentProfile.class);
        AgentProfile agentProfile1 = getAgentProfileSample1();
        AgentProfile agentProfile2 = new AgentProfile();
        assertThat(agentProfile1).isNotEqualTo(agentProfile2);

        agentProfile2.setId(agentProfile1.getId());
        assertThat(agentProfile1).isEqualTo(agentProfile2);

        agentProfile2 = getAgentProfileSample2();
        assertThat(agentProfile1).isNotEqualTo(agentProfile2);
    }
}
