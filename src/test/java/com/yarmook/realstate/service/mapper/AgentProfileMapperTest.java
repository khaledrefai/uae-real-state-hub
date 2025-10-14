package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.AgentProfileAsserts.*;
import static com.yarmook.realstate.domain.AgentProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentProfileMapperTest {

    private AgentProfileMapper agentProfileMapper;

    @BeforeEach
    void setUp() {
        agentProfileMapper = new AgentProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgentProfileSample1();
        var actual = agentProfileMapper.toEntity(agentProfileMapper.toDto(expected));
        assertAgentProfileAllPropertiesEquals(expected, actual);
    }
}
