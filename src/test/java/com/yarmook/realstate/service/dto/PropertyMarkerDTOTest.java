package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropertyMarkerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyMarkerDTO.class);
        PropertyMarkerDTO propertyMarkerDTO1 = new PropertyMarkerDTO();
        propertyMarkerDTO1.setId(1L);
        PropertyMarkerDTO propertyMarkerDTO2 = new PropertyMarkerDTO();
        assertThat(propertyMarkerDTO1).isNotEqualTo(propertyMarkerDTO2);
        propertyMarkerDTO2.setId(propertyMarkerDTO1.getId());
        assertThat(propertyMarkerDTO1).isEqualTo(propertyMarkerDTO2);
        propertyMarkerDTO2.setId(2L);
        assertThat(propertyMarkerDTO1).isNotEqualTo(propertyMarkerDTO2);
        propertyMarkerDTO1.setId(null);
        assertThat(propertyMarkerDTO1).isNotEqualTo(propertyMarkerDTO2);
    }
}
