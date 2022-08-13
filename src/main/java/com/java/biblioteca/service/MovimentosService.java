package com.java.biblioteca.service;

import com.java.biblioteca.service.dto.MovimentosDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.java.biblioteca.domain.Movimentos}.
 */
public interface MovimentosService {
    /**
     * Save a movimentos.
     *
     * @param movimentosDTO the entity to save.
     * @return the persisted entity.
     */
    MovimentosDTO save(MovimentosDTO movimentosDTO);

    /**
     * Updates a movimentos.
     *
     * @param movimentosDTO the entity to update.
     * @return the persisted entity.
     */
    MovimentosDTO update(MovimentosDTO movimentosDTO);

    /**
     * Partially updates a movimentos.
     *
     * @param movimentosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MovimentosDTO> partialUpdate(MovimentosDTO movimentosDTO);

    /**
     * Get all the movimentos.
     *
     * @return the list of entities.
     */
    List<MovimentosDTO> findAll();

    /**
     * Get the "id" movimentos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovimentosDTO> findOne(Long id);

    /**
     * Delete the "id" movimentos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
