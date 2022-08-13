package com.java.biblioteca.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.java.biblioteca.IntegrationTest;
import com.java.biblioteca.domain.Livro;
import com.java.biblioteca.repository.LivroRepository;
import com.java.biblioteca.service.dto.LivroDTO;
import com.java.biblioteca.service.mapper.LivroMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LivroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivroResourceIT {

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CRIACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CRIACAO = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ATIVO = 1L;
    private static final Long UPDATED_ATIVO = 2L;

    private static final Long DEFAULT_ID_USUARIO_CADASTRO = 1L;
    private static final Long UPDATED_ID_USUARIO_CADASTRO = 2L;

    private static final String DEFAULT_PROPRIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroMapper livroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivroMockMvc;

    private Livro livro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livro createEntity(EntityManager em) {
        Livro livro = new Livro()
            .autor(DEFAULT_AUTOR)
            .titulo(DEFAULT_TITULO)
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .ativo(DEFAULT_ATIVO)
            .idUsuarioCadastro(DEFAULT_ID_USUARIO_CADASTRO)
            .proprietario(DEFAULT_PROPRIETARIO);
        return livro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livro createUpdatedEntity(EntityManager em) {
        Livro livro = new Livro()
            .autor(UPDATED_AUTOR)
            .titulo(UPDATED_TITULO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ativo(UPDATED_ATIVO)
            .idUsuarioCadastro(UPDATED_ID_USUARIO_CADASTRO)
            .proprietario(UPDATED_PROPRIETARIO);
        return livro;
    }

    @BeforeEach
    public void initTest() {
        livro = createEntity(em);
    }

    @Test
    @Transactional
    void createLivro() throws Exception {
        int databaseSizeBeforeCreate = livroRepository.findAll().size();
        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);
        restLivroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livroDTO)))
            .andExpect(status().isCreated());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate + 1);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testLivro.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testLivro.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testLivro.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testLivro.getIdUsuarioCadastro()).isEqualTo(DEFAULT_ID_USUARIO_CADASTRO);
        assertThat(testLivro.getProprietario()).isEqualTo(DEFAULT_PROPRIETARIO);
    }

    @Test
    @Transactional
    void createLivroWithExistingId() throws Exception {
        // Create the Livro with an existing ID
        livro.setId(1L);
        LivroDTO livroDTO = livroMapper.toDto(livro);

        int databaseSizeBeforeCreate = livroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLivros() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get all the livroList
        restLivroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livro.getId().intValue())))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(DEFAULT_DATA_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.intValue())))
            .andExpect(jsonPath("$.[*].idUsuarioCadastro").value(hasItem(DEFAULT_ID_USUARIO_CADASTRO.intValue())))
            .andExpect(jsonPath("$.[*].proprietario").value(hasItem(DEFAULT_PROPRIETARIO)));
    }

    @Test
    @Transactional
    void getLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        // Get the livro
        restLivroMockMvc
            .perform(get(ENTITY_API_URL_ID, livro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livro.getId().intValue()))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.dataCriacao").value(DEFAULT_DATA_CRIACAO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.intValue()))
            .andExpect(jsonPath("$.idUsuarioCadastro").value(DEFAULT_ID_USUARIO_CADASTRO.intValue()))
            .andExpect(jsonPath("$.proprietario").value(DEFAULT_PROPRIETARIO));
    }

    @Test
    @Transactional
    void getNonExistingLivro() throws Exception {
        // Get the livro
        restLivroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Update the livro
        Livro updatedLivro = livroRepository.findById(livro.getId()).get();
        // Disconnect from session so that the updates on updatedLivro are not directly saved in db
        em.detach(updatedLivro);
        updatedLivro
            .autor(UPDATED_AUTOR)
            .titulo(UPDATED_TITULO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ativo(UPDATED_ATIVO)
            .idUsuarioCadastro(UPDATED_ID_USUARIO_CADASTRO)
            .proprietario(UPDATED_PROPRIETARIO);
        LivroDTO livroDTO = livroMapper.toDto(updatedLivro);

        restLivroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testLivro.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testLivro.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testLivro.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testLivro.getIdUsuarioCadastro()).isEqualTo(UPDATED_ID_USUARIO_CADASTRO);
        assertThat(testLivro.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void putNonExistingLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivroWithPatch() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Update the livro using partial update
        Livro partialUpdatedLivro = new Livro();
        partialUpdatedLivro.setId(livro.getId());

        partialUpdatedLivro.titulo(UPDATED_TITULO).idUsuarioCadastro(UPDATED_ID_USUARIO_CADASTRO).proprietario(UPDATED_PROPRIETARIO);

        restLivroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivro))
            )
            .andExpect(status().isOk());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testLivro.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testLivro.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testLivro.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testLivro.getIdUsuarioCadastro()).isEqualTo(UPDATED_ID_USUARIO_CADASTRO);
        assertThat(testLivro.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void fullUpdateLivroWithPatch() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        int databaseSizeBeforeUpdate = livroRepository.findAll().size();

        // Update the livro using partial update
        Livro partialUpdatedLivro = new Livro();
        partialUpdatedLivro.setId(livro.getId());

        partialUpdatedLivro
            .autor(UPDATED_AUTOR)
            .titulo(UPDATED_TITULO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ativo(UPDATED_ATIVO)
            .idUsuarioCadastro(UPDATED_ID_USUARIO_CADASTRO)
            .proprietario(UPDATED_PROPRIETARIO);

        restLivroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivro))
            )
            .andExpect(status().isOk());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
        Livro testLivro = livroList.get(livroList.size() - 1);
        assertThat(testLivro.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testLivro.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testLivro.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testLivro.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testLivro.getIdUsuarioCadastro()).isEqualTo(UPDATED_ID_USUARIO_CADASTRO);
        assertThat(testLivro.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void patchNonExistingLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivro() throws Exception {
        int databaseSizeBeforeUpdate = livroRepository.findAll().size();
        livro.setId(count.incrementAndGet());

        // Create the Livro
        LivroDTO livroDTO = livroMapper.toDto(livro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livro in the database
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivro() throws Exception {
        // Initialize the database
        livroRepository.saveAndFlush(livro);

        int databaseSizeBeforeDelete = livroRepository.findAll().size();

        // Delete the livro
        restLivroMockMvc
            .perform(delete(ENTITY_API_URL_ID, livro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livro> livroList = livroRepository.findAll();
        assertThat(livroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
