package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static com.yarmook.realstate.domain.UnitAvailabilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitAvailabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitAvailability.class);
        UnitAvailability unitAvailability1 = getUnitAvailabilitySample1();
        UnitAvailability unitAvailability2 = new UnitAvailability();
        assertThat(unitAvailability1).isNotEqualTo(unitAvailability2);

        unitAvailability2.setId(unitAvailability1.getId());
        assertThat(unitAvailability1).isEqualTo(unitAvailability2);

        unitAvailability2 = getUnitAvailabilitySample2();
        assertThat(unitAvailability1).isNotEqualTo(unitAvailability2);
    }

    @Test
    void propertyTest() {
        UnitAvailability unitAvailability = getUnitAvailabilityRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        unitAvailability.setProperty(propertyBack);
        assertThat(unitAvailability.getProperty()).isEqualTo(propertyBack);

        unitAvailability.property(null);
        assertThat(unitAvailability.getProperty()).isNull();
    }
}
