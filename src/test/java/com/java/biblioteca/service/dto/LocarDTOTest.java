package com.java.biblioteca.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocarDTO.class);
        LocarDTO locarDTO1 = new LocarDTO();
        locarDTO1.setId(1L);
        LocarDTO locarDTO2 = new LocarDTO();
        assertThat(locarDTO1).isNotEqualTo(locarDTO2);
        locarDTO2.setId(locarDTO1.getId());
        assertThat(locarDTO1).isEqualTo(locarDTO2);
        locarDTO2.setId(2L);
        assertThat(locarDTO1).isNotEqualTo(locarDTO2);
        locarDTO1.setId(null);
        assertThat(locarDTO1).isNotEqualTo(locarDTO2);
    }
}
