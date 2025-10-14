package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentSiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgentSite getAgentSiteSample1() {
        return new AgentSite()
            .id(1L)
            .slug("slug1")
            .displayName("displayName1")
            .theme("theme1")
            .primaryColor("primaryColor1")
            .secondaryColor("secondaryColor1")
            .logoUrl("logoUrl1")
            .contactEmail("contactEmail1")
            .contactPhone("contactPhone1")
            .contactWhatsapp("contactWhatsapp1")
            .domain("domain1");
    }

    public static AgentSite getAgentSiteSample2() {
        return new AgentSite()
            .id(2L)
            .slug("slug2")
            .displayName("displayName2")
            .theme("theme2")
            .primaryColor("primaryColor2")
            .secondaryColor("secondaryColor2")
            .logoUrl("logoUrl2")
            .contactEmail("contactEmail2")
            .contactPhone("contactPhone2")
            .contactWhatsapp("contactWhatsapp2")
            .domain("domain2");
    }

    public static AgentSite getAgentSiteRandomSampleGenerator() {
        return new AgentSite()
            .id(longCount.incrementAndGet())
            .slug(UUID.randomUUID().toString())
            .displayName(UUID.randomUUID().toString())
            .theme(UUID.randomUUID().toString())
            .primaryColor(UUID.randomUUID().toString())
            .secondaryColor(UUID.randomUUID().toString())
            .logoUrl(UUID.randomUUID().toString())
            .contactEmail(UUID.randomUUID().toString())
            .contactPhone(UUID.randomUUID().toString())
            .contactWhatsapp(UUID.randomUUID().toString())
            .domain(UUID.randomUUID().toString());
    }
}
