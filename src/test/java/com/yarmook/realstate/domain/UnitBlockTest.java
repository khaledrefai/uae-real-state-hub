package com.yarmook.realstate.domain;

import static com.yarmook.realstate.domain.PropertyTestSamples.*;
import static com.yarmook.realstate.domain.UnitBlockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitBlockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitBlock.class);
        UnitBlock unitBlock1 = getUnitBlockSample1();
        UnitBlock unitBlock2 = new UnitBlock();
        assertThat(unitBlock1).isNotEqualTo(unitBlock2);

        unitBlock2.setId(unitBlock1.getId());
        assertThat(unitBlock1).isEqualTo(unitBlock2);

        unitBlock2 = getUnitBlockSample2();
        assertThat(unitBlock1).isNotEqualTo(unitBlock2);
    }

    @Test
    void propertyTest() {
        UnitBlock unitBlock = getUnitBlockRandomSampleGenerator();
        Property propertyBack = getPropertyRandomSampleGenerator();

        unitBlock.setProperty(propertyBack);
        assertThat(unitBlock.getProperty()).isEqualTo(propertyBack);

        unitBlock.property(null);
        assertThat(unitBlock.getProperty()).isNull();
    }
}
