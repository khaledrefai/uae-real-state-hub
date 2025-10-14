package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.ContactLeadAsserts.*;
import static com.yarmook.realstate.domain.ContactLeadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactLeadMapperTest {

    private ContactLeadMapper contactLeadMapper;

    @BeforeEach
    void setUp() {
        contactLeadMapper = new ContactLeadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContactLeadSample1();
        var actual = contactLeadMapper.toEntity(contactLeadMapper.toDto(expected));
        assertContactLeadAllPropertiesEquals(expected, actual);
    }
}
