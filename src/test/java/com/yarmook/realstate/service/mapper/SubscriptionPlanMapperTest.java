package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.SubscriptionPlanAsserts.*;
import static com.yarmook.realstate.domain.SubscriptionPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionPlanMapperTest {

    private SubscriptionPlanMapper subscriptionPlanMapper;

    @BeforeEach
    void setUp() {
        subscriptionPlanMapper = new SubscriptionPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscriptionPlanSample1();
        var actual = subscriptionPlanMapper.toEntity(subscriptionPlanMapper.toDto(expected));
        assertSubscriptionPlanAllPropertiesEquals(expected, actual);
    }
}
