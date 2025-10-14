package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.MapPointAsserts.*;
import static com.yarmook.realstate.domain.MapPointTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapPointMapperTest {

    private MapPointMapper mapPointMapper;

    @BeforeEach
    void setUp() {
        mapPointMapper = new MapPointMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMapPointSample1();
        var actual = mapPointMapper.toEntity(mapPointMapper.toDto(expected));
        assertMapPointAllPropertiesEquals(expected, actual);
    }
}
