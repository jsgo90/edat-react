package com.edat.web.rest;

import static com.edat.domain.BaneadosAsserts.*;
import static com.edat.web.rest.TestUtil.createUpdateProxyForBean;
import static com.edat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edat.IntegrationTest;
import com.edat.domain.Baneados;
import com.edat.repository.BaneadosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BaneadosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BaneadosResourceIT {

    private static final Long DEFAULT_DNI = 1L;
    private static final Long UPDATED_DNI = 2L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_BANEO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_BANEO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/baneados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BaneadosRepository baneadosRepository;

    @Mock
    private BaneadosRepository baneadosRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaneadosMockMvc;

    private Baneados baneados;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Baneados createEntity(EntityManager em) {
        Baneados baneados = new Baneados()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .motivo(DEFAULT_MOTIVO)
            .fechaBaneo(DEFAULT_FECHA_BANEO);
        return baneados;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Baneados createUpdatedEntity(EntityManager em) {
        Baneados baneados = new Baneados()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .motivo(UPDATED_MOTIVO)
            .fechaBaneo(UPDATED_FECHA_BANEO);
        return baneados;
    }

    @BeforeEach
    public void initTest() {
        baneados = createEntity(em);
    }

    @Test
    @Transactional
    void createBaneados() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Baneados
        var returnedBaneados = om.readValue(
            restBaneadosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baneados)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Baneados.class
        );

        // Validate the Baneados in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBaneadosUpdatableFieldsEquals(returnedBaneados, getPersistedBaneados(returnedBaneados));
    }

    @Test
    @Transactional
    void createBaneadosWithExistingId() throws Exception {
        // Create the Baneados with an existing ID
        baneados.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaneadosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baneados)))
            .andExpect(status().isBadRequest());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaneados() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        // Get all the baneadosList
        restBaneadosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baneados.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].fechaBaneo").value(hasItem(sameInstant(DEFAULT_FECHA_BANEO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBaneadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(baneadosRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBaneadosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(baneadosRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBaneadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(baneadosRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBaneadosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(baneadosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBaneados() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        // Get the baneados
        restBaneadosMockMvc
            .perform(get(ENTITY_API_URL_ID, baneados.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baneados.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO))
            .andExpect(jsonPath("$.fechaBaneo").value(sameInstant(DEFAULT_FECHA_BANEO)));
    }

    @Test
    @Transactional
    void getNonExistingBaneados() throws Exception {
        // Get the baneados
        restBaneadosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaneados() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baneados
        Baneados updatedBaneados = baneadosRepository.findById(baneados.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBaneados are not directly saved in db
        em.detach(updatedBaneados);
        updatedBaneados
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .motivo(UPDATED_MOTIVO)
            .fechaBaneo(UPDATED_FECHA_BANEO);

        restBaneadosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBaneados.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBaneados))
            )
            .andExpect(status().isOk());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBaneadosToMatchAllProperties(updatedBaneados);
    }

    @Test
    @Transactional
    void putNonExistingBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baneados.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baneados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baneados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baneados)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaneadosWithPatch() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baneados using partial update
        Baneados partialUpdatedBaneados = new Baneados();
        partialUpdatedBaneados.setId(baneados.getId());

        partialUpdatedBaneados.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).motivo(UPDATED_MOTIVO).fechaBaneo(UPDATED_FECHA_BANEO);

        restBaneadosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaneados.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaneados))
            )
            .andExpect(status().isOk());

        // Validate the Baneados in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaneadosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBaneados, baneados), getPersistedBaneados(baneados));
    }

    @Test
    @Transactional
    void fullUpdateBaneadosWithPatch() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baneados using partial update
        Baneados partialUpdatedBaneados = new Baneados();
        partialUpdatedBaneados.setId(baneados.getId());

        partialUpdatedBaneados
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .motivo(UPDATED_MOTIVO)
            .fechaBaneo(UPDATED_FECHA_BANEO);

        restBaneadosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaneados.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaneados))
            )
            .andExpect(status().isOk());

        // Validate the Baneados in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaneadosUpdatableFieldsEquals(partialUpdatedBaneados, getPersistedBaneados(partialUpdatedBaneados));
    }

    @Test
    @Transactional
    void patchNonExistingBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baneados.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baneados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baneados))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaneados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baneados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaneadosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(baneados)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Baneados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaneados() throws Exception {
        // Initialize the database
        baneadosRepository.saveAndFlush(baneados);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the baneados
        restBaneadosMockMvc
            .perform(delete(ENTITY_API_URL_ID, baneados.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return baneadosRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Baneados getPersistedBaneados(Baneados baneados) {
        return baneadosRepository.findById(baneados.getId()).orElseThrow();
    }

    protected void assertPersistedBaneadosToMatchAllProperties(Baneados expectedBaneados) {
        assertBaneadosAllPropertiesEquals(expectedBaneados, getPersistedBaneados(expectedBaneados));
    }

    protected void assertPersistedBaneadosToMatchUpdatableProperties(Baneados expectedBaneados) {
        assertBaneadosAllUpdatablePropertiesEquals(expectedBaneados, getPersistedBaneados(expectedBaneados));
    }
}
