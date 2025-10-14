package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactLeadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContactLead getContactLeadSample1() {
        return new ContactLead()
            .id(1L)
            .name("name1")
            .email("email1")
            .phone("phone1")
            .whatsapp("whatsapp1")
            .message("message1")
            .source("source1");
    }

    public static ContactLead getContactLeadSample2() {
        return new ContactLead()
            .id(2L)
            .name("name2")
            .email("email2")
            .phone("phone2")
            .whatsapp("whatsapp2")
            .message("message2")
            .source("source2");
    }

    public static ContactLead getContactLeadRandomSampleGenerator() {
        return new ContactLead()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .whatsapp(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString());
    }
}
