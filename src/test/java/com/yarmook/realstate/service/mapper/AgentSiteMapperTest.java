package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.AgentSiteAsserts.*;
import static com.yarmook.realstate.domain.AgentSiteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentSiteMapperTest {

    private AgentSiteMapper agentSiteMapper;

    @BeforeEach
    void setUp() {
        agentSiteMapper = new AgentSiteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgentSiteSample1();
        var actual = agentSiteMapper.toEntity(agentSiteMapper.toDto(expected));
        assertAgentSiteAllPropertiesEquals(expected, actual);
    }
}
