package com.java.biblioteca.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.java.biblioteca.IntegrationTest;
import com.java.biblioteca.domain.Locar;
import com.java.biblioteca.repository.LocarRepository;
import com.java.biblioteca.service.dto.LocarDTO;
import com.java.biblioteca.service.mapper.LocarMapper;
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
 * Integration tests for the {@link LocarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocarResourceIT {

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

    private static final String ENTITY_API_URL = "/api/locars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocarRepository locarRepository;

    @Autowired
    private LocarMapper locarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocarMockMvc;

    private Locar locar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locar createEntity(EntityManager em) {
        Locar locar = new Locar()
            .idUsuario(DEFAULT_ID_USUARIO)
            .idLivro(DEFAULT_ID_LIVRO)
            .locacao(DEFAULT_LOCACAO)
            .dataLocacao(DEFAULT_DATA_LOCACAO)
            .previsaoDevolucao(DEFAULT_PREVISAO_DEVOLUCAO)
            .dataDevolucao(DEFAULT_DATA_DEVOLUCAO)
            .devolucao(DEFAULT_DEVOLUCAO)
            .status(DEFAULT_STATUS)
            .proprietario(DEFAULT_PROPRIETARIO);
        return locar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locar createUpdatedEntity(EntityManager em) {
        Locar locar = new Locar()
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);
        return locar;
    }

    @BeforeEach
    public void initTest() {
        locar = createEntity(em);
    }

    @Test
    @Transactional
    void createLocar() throws Exception {
        int databaseSizeBeforeCreate = locarRepository.findAll().size();
        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);
        restLocarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locarDTO)))
            .andExpect(status().isCreated());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeCreate + 1);
        Locar testLocar = locarList.get(locarList.size() - 1);
        assertThat(testLocar.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testLocar.getIdLivro()).isEqualTo(DEFAULT_ID_LIVRO);
        assertThat(testLocar.getLocacao()).isEqualTo(DEFAULT_LOCACAO);
        assertThat(testLocar.getDataLocacao()).isEqualTo(DEFAULT_DATA_LOCACAO);
        assertThat(testLocar.getPrevisaoDevolucao()).isEqualTo(DEFAULT_PREVISAO_DEVOLUCAO);
        assertThat(testLocar.getDataDevolucao()).isEqualTo(DEFAULT_DATA_DEVOLUCAO);
        assertThat(testLocar.getDevolucao()).isEqualTo(DEFAULT_DEVOLUCAO);
        assertThat(testLocar.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocar.getProprietario()).isEqualTo(DEFAULT_PROPRIETARIO);
    }

    @Test
    @Transactional
    void createLocarWithExistingId() throws Exception {
        // Create the Locar with an existing ID
        locar.setId(1L);
        LocarDTO locarDTO = locarMapper.toDto(locar);

        int databaseSizeBeforeCreate = locarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocars() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        // Get all the locarList
        restLocarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locar.getId().intValue())))
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
    void getLocar() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        // Get the locar
        restLocarMockMvc
            .perform(get(ENTITY_API_URL_ID, locar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locar.getId().intValue()))
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
    void getNonExistingLocar() throws Exception {
        // Get the locar
        restLocarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocar() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        int databaseSizeBeforeUpdate = locarRepository.findAll().size();

        // Update the locar
        Locar updatedLocar = locarRepository.findById(locar.getId()).get();
        // Disconnect from session so that the updates on updatedLocar are not directly saved in db
        em.detach(updatedLocar);
        updatedLocar
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);
        LocarDTO locarDTO = locarMapper.toDto(updatedLocar);

        restLocarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locarDTO))
            )
            .andExpect(status().isOk());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
        Locar testLocar = locarList.get(locarList.size() - 1);
        assertThat(testLocar.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLocar.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testLocar.getLocacao()).isEqualTo(UPDATED_LOCACAO);
        assertThat(testLocar.getDataLocacao()).isEqualTo(UPDATED_DATA_LOCACAO);
        assertThat(testLocar.getPrevisaoDevolucao()).isEqualTo(UPDATED_PREVISAO_DEVOLUCAO);
        assertThat(testLocar.getDataDevolucao()).isEqualTo(UPDATED_DATA_DEVOLUCAO);
        assertThat(testLocar.getDevolucao()).isEqualTo(UPDATED_DEVOLUCAO);
        assertThat(testLocar.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocar.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void putNonExistingLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocarWithPatch() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        int databaseSizeBeforeUpdate = locarRepository.findAll().size();

        // Update the locar using partial update
        Locar partialUpdatedLocar = new Locar();
        partialUpdatedLocar.setId(locar.getId());

        partialUpdatedLocar
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .proprietario(UPDATED_PROPRIETARIO);

        restLocarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocar))
            )
            .andExpect(status().isOk());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
        Locar testLocar = locarList.get(locarList.size() - 1);
        assertThat(testLocar.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLocar.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testLocar.getLocacao()).isEqualTo(DEFAULT_LOCACAO);
        assertThat(testLocar.getDataLocacao()).isEqualTo(UPDATED_DATA_LOCACAO);
        assertThat(testLocar.getPrevisaoDevolucao()).isEqualTo(UPDATED_PREVISAO_DEVOLUCAO);
        assertThat(testLocar.getDataDevolucao()).isEqualTo(UPDATED_DATA_DEVOLUCAO);
        assertThat(testLocar.getDevolucao()).isEqualTo(DEFAULT_DEVOLUCAO);
        assertThat(testLocar.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocar.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void fullUpdateLocarWithPatch() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        int databaseSizeBeforeUpdate = locarRepository.findAll().size();

        // Update the locar using partial update
        Locar partialUpdatedLocar = new Locar();
        partialUpdatedLocar.setId(locar.getId());

        partialUpdatedLocar
            .idUsuario(UPDATED_ID_USUARIO)
            .idLivro(UPDATED_ID_LIVRO)
            .locacao(UPDATED_LOCACAO)
            .dataLocacao(UPDATED_DATA_LOCACAO)
            .previsaoDevolucao(UPDATED_PREVISAO_DEVOLUCAO)
            .dataDevolucao(UPDATED_DATA_DEVOLUCAO)
            .devolucao(UPDATED_DEVOLUCAO)
            .status(UPDATED_STATUS)
            .proprietario(UPDATED_PROPRIETARIO);

        restLocarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocar))
            )
            .andExpect(status().isOk());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
        Locar testLocar = locarList.get(locarList.size() - 1);
        assertThat(testLocar.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLocar.getIdLivro()).isEqualTo(UPDATED_ID_LIVRO);
        assertThat(testLocar.getLocacao()).isEqualTo(UPDATED_LOCACAO);
        assertThat(testLocar.getDataLocacao()).isEqualTo(UPDATED_DATA_LOCACAO);
        assertThat(testLocar.getPrevisaoDevolucao()).isEqualTo(UPDATED_PREVISAO_DEVOLUCAO);
        assertThat(testLocar.getDataDevolucao()).isEqualTo(UPDATED_DATA_DEVOLUCAO);
        assertThat(testLocar.getDevolucao()).isEqualTo(UPDATED_DEVOLUCAO);
        assertThat(testLocar.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocar.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
    }

    @Test
    @Transactional
    void patchNonExistingLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocar() throws Exception {
        int databaseSizeBeforeUpdate = locarRepository.findAll().size();
        locar.setId(count.incrementAndGet());

        // Create the Locar
        LocarDTO locarDTO = locarMapper.toDto(locar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locar in the database
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocar() throws Exception {
        // Initialize the database
        locarRepository.saveAndFlush(locar);

        int databaseSizeBeforeDelete = locarRepository.findAll().size();

        // Delete the locar
        restLocarMockMvc
            .perform(delete(ENTITY_API_URL_ID, locar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Locar> locarList = locarRepository.findAll();
        assertThat(locarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
