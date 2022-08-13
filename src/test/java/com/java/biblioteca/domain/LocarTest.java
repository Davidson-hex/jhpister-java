package com.java.biblioteca.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locar.class);
        Locar locar1 = new Locar();
        locar1.setId(1L);
        Locar locar2 = new Locar();
        locar2.setId(locar1.getId());
        assertThat(locar1).isEqualTo(locar2);
        locar2.setId(2L);
        assertThat(locar1).isNotEqualTo(locar2);
        locar1.setId(null);
        assertThat(locar1).isNotEqualTo(locar2);
    }
}
