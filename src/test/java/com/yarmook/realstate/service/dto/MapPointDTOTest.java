package com.yarmook.realstate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yarmook.realstate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MapPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MapPointDTO.class);
        MapPointDTO mapPointDTO1 = new MapPointDTO();
        mapPointDTO1.setId(1L);
        MapPointDTO mapPointDTO2 = new MapPointDTO();
        assertThat(mapPointDTO1).isNotEqualTo(mapPointDTO2);
        mapPointDTO2.setId(mapPointDTO1.getId());
        assertThat(mapPointDTO1).isEqualTo(mapPointDTO2);
        mapPointDTO2.setId(2L);
        assertThat(mapPointDTO1).isNotEqualTo(mapPointDTO2);
        mapPointDTO1.setId(null);
        assertThat(mapPointDTO1).isNotEqualTo(mapPointDTO2);
    }
}
