package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactLeadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactLeadDTO.class);
        ContactLeadDTO contactLeadDTO1 = new ContactLeadDTO();
        contactLeadDTO1.setId(1L);
        ContactLeadDTO contactLeadDTO2 = new ContactLeadDTO();
        assertThat(contactLeadDTO1).isNotEqualTo(contactLeadDTO2);
        contactLeadDTO2.setId(contactLeadDTO1.getId());
        assertThat(contactLeadDTO1).isEqualTo(contactLeadDTO2);
        contactLeadDTO2.setId(2L);
        assertThat(contactLeadDTO1).isNotEqualTo(contactLeadDTO2);
        contactLeadDTO1.setId(null);
        assertThat(contactLeadDTO1).isNotEqualTo(contactLeadDTO2);
    }
}
