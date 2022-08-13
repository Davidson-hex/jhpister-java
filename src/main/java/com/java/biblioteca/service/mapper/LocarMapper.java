package com.java.biblioteca.service.mapper;

import com.java.biblioteca.domain.Locar;
import com.java.biblioteca.service.dto.LocarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Locar} and its DTO {@link LocarDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocarMapper extends EntityMapper<LocarDTO, Locar> {}
