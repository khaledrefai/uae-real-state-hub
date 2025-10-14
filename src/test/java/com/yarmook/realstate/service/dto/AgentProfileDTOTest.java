package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentProfileDTO.class);
        AgentProfileDTO agentProfileDTO1 = new AgentProfileDTO();
        agentProfileDTO1.setId(1L);
        AgentProfileDTO agentProfileDTO2 = new AgentProfileDTO();
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
        agentProfileDTO2.setId(agentProfileDTO1.getId());
        assertThat(agentProfileDTO1).isEqualTo(agentProfileDTO2);
        agentProfileDTO2.setId(2L);
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
        agentProfileDTO1.setId(null);
        assertThat(agentProfileDTO1).isNotEqualTo(agentProfileDTO2);
    }
}
