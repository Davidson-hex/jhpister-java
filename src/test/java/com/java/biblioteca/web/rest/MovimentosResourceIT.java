package com.java.biblioteca.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.java.biblioteca.IntegrationTest;
import com.java.biblioteca.domain.Movimentos;
import com.java.biblioteca.repository.MovimentosRepository;
import com.java.biblioteca.service.dto.MovimentosDTO;
import com.java.biblioteca.service.mapper.MovimentosMapper;
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
 * Integration tests for the {@link MovimentosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MovimentosResourceIT {

    private static final Integer DEFAULT_ID_USUARIO = 1;
    private static final Integer UPDATED_ID_USUARIO = 2;

    private static final Integer DEFAULT_ID_LIVRO = 1;
    private static final Integer UPDATED_ID_LIVRO = 2;

    private static final Integer DEFAULT_LOCACAO = 1;
    private static final Integer UPDATED_LOCACAO = 2;

    private static final LocalDate DEFAULT_DATA_LOCACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_LOCACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PREVISAO_DEVOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREVISAO_DEVOLUCAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_DEVOLUCAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DEVOLUCAO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DEVOLUCAO = 1;
    private static final Integer UPDATED_DEVOLUCAO = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PROPRIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/movimentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovimentosRepository movimentosRepository;

    @Autowired
    private MovimentosMapper movimentosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovimentosMockMvc;

    private Movimentos movimentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movimentos createEntity(EntityManager em) {
        Movimentos movimentos = new Movimentos()
            .idUsuario(DEFAULT_ID_USUARIO)
            .idLivro(DEFAULT_ID_LIVRO)
            .locacao(DEFAULT_LOCACAO)
            .dataLocacao(DEFAULT_DATA_LOCACAO)
            .previsaoDevolucao(DEFAULT_PREVISAO_DEVOLUCAO)
            .dataDevolucao(DEFAULT_DATA_DEVOLUCAO)
            .devolucao(DEFAULT_DEVOLUCAO)
            .status(DEFAULT_STATUS)
            .proprietario(DEFAULT_PROPRIETARIO);
        return movimentos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movimentos createUpdatedEntity(EntityManager em) {
        Movimentos movimentos = new Movimentos()
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);
        return movimentos;
    }

    @BeforeEach
    public void initTest() {
        movimentos = createEntity(em);
    }

    @Test
    @Transactional
    void createMovimentos() throws Exception {
        int databaseSizeBeforeCreate = movimentosRepository.findAll().size();
        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);
        restMovimentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movimentosDTO)))
            .andExpect(status().isCreated());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeCreate + 1);
        Movimentos testMovimentos = movimentosList.get(movimentosList.size() - 1);
        assertThat(testMovimentos.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testMovimentos.getIdLivro()).isEqualTo(DEFAULT_ID_LIVRO);
        assertThat(testMovimentos.getLocacao()).isEqualTo(DEFAULT_LOCACAO);
        assertThat(testMovimentos.getDataLocacao()).isEqualTo(DEFAULT_DATA_LOCACAO);
        assertThat(testMovimentos.getPrevisaoDevolucao()).isEqualTo(DEFAULT_PREVISAO_DEVOLUCAO);
        assertThat(testMovimentos.getDataDevolucao()).isEqualTo(DEFAULT_DATA_DEVOLUCAO);
        assertThat(testMovimentos.getDevolucao()).isEqualTo(DEFAULT_DEVOLUCAO);
        assertThat(testMovimentos.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMovimentos.getProprietario()).isEqualTo(DEFAULT_PROPRIETARIO);
    }

    @Test
    @Transactional
    void createMovimentosWithExistingId() throws Exception {
        // Create the Movimentos with an existing ID
        movimentos.setId(1L);
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        int databaseSizeBeforeCreate = movimentosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovimentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movimentosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMovimentos() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        // Get all the movimentosList
        restMovimentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUsuario").value(hasItem(DEFAULT_ID_USUARIO)))
            .andExpect(jsonPath("$.[*].idLivro").value(hasItem(DEFAULT_ID_LIVRO)))
            .andExpect(jsonPath("$.[*].locacao").value(hasItem(DEFAULT_LOCACAO)))
            .andExpect(jsonPath("$.[*].dataLocacao").value(hasItem(DEFAULT_DATA_LOCACAO.toString())))
            .andExpect(jsonPath("$.[*].previsaoDevolucao").value(hasItem(DEFAULT_PREVISAO_DEVOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].dataDevolucao").value(hasItem(DEFAULT_DATA_DEVOLUCAO.toString())))
            .andExpect(jsonPath("$.[*].devolucao").value(hasItem(DEFAULT_DEVOLUCAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].proprietario").value(hasItem(DEFAULT_PROPRIETARIO)));
    }

    @Test
    @Transactional
    void getMovimentos() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        // Get the movimentos
        restMovimentosMockMvc
            .perform(get(ENTITY_API_URL_ID, movimentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movimentos.getId().intValue()))
            .andExpect(jsonPath("$.idUsuario").value(DEFAULT_ID_USUARIO))
            .andExpect(jsonPath("$.idLivro").value(DEFAULT_ID_LIVRO))
            .andExpect(jsonPath("$.locacao").value(DEFAULT_LOCACAO))
            .andExpect(jsonPath("$.dataLocacao").value(DEFAULT_DATA_LOCACAO.toString()))
            .andExpect(jsonPath("$.previsaoDevolucao").value(DEFAULT_PREVISAO_DEVOLUCAO.toString()))
            .andExpect(jsonPath("$.dataDevolucao").value(DEFAULT_DATA_DEVOLUCAO.toString()))
            .andExpect(jsonPath("$.devolucao").value(DEFAULT_DEVOLUCAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.proprietario").value(DEFAULT_PROPRIETARIO));
    }

    @Test
    @Transactional
    void getNonExistingMovimentos() throws Exception {
        // Get the movimentos
        restMovimentosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMovimentos() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();

        // Update the movimentos
        Movimentos updatedMovimentos = movimentosRepository.findById(movimentos.getId()).get();
        // Disconnect from session so that the updates on updatedMovimentos are not directly saved in db
        em.detach(updatedMovimentos);
        updatedMovimentos
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(updatedMovimentos);

        restMovimentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movimentosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
        Movimentos testMovimentos = movimentosList.get(movimentosList.size() - 1);
        assertThat(testMovimentos.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testMovimentos.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testMovimentos.getLocacao()).isEqualTo(UPDATED_LOCACAO);
        assertThat(testMovimentos.getDataLocacao()).isEqualTo(UPDATED_DATA_LOCACAO);
        assertThat(testMovimentos.getPrevisaoDevolucao()).isEqualTo(UPDATED_PREVISAO_DEVOLUCAO);
        assertThat(testMovimentos.getDataDevolucao()).isEqualTo(UPDATED_DATA_DEVOLUCAO);
        assertThat(testMovimentos.getDevolucao()).isEqualTo(UPDATED_DEVOLUCAO);
        assertThat(testMovimentos.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMovimentos.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void putNonExistingMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movimentosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movimentosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovimentosWithPatch() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();

        // Update the movimentos using partial update
        Movimentos partialUpdatedMovimentos = new Movimentos();
        partialUpdatedMovimentos.setId(movimentos.getId());

        partialUpdatedMovimentos
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);

        restMovimentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovimentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovimentos))
            )
            .andExpect(status().isOk());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
        Movimentos testMovimentos = movimentosList.get(movimentosList.size() - 1);
        assertThat(testMovimentos.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testMovimentos.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testMovimentos.getLocacao()).isEqualTo(UPDATED_LOCACAO);
        assertThat(testMovimentos.getDataLocacao()).isEqualTo(DEFAULT_DATA_LOCACAO);
        assertThat(testMovimentos.getPrevisaoDevolucao()).isEqualTo(DEFAULT_PREVISAO_DEVOLUCAO);
        assertThat(testMovimentos.getDataDevolucao()).isEqualTo(DEFAULT_DATA_DEVOLUCAO);
        assertThat(testMovimentos.getDevolucao()).isEqualTo(DEFAULT_DEVOLUCAO);
        assertThat(testMovimentos.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMovimentos.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void fullUpdateMovimentosWithPatch() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();

        // Update the movimentos using partial update
        Movimentos partialUpdatedMovimentos = new Movimentos();
        partialUpdatedMovimentos.setId(movimentos.getId());

        partialUpdatedMovimentos
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);

        restMovimentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovimentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovimentos))
            )
            .andExpect(status().isOk());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
        Movimentos testMovimentos = movimentosList.get(movimentosList.size() - 1);
        assertThat(testMovimentos.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testMovimentos.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testMovimentos.getLocacao()).isEqualTo(UPDATED_LOCACAO);
        assertThat(testMovimentos.getDataLocacao()).isEqualTo(UPDATED_DATA_LOCACAO);
        assertThat(testMovimentos.getPrevisaoDevolucao()).isEqualTo(UPDATED_PREVISAO_DEVOLUCAO);
        assertThat(testMovimentos.getDataDevolucao()).isEqualTo(UPDATED_DATA_DEVOLUCAO);
        assertThat(testMovimentos.getDevolucao()).isEqualTo(UPDATED_DEVOLUCAO);
        assertThat(testMovimentos.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMovimentos.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void patchNonExistingMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movimentosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovimentos() throws Exception {
        int databaseSizeBeforeUpdate = movimentosRepository.findAll().size();
        movimentos.setId(count.incrementAndGet());

        // Create the Movimentos
        MovimentosDTO movimentosDTO = movimentosMapper.toDto(movimentos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovimentosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(movimentosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movimentos in the database
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovimentos() throws Exception {
        // Initialize the database
        movimentosRepository.saveAndFlush(movimentos);

        int databaseSizeBeforeDelete = movimentosRepository.findAll().size();

        // Delete the movimentos
        restMovimentosMockMvc
            .perform(delete(ENTITY_API_URL_ID, movimentos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movimentos> movimentosList = movimentosRepository.findAll();
        assertThat(movimentosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
