package com.java.biblioteca.web.rest;

import com.java.biblioteca.repository.LivroRepository;
import com.java.biblioteca.security.jwt.TokenProvider;
import com.java.biblioteca.service.BookService;
import com.java.biblioteca.service.LivroService;
import com.java.biblioteca.service.dto.LivroDTO;
import com.java.biblioteca.service.dto.LocarDTO;
import com.java.biblioteca.service.dto.LocarDtO;
import com.java.biblioteca.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing {@link com.java.biblioteca.domain.Livro}.
 */
@RestController
@RequestMapping("/api")
public class LivroResource {

    private final Logger log = LoggerFactory.getLogger(LivroResource.class);

    private static final String ENTITY_NAME = "livro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivroService livroService;

    private final TokenProvider tokenProvider;

    private final BookService bookService;

    private final LivroRepository livroRepository;

    public LivroResource(LivroService livroService, TokenProvider tokenProvider, BookService bookService, LivroRepository livroRepository) {
        this.livroService = livroService;
        this.tokenProvider = tokenProvider;
        this.bookService = bookService;
        this.livroRepository = livroRepository;
    }

    /**
     * {@code POST  /livros} : Create a new livro.
     *
     * @param livroDTO the livroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livroDTO, or with status {@code 400 (Bad Request)} if the livro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livros")
    public ResponseEntity<LivroDTO> createLivro(@RequestBody LivroDTO livroDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Livro : {}", livroDTO);
        if (livroDTO.getId() != null) {
            throw new BadRequestAlertException("A new livro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        livroDTO.setAtivo(Long.valueOf(1));
        livroDTO.setProprietario(tokenProvider.userName(request));
        livroDTO.setDataCriacao(LocalDate.from(LocalDateTime.now()));
        LivroDTO result = livroService.save(livroDTO);

        return ResponseEntity
            .created(new URI("/api/livros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livros/:id} : Updates an existing livro.
     *
     * @param id the id of the livroDTO to save.
     * @param livroDTO the livroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livroDTO,
     * or with status {@code 400 (Bad Request)} if the livroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livros/{id}")
    public ResponseEntity<LivroDTO> updateLivro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivroDTO livroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livro : {}, {}", id, livroDTO);
        if (livroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LivroDTO result = livroService.update(livroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /livros/:id} : Partial updates given fields of an existing livro, field will ignore if it is null
     *
     * @param id the id of the livroDTO to save.
     * @param livroDTO the livroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livroDTO,
     * or with status {@code 400 (Bad Request)} if the livroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivroDTO> partialUpdateLivro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivroDTO livroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livro partially : {}, {}", id, livroDTO);
        if (livroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivroDTO> result = livroService.partialUpdate(livroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livros} : get all the livros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livros in body.
     */
    @GetMapping("/livros")
    public ResponseEntity<List<LivroDTO>> getAllLivros(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Livros");
        Page<LivroDTO> page = livroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /livros/:id} : get the "id" livro.
     *
     * @param id the id of the livroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livros/{id}")
    public ResponseEntity<LivroDTO> getLivro(@PathVariable Long id) {
        log.debug("REST request to get Livro : {}", id);
        Optional<LivroDTO> livroDTO = livroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livroDTO);
    }

    /**
     * {@code DELETE  /livros/:id} : delete the "id" livro.
     *
     * @param id the id of the livroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livros/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        log.debug("REST request to delete Livro : {}", id);
        livroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/inativar/{id}")
    public String inativarLivro(@PathVariable Long id, HttpServletRequest request) {
        var user = tokenProvider.userName(request);
        return bookService.inativarLivro(id, user);
    }

    @GetMapping("/ativar/{id}")
    public String ativarLivro(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        var user = tokenProvider.userName(request);
        return bookService.ativarLivro(id, user);
    }

    @PostMapping("/locar/{id}")
    public String locarLivro(@PathVariable(value = "id") Long id, HttpServletRequest request, @RequestBody LocarDtO locarDtO) throws ParseException {
        var user = tokenProvider.userName(request);
        return bookService.locarLivro(id, user, locarDtO);
    }
}
