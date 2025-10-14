package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.FacilityAsserts.*;
import static com.yarmook.realstate.domain.FacilityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacilityMapperTest {

    private FacilityMapper facilityMapper;

    @BeforeEach
    void setUp() {
        facilityMapper = new FacilityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFacilitySample1();
        var actual = facilityMapper.toEntity(facilityMapper.toDto(expected));
        assertFacilityAllPropertiesEquals(expected, actual);
    }
}
