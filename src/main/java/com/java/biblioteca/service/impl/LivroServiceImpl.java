package com.java.biblioteca.service.impl;

import com.java.biblioteca.domain.Livro;
import com.java.biblioteca.repository.LivroRepository;
import com.java.biblioteca.service.LivroService;
import com.java.biblioteca.service.dto.LivroDTO;
import com.java.biblioteca.service.mapper.LivroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Livro}.
 */
@Service
@Transactional
public class LivroServiceImpl implements LivroService {

    private final Logger log = LoggerFactory.getLogger(LivroServiceImpl.class);

    private final LivroRepository livroRepository;

    private final LivroMapper livroMapper;

    public LivroServiceImpl(LivroRepository livroRepository, LivroMapper livroMapper) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
    }

    @Override
    public LivroDTO save(LivroDTO livroDTO) {
        log.debug("Request to save Livro : {}", livroDTO);
        Livro livro = livroMapper.toEntity(livroDTO);
        livro = livroRepository.save(livro);
        return livroMapper.toDto(livro);
    }

    @Override
    public LivroDTO update(LivroDTO livroDTO) {
        log.debug("Request to save Livro : {}", livroDTO);
        Livro livro = livroMapper.toEntity(livroDTO);
        livro = livroRepository.save(livro);
        return livroMapper.toDto(livro);
    }

    @Override
    public Optional<LivroDTO> partialUpdate(LivroDTO livroDTO) {
        log.debug("Request to partially update Livro : {}", livroDTO);

        return livroRepository
            .findById(livroDTO.getId())
            .map(existingLivro -> {
                livroMapper.partialUpdate(existingLivro, livroDTO);

                return existingLivro;
            })
            .map(livroRepository::save)
            .map(livroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Livros");
        return livroRepository.findAll(pageable).map(livroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LivroDTO> findOne(Long id) {
        log.debug("Request to get Livro : {}", id);
        return livroRepository.findById(id).map(livroMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Livro : {}", id);
        livroRepository.deleteById(id);
    }
}
