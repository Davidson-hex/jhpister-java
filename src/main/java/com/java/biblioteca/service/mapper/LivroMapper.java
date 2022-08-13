package com.java.biblioteca.service.mapper;

import com.java.biblioteca.domain.Livro;
import com.java.biblioteca.service.dto.LivroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livro} and its DTO {@link LivroDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivroMapper extends EntityMapper<LivroDTO, Livro> {}
