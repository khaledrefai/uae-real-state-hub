package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.MediaAssetTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaAssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediaAsset.class);
        MediaAsset mediaAsset1 = getMediaAssetSample1();
        MediaAsset mediaAsset2 = new MediaAsset();
        assertThat(mediaAsset1).isNotEqualTo(mediaAsset2);

        mediaAsset2.setId(mediaAsset1.getId());
        assertThat(mediaAsset1).isEqualTo(mediaAsset2);

        mediaAsset2 = getMediaAssetSample2();
        assertThat(mediaAsset1).isNotEqualTo(mediaAsset2);
    }

    @Test
    void propertyTest() {
        MediaAsset mediaAsset = getMediaAssetRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        mediaAsset.setProperty(propertyBack);
        assertThat(mediaAsset.getProperty()).isEqualTo(propertyBack);

        mediaAsset.property(null);
        assertThat(mediaAsset.getProperty()).isNull();
    }
}
