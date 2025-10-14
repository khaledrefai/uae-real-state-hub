package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.PropertyMarkerAsserts.*;
import static com.yarmook.realstate.domain.PropertyMarkerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyMarkerMapperTest {

    private PropertyMarkerMapper propertyMarkerMapper;

    @BeforeEach
    void setUp() {
        propertyMarkerMapper = new PropertyMarkerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPropertyMarkerSample1();
        var actual = propertyMarkerMapper.toEntity(propertyMarkerMapper.toDto(expected));
        assertPropertyMarkerAllPropertiesEquals(expected, actual);
    }
}
