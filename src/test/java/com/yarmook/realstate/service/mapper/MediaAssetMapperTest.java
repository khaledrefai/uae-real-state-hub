package com.yarmook.realstate.service.mapper;

import static com.yarmook.realstate.domain.MediaAssetAsserts.*;
import static com.yarmook.realstate.domain.MediaAssetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaAssetMapperTest {

    private MediaAssetMapper mediaAssetMapper;

    @BeforeEach
    void setUp() {
        mediaAssetMapper = new MediaAssetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMediaAssetSample1();
        var actual = mediaAssetMapper.toEntity(mediaAssetMapper.toDto(expected));
        assertMediaAssetAllPropertiesEquals(expected, actual);
    }
}
