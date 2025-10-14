package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.MapPointTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MapPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MapPoint.class);
        MapPoint mapPoint1 = getMapPointSample1();
        MapPoint mapPoint2 = new MapPoint();
        assertThat(mapPoint1).isNotEqualTo(mapPoint2);

        mapPoint2.setId(mapPoint1.getId());
        assertThat(mapPoint1).isEqualTo(mapPoint2);

        mapPoint2 = getMapPointSample2();
        assertThat(mapPoint1).isNotEqualTo(mapPoint2);
    }

    @Test
    void propertyTest() {
        MapPoint mapPoint = getMapPointRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        mapPoint.setProperty(propertyBack);
        assertThat(mapPoint.getProperty()).isEqualTo(propertyBack);

        mapPoint.property(null);
        assertThat(mapPoint.getProperty()).isNull();
    }
}
