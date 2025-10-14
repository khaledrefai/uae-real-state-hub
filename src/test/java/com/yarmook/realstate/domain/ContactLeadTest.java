package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.AgentSiteTestSamples.*;
import static com.yarmook.realstate.domain.ContactLeadTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactLeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactLead.class);
        ContactLead contactLead1 = getContactLeadSample1();
        ContactLead contactLead2 = new ContactLead();
        assertThat(contactLead1).isNotEqualTo(contactLead2);

        contactLead2.setId(contactLead1.getId());
        assertThat(contactLead1).isEqualTo(contactLead2);

        contactLead2 = getContactLeadSample2();
        assertThat(contactLead1).isNotEqualTo(contactLead2);
    }

    @Test
    void siteTest() {
        ContactLead contactLead = getContactLeadRandomSampleGenerator();
        AgentSite agentSiteBack = getAgentSiteRandomSampleGenerator();

        contactLead.setSite(agentSiteBack);
        assertThat(contactLead.getSite()).isEqualTo(agentSiteBack);

        contactLead.site(null);
        assertThat(contactLead.getSite()).isNull();
    }

    @Test
    void propertyTest() {
        ContactLead contactLead = getContactLeadRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        contactLead.setProperty(propertyBack);
        assertThat(contactLead.getProperty()).isEqualTo(propertyBack);

        contactLead.property(null);
        assertThat(contactLead.getProperty()).isNull();
    }
}
