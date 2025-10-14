package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.PaymentPlanItemAsserts.*;
import static com.yarmook.realstate.domain.PaymentPlanItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentPlanItemMapperTest {

    private PaymentPlanItemMapper paymentPlanItemMapper;

    @BeforeEach
    void setUp() {
        paymentPlanItemMapper = new PaymentPlanItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentPlanItemSample1();
        var actual = paymentPlanItemMapper.toEntity(paymentPlanItemMapper.toDto(expected));
        assertPaymentPlanItemAllPropertiesEquals(expected, actual);
    }
}
