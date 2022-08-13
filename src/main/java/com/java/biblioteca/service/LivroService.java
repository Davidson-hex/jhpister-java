package com.java.biblioteca.service;

import com.java.biblioteca.service.dto.LivroDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.java.biblioteca.domain.Livro}.
 */
public interface LivroService {
    /**
     * Save a livro.
     *
     * @param livroDTO the entity to save.
     * @return the persisted entity.
     */
    LivroDTO save(LivroDTO livroDTO);

    /**
     * Updates a livro.
     *
     * @param livroDTO the entity to update.
     * @return the persisted entity.
     */
    LivroDTO update(LivroDTO livroDTO);

    /**
     * Partially updates a livro.
     *
     * @param livroDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LivroDTO> partialUpdate(LivroDTO livroDTO);

    /**
     * Get all the livros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LivroDTO> findAll(Pageable pageable);

    /**
     * Get the "id" livro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LivroDTO> findOne(Long id);

    /**
     * Delete the "id" livro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
