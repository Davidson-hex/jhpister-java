package com.java.biblioteca.web.rest;

import com.java.biblioteca.repository.LocarRepository;
import com.java.biblioteca.service.LocarService;
import com.java.biblioteca.service.dto.LocarDTO;
import com.java.biblioteca.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.java.biblioteca.domain.Locar}.
 */
@RestController
@RequestMapping("/api")
public class LocarResource {

    private final Logger log = LoggerFactory.getLogger(LocarResource.class);

    private static final String ENTITY_NAME = "locar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocarService locarService;

    private final LocarRepository locarRepository;

    public LocarResource(LocarService locarService, LocarRepository locarRepository) {
        this.locarService = locarService;
        this.locarRepository = locarRepository;
    }

    /**
     * {@code POST  /locars} : Create a new locar.
     *
     * @param locarDTO the locarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locarDTO, or with status {@code 400 (Bad Request)} if the locar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locars")
    public ResponseEntity<LocarDTO> createLocar(@RequestBody LocarDTO locarDTO) throws URISyntaxException {
        log.debug("REST request to save Locar : {}", locarDTO);
        if (locarDTO.getId() != null) {
            throw new BadRequestAlertException("A new locar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocarDTO result = locarService.save(locarDTO);
        return ResponseEntity
            .created(new URI("/api/locars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locars/:id} : Updates an existing locar.
     *
     * @param id the id of the locarDTO to save.
     * @param locarDTO the locarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locarDTO,
     * or with status {@code 400 (Bad Request)} if the locarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locars/{id}")
    public ResponseEntity<LocarDTO> updateLocar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocarDTO locarDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Locar : {}, {}", id, locarDTO);
        if (locarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocarDTO result = locarService.update(locarDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /locars/:id} : Partial updates given fields of an existing locar, field will ignore if it is null
     *
     * @param id the id of the locarDTO to save.
     * @param locarDTO the locarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locarDTO,
     * or with status {@code 400 (Bad Request)} if the locarDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locarDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocarDTO> partialUpdateLocar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocarDTO locarDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Locar partially : {}, {}", id, locarDTO);
        if (locarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocarDTO> result = locarService.partialUpdate(locarDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locarDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /locars} : get all the locars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locars in body.
     */
    @GetMapping("/locars")
    public List<LocarDTO> getAllLocars() {
        log.debug("REST request to get all Locars");
        return locarService.findAll();
    }

    /**
     * {@code GET  /locars/:id} : get the "id" locar.
     *
     * @param id the id of the locarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locars/{id}")
    public ResponseEntity<LocarDTO> getLocar(@PathVariable Long id) {
        log.debug("REST request to get Locar : {}", id);
        Optional<LocarDTO> locarDTO = locarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locarDTO);
    }

    /**
     * {@code DELETE  /locars/:id} : delete the "id" locar.
     *
     * @param id the id of the locarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locars/{id}")
    public ResponseEntity<Void> deleteLocar(@PathVariable Long id) {
        log.debug("REST request to delete Locar : {}", id);
        locarService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
