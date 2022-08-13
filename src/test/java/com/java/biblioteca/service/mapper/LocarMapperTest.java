package com.java.biblioteca.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocarMapperTest {

    private LocarMapper locarMapper;

    @BeforeEach
    public void setUp() {
        locarMapper = new LocarMapperImpl();
    }
}
