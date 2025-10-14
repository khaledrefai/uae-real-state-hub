package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitAvailabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitAvailabilityDTO.class);
        UnitAvailabilityDTO unitAvailabilityDTO1 = new UnitAvailabilityDTO();
        unitAvailabilityDTO1.setId(1L);
        UnitAvailabilityDTO unitAvailabilityDTO2 = new UnitAvailabilityDTO();
        assertThat(unitAvailabilityDTO1).isNotEqualTo(unitAvailabilityDTO2);
        unitAvailabilityDTO2.setId(unitAvailabilityDTO1.getId());
        assertThat(unitAvailabilityDTO1).isEqualTo(unitAvailabilityDTO2);
        unitAvailabilityDTO2.setId(2L);
        assertThat(unitAvailabilityDTO1).isNotEqualTo(unitAvailabilityDTO2);
        unitAvailabilityDTO1.setId(null);
        assertThat(unitAvailabilityDTO1).isNotEqualTo(unitAvailabilityDTO2);
    }
}
