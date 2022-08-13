package com.java.biblioteca.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovimentosMapperTest {

    private MovimentosMapper movimentosMapper;

    @BeforeEach
    public void setUp() {
        movimentosMapper = new MovimentosMapperImpl();
    }
}
