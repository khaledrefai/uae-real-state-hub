package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.UnitBlockAsserts.*;
import static com.yarmook.realstate.domain.UnitBlockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitBlockMapperTest {

    private UnitBlockMapper unitBlockMapper;

    @BeforeEach
    void setUp() {
        unitBlockMapper = new UnitBlockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUnitBlockSample1();
        var actual = unitBlockMapper.toEntity(unitBlockMapper.toDto(expected));
        assertUnitBlockAllPropertiesEquals(expected, actual);
    }
}
