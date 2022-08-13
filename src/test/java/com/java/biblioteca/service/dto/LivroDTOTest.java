package com.java.biblioteca.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.java.biblioteca.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivroDTO.class);
        LivroDTO livroDTO1 = new LivroDTO();
        livroDTO1.setId(1L);
        LivroDTO livroDTO2 = new LivroDTO();
        assertThat(livroDTO1).isNotEqualTo(livroDTO2);
        livroDTO2.setId(livroDTO1.getId());
        assertThat(livroDTO1).isEqualTo(livroDTO2);
        livroDTO2.setId(2L);
        assertThat(livroDTO1).isNotEqualTo(livroDTO2);
        livroDTO1.setId(null);
        assertThat(livroDTO1).isNotEqualTo(livroDTO2);
    }
}
