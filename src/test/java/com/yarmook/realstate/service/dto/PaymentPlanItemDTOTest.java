package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentPlanItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentPlanItemDTO.class);
        PaymentPlanItemDTO paymentPlanItemDTO1 = new PaymentPlanItemDTO();
        paymentPlanItemDTO1.setId(1L);
        PaymentPlanItemDTO paymentPlanItemDTO2 = new PaymentPlanItemDTO();
        assertThat(paymentPlanItemDTO1).isNotEqualTo(paymentPlanItemDTO2);
        paymentPlanItemDTO2.setId(paymentPlanItemDTO1.getId());
        assertThat(paymentPlanItemDTO1).isEqualTo(paymentPlanItemDTO2);
        paymentPlanItemDTO2.setId(2L);
        assertThat(paymentPlanItemDTO1).isNotEqualTo(paymentPlanItemDTO2);
        paymentPlanItemDTO1.setId(null);
        assertThat(paymentPlanItemDTO1).isNotEqualTo(paymentPlanItemDTO2);
    }
}
