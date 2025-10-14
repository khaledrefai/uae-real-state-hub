package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.PropertyMarkerTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropertyMarkerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyMarker.class);
        PropertyMarker propertyMarker1 = getPropertyMarkerSample1();
        PropertyMarker propertyMarker2 = new PropertyMarker();
        assertThat(propertyMarker1).isNotEqualTo(propertyMarker2);

        propertyMarker2.setId(propertyMarker1.getId());
        assertThat(propertyMarker1).isEqualTo(propertyMarker2);

        propertyMarker2 = getPropertyMarkerSample2();
        assertThat(propertyMarker1).isNotEqualTo(propertyMarker2);
    }

    @Test
    void propertyTest() {
        PropertyMarker propertyMarker = getPropertyMarkerRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        propertyMarker.setProperty(propertyBack);
        assertThat(propertyMarker.getProperty()).isEqualTo(propertyBack);
        assertThat(propertyBack.getMarker()).isEqualTo(propertyMarker);

        propertyMarker.property(null);
        assertThat(propertyMarker.getProperty()).isNull();
        assertThat(propertyBack.getMarker()).isNull();
    }
}
