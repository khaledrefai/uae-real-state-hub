package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.PaymentPlanAsserts.*;
import static com.yarmook.realstate.domain.PaymentPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentPlanMapperTest {

    private PaymentPlanMapper paymentPlanMapper;

    @BeforeEach
    void setUp() {
        paymentPlanMapper = new PaymentPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentPlanSample1();
        var actual = paymentPlanMapper.toEntity(paymentPlanMapper.toDto(expected));
        assertPaymentPlanAllPropertiesEquals(expected, actual);
    }
}
