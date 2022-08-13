package com.java.biblioteca.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivroMapperTest {

    private LivroMapper livroMapper;

    @BeforeEach
    public void setUp() {
        livroMapper = new LivroMapperImpl();
    }
}
