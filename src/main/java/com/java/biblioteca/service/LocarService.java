package com.java.biblioteca.service;

import com.java.biblioteca.service.dto.LocarDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.java.biblioteca.domain.Locar}.
 */
public interface LocarService {
    /**
     * Save a locar.
     *
     * @param locarDTO the entity to save.
     * @return the persisted entity.
     */
    LocarDTO save(LocarDTO locarDTO);

    /**
     * Updates a locar.
     *
     * @param locarDTO the entity to update.
     * @return the persisted entity.
     */
    LocarDTO update(LocarDTO locarDTO);

    /**
     * Partially updates a locar.
     *
     * @param locarDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocarDTO> partialUpdate(LocarDTO locarDTO);

    /**
     * Get all the locars.
     *
     * @return the list of entities.
     */
    List<LocarDTO> findAll();

    /**
     * Get the "id" locar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocarDTO> findOne(Long id);

    /**
     * Delete the "id" locar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
