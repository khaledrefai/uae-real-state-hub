package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.FacilityTestSamples.*;
import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facility.class);
        Facility facility1 = getFacilitySample1();
        Facility facility2 = new Facility();
        assertThat(facility1).isNotEqualTo(facility2);

        facility2.setId(facility1.getId());
        assertThat(facility1).isEqualTo(facility2);

        facility2 = getFacilitySample2();
        assertThat(facility1).isNotEqualTo(facility2);
    }

    @Test
    void propertyTest() {
        Facility facility = getFacilityRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        facility.setProperty(propertyBack);
        assertThat(facility.getProperty()).isEqualTo(propertyBack);

        facility.property(null);
        assertThat(facility.getProperty()).isNull();
    }
}
