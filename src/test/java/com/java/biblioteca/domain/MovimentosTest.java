package com.java.biblioteca.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovimentosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movimentos.class);
        Movimentos movimentos1 = new Movimentos();
        movimentos1.setId(1L);
        Movimentos movimentos2 = new Movimentos();
        movimentos2.setId(movimentos1.getId());
        assertThat(movimentos1).isEqualTo(movimentos2);
        movimentos2.setId(2L);
        assertThat(movimentos1).isNotEqualTo(movimentos2);
        movimentos1.setId(null);
        assertThat(movimentos1).isNotEqualTo(movimentos2);
    }
}
