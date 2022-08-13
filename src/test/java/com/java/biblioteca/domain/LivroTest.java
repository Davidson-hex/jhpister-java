package com.java.biblioteca.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livro.class);
        Livro livro1 = new Livro();
        livro1.setId(1L);
        Livro livro2 = new Livro();
        livro2.setId(livro1.getId());
        assertThat(livro1).isEqualTo(livro2);
        livro2.setId(2L);
        assertThat(livro1).isNotEqualTo(livro2);
        livro1.setId(null);
        assertThat(livro1).isNotEqualTo(livro2);
    }
}
