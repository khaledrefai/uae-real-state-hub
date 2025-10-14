package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingSubscriptionDTO.class);
        BillingSubscriptionDTO billingSubscriptionDTO1 = new BillingSubscriptionDTO();
        billingSubscriptionDTO1.setId(1L);
        BillingSubscriptionDTO billingSubscriptionDTO2 = new BillingSubscriptionDTO();
        assertThat(billingSubscriptionDTO1).isNotEqualTo(billingSubscriptionDTO2);
        billingSubscriptionDTO2.setId(billingSubscriptionDTO1.getId());
        assertThat(billingSubscriptionDTO1).isEqualTo(billingSubscriptionDTO2);
        billingSubscriptionDTO2.setId(2L);
        assertThat(billingSubscriptionDTO1).isNotEqualTo(billingSubscriptionDTO2);
        billingSubscriptionDTO1.setId(null);
        assertThat(billingSubscriptionDTO1).isNotEqualTo(billingSubscriptionDTO2);
    }
}
