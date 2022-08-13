package com.java.biblioteca.service.impl;

import com.java.biblioteca.domain.Movimentos;
import com.java.biblioteca.repository.MovimentosRepository;
import com.java.biblioteca.service.MovimentosService;
import com.java.biblioteca.service.dto.MovimentosDTO;
import com.java.biblioteca.service.mapper.MovimentosMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Movimentos}.
 */
@Service
@Transactional
public class MovimentosServiceImpl implements MovimentosService {

    private final Logger log = LoggerFactory.getLogger(MovimentosServiceImpl.class);

    private final MovimentosRepository movimentosRepository;

    private final MovimentosMapper movimentosMapper;

    public MovimentosServiceImpl(MovimentosRepository movimentosRepository, MovimentosMapper movimentosMapper) {
        this.movimentosRepository = movimentosRepository;
        this.movimentosMapper = movimentosMapper;
    }

    @Override
    public MovimentosDTO save(MovimentosDTO movimentosDTO) {
        log.debug("Request to save Movimentos : {}", movimentosDTO);
        Movimentos movimentos = movimentosMapper.toEntity(movimentosDTO);
        movimentos = movimentosRepository.save(movimentos);
        return movimentosMapper.toDto(movimentos);
    }

    @Override
    public MovimentosDTO update(MovimentosDTO movimentosDTO) {
        log.debug("Request to save Movimentos : {}", movimentosDTO);
        Movimentos movimentos = movimentosMapper.toEntity(movimentosDTO);
        movimentos = movimentosRepository.save(movimentos);
        return movimentosMapper.toDto(movimentos);
    }

    @Override
    public Optional<MovimentosDTO> partialUpdate(MovimentosDTO movimentosDTO) {
        log.debug("Request to partially update Movimentos : {}", movimentosDTO);

        return movimentosRepository
            .findById(movimentosDTO.getId())
            .map(existingMovimentos -> {
                movimentosMapper.partialUpdate(existingMovimentos, movimentosDTO);

                return existingMovimentos;
            })
            .map(movimentosRepository::save)
            .map(movimentosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentosDTO> findAll() {
        log.debug("Request to get all Movimentos");
        return movimentosRepository.findAll().stream().map(movimentosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimentosDTO> findOne(Long id) {
        log.debug("Request to get Movimentos : {}", id);
        return movimentosRepository.findById(id).map(movimentosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Movimentos : {}", id);
        movimentosRepository.deleteById(id);
    }
}
