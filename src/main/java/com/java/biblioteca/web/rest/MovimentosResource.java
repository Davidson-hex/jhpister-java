package com.java.biblioteca.web.rest;

import com.java.biblioteca.repository.MovimentosRepository;
import com.java.biblioteca.service.MovimentosService;
import com.java.biblioteca.service.dto.MovimentosDTO;
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
 * REST controller for managing {@link com.java.biblioteca.domain.Movimentos}.
 */
@RestController
@RequestMapping("/api")
public class MovimentosResource {

    private final Logger log = LoggerFactory.getLogger(MovimentosResource.class);

    private static final String ENTITY_NAME = "movimentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovimentosService movimentosService;

    private final MovimentosRepository movimentosRepository;

    public MovimentosResource(MovimentosService movimentosService, MovimentosRepository movimentosRepository) {
        this.movimentosService = movimentosService;
        this.movimentosRepository = movimentosRepository;
    }

    /**
     * {@code POST  /movimentos} : Create a new movimentos.
     *
     * @param movimentosDTO the movimentosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movimentosDTO, or with status {@code 400 (Bad Request)} if the movimentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movimentos")
    public ResponseEntity<MovimentosDTO> createMovimentos(@RequestBody MovimentosDTO movimentosDTO) throws URISyntaxException {
        log.debug("REST request to save Movimentos : {}", movimentosDTO);
        if (movimentosDTO.getId() != null) {
            throw new BadRequestAlertException("A new movimentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovimentosDTO result = movimentosService.save(movimentosDTO);
        return ResponseEntity
            .created(new URI("/api/movimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movimentos/:id} : Updates an existing movimentos.
     *
     * @param id the id of the movimentosDTO to save.
     * @param movimentosDTO the movimentosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimentosDTO,
     * or with status {@code 400 (Bad Request)} if the movimentosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movimentosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movimentos/{id}")
    public ResponseEntity<MovimentosDTO> updateMovimentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovimentosDTO movimentosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Movimentos : {}, {}", id, movimentosDTO);
        if (movimentosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimentosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movimentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovimentosDTO result = movimentosService.update(movimentosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movimentosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movimentos/:id} : Partial updates given fields of an existing movimentos, field will ignore if it is null
     *
     * @param id the id of the movimentosDTO to save.
     * @param movimentosDTO the movimentosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimentosDTO,
     * or with status {@code 400 (Bad Request)} if the movimentosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movimentosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movimentosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movimentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovimentosDTO> partialUpdateMovimentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MovimentosDTO movimentosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Movimentos partially : {}, {}", id, movimentosDTO);
        if (movimentosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimentosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movimentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovimentosDTO> result = movimentosService.partialUpdate(movimentosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movimentosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /movimentos} : get all the movimentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movimentos in body.
     */
    @GetMapping("/movimentos")
    public List<MovimentosDTO> getAllMovimentos() {
        log.debug("REST request to get all Movimentos");
        return movimentosService.findAll();
    }

    /**
     * {@code GET  /movimentos/:id} : get the "id" movimentos.
     *
     * @param id the id of the movimentosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movimentosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movimentos/{id}")
    public ResponseEntity<MovimentosDTO> getMovimentos(@PathVariable Long id) {
        log.debug("REST request to get Movimentos : {}", id);
        Optional<MovimentosDTO> movimentosDTO = movimentosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movimentosDTO);
    }

    /**
     * {@code DELETE  /movimentos/:id} : delete the "id" movimentos.
     *
     * @param id the id of the movimentosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movimentos/{id}")
    public ResponseEntity<Void> deleteMovimentos(@PathVariable Long id) {
        log.debug("REST request to delete Movimentos : {}", id);
        movimentosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
