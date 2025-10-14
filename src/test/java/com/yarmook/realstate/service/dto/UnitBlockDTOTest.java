package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitBlockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitBlockDTO.class);
        UnitBlockDTO unitBlockDTO1 = new UnitBlockDTO();
        unitBlockDTO1.setId(1L);
        UnitBlockDTO unitBlockDTO2 = new UnitBlockDTO();
        assertThat(unitBlockDTO1).isNotEqualTo(unitBlockDTO2);
        unitBlockDTO2.setId(unitBlockDTO1.getId());
        assertThat(unitBlockDTO1).isEqualTo(unitBlockDTO2);
        unitBlockDTO2.setId(2L);
        assertThat(unitBlockDTO1).isNotEqualTo(unitBlockDTO2);
        unitBlockDTO1.setId(null);
        assertThat(unitBlockDTO1).isNotEqualTo(unitBlockDTO2);
    }
}
