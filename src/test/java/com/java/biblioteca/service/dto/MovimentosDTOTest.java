package com.java.biblioteca.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovimentosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimentosDTO.class);
        MovimentosDTO movimentosDTO1 = new MovimentosDTO();
        movimentosDTO1.setId(1L);
        MovimentosDTO movimentosDTO2 = new MovimentosDTO();
        assertThat(movimentosDTO1).isNotEqualTo(movimentosDTO2);
        movimentosDTO2.setId(movimentosDTO1.getId());
        assertThat(movimentosDTO1).isEqualTo(movimentosDTO2);
        movimentosDTO2.setId(2L);
        assertThat(movimentosDTO1).isNotEqualTo(movimentosDTO2);
        movimentosDTO1.setId(null);
        assertThat(movimentosDTO1).isNotEqualTo(movimentosDTO2);
    }
}
