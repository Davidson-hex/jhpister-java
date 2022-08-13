package com.java.biblioteca.service.impl;

import com.java.biblioteca.domain.Locar;
import com.java.biblioteca.repository.LocarRepository;
import com.java.biblioteca.service.LocarService;
import com.java.biblioteca.service.dto.LocarDTO;
import com.java.biblioteca.service.mapper.LocarMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Locar}.
 */
@Service
@Transactional
public class LocarServiceImpl implements LocarService {

    private final Logger log = LoggerFactory.getLogger(LocarServiceImpl.class);

    private final LocarRepository locarRepository;

    private final LocarMapper locarMapper;

    public LocarServiceImpl(LocarRepository locarRepository, LocarMapper locarMapper) {
        this.locarRepository = locarRepository;
        this.locarMapper = locarMapper;
    }

    @Override
    public LocarDTO save(LocarDTO locarDTO) {
        log.debug("Request to save Locar : {}", locarDTO);
        Locar locar = locarMapper.toEntity(locarDTO);
        locar = locarRepository.save(locar);
        return locarMapper.toDto(locar);
    }

    @Override
    public LocarDTO update(LocarDTO locarDTO) {
        log.debug("Request to save Locar : {}", locarDTO);
        Locar locar = locarMapper.toEntity(locarDTO);
        locar = locarRepository.save(locar);
        return locarMapper.toDto(locar);
    }

    @Override
    public Optional<LocarDTO> partialUpdate(LocarDTO locarDTO) {
        log.debug("Request to partially update Locar : {}", locarDTO);

        return locarRepository
            .findById(locarDTO.getId())
            .map(existingLocar -> {
                locarMapper.partialUpdate(existingLocar, locarDTO);

                return existingLocar;
            })
            .map(locarRepository::save)
            .map(locarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocarDTO> findAll() {
        log.debug("Request to get all Locars");
        return locarRepository.findAll().stream().map(locarMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocarDTO> findOne(Long id) {
        log.debug("Request to get Locar : {}", id);
        return locarRepository.findById(id).map(locarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Locar : {}", id);
        locarRepository.deleteById(id);
    }
}
