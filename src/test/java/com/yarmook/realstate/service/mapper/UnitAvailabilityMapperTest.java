package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.UnitAvailabilityAsserts.*;
import static com.yarmook.realstate.domain.UnitAvailabilityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitAvailabilityMapperTest {

    private UnitAvailabilityMapper unitAvailabilityMapper;

    @BeforeEach
    void setUp() {
        unitAvailabilityMapper = new UnitAvailabilityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUnitAvailabilitySample1();
        var actual = unitAvailabilityMapper.toEntity(unitAvailabilityMapper.toDto(expected));
        assertUnitAvailabilityAllPropertiesEquals(expected, actual);
    }
}
