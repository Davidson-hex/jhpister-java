package com.java.biblioteca.service.mapper;

import com.java.biblioteca.domain.Movimentos;
import com.java.biblioteca.service.dto.MovimentosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Movimentos} and its DTO {@link MovimentosDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovimentosMapper extends EntityMapper<MovimentosDTO, Movimentos> {}
