package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentSiteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentSiteDTO.class);
        AgentSiteDTO agentSiteDTO1 = new AgentSiteDTO();
        agentSiteDTO1.setId(1L);
        AgentSiteDTO agentSiteDTO2 = new AgentSiteDTO();
        assertThat(agentSiteDTO1).isNotEqualTo(agentSiteDTO2);
        agentSiteDTO2.setId(agentSiteDTO1.getId());
        assertThat(agentSiteDTO1).isEqualTo(agentSiteDTO2);
        agentSiteDTO2.setId(2L);
        assertThat(agentSiteDTO1).isNotEqualTo(agentSiteDTO2);
        agentSiteDTO1.setId(null);
        assertThat(agentSiteDTO1).isNotEqualTo(agentSiteDTO2);
    }
}
