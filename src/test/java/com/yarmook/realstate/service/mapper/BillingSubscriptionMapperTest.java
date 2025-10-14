package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.BillingSubscriptionAsserts.*;
import static com.yarmook.realstate.domain.BillingSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillingSubscriptionMapperTest {

    private BillingSubscriptionMapper billingSubscriptionMapper;

    @BeforeEach
    void setUp() {
        billingSubscriptionMapper = new BillingSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBillingSubscriptionSample1();
        var actual = billingSubscriptionMapper.toEntity(billingSubscriptionMapper.toDto(expected));
        assertBillingSubscriptionAllPropertiesEquals(expected, actual);
    }
}
