package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgentProfile getAgentProfileSample1() {
        return new AgentProfile()
            .id(1L)
            .externalUserId("externalUserId1")
            .fullName("fullName1")
            .companyName("companyName1")
            .email("email1")
            .phone("phone1")
            .whatsapp("whatsapp1")
            .country("country1")
            .city("city1")
            .website("website1");
    }

    public static AgentProfile getAgentProfileSample2() {
        return new AgentProfile()
            .id(2L)
            .externalUserId("externalUserId2")
            .fullName("fullName2")
            .companyName("companyName2")
            .email("email2")
            .phone("phone2")
            .whatsapp("whatsapp2")
            .country("country2")
            .city("city2")
            .website("website2");
    }

    public static AgentProfile getAgentProfileRandomSampleGenerator() {
        return new AgentProfile()
            .id(longCount.incrementAndGet())
            .externalUserId(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .companyName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .whatsapp(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
