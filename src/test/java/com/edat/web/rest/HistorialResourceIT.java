package com.edat.web.rest;

import static com.edat.domain.HistorialAsserts.*;
import static com.edat.web.rest.TestUtil.createUpdateProxyForBean;
import static com.edat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edat.IntegrationTest;
import com.edat.domain.Autorizado;
import com.edat.domain.Historial;
import com.edat.repository.HistorialRepository;
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
 * Integration tests for the {@link HistorialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistorialResourceIT {

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/historials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistorialRepository historialRepository;

    @Mock
    private HistorialRepository historialRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistorialMockMvc;

    private Historial historial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createEntity(EntityManager em) {
        Historial historial = new Historial().fecha(DEFAULT_FECHA);
        // Add required entity
        Autorizado autorizado;
        if (TestUtil.findAll(em, Autorizado.class).isEmpty()) {
            autorizado = AutorizadoResourceIT.createEntity(em);
            em.persist(autorizado);
            em.flush();
        } else {
            autorizado = TestUtil.findAll(em, Autorizado.class).get(0);
        }
        historial.setAutorizado(autorizado);
        return historial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createUpdatedEntity(EntityManager em) {
        Historial historial = new Historial().fecha(UPDATED_FECHA);
        // Add required entity
        Autorizado autorizado;
        if (TestUtil.findAll(em, Autorizado.class).isEmpty()) {
            autorizado = AutorizadoResourceIT.createUpdatedEntity(em);
            em.persist(autorizado);
            em.flush();
        } else {
            autorizado = TestUtil.findAll(em, Autorizado.class).get(0);
        }
        historial.setAutorizado(autorizado);
        return historial;
    }

    @BeforeEach
    public void initTest() {
        historial = createEntity(em);
    }

    @Test
    @Transactional
    void createHistorial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Historial
        var returnedHistorial = om.readValue(
            restHistorialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historial)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Historial.class
        );

        // Validate the Historial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHistorialUpdatableFieldsEquals(returnedHistorial, getPersistedHistorial(returnedHistorial));
    }

    @Test
    @Transactional
    void createHistorialWithExistingId() throws Exception {
        // Create the Historial with an existing ID
        historial.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historial)))
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historial.setFecha(null);

        // Create the Historial, which fails.

        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historial)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistorials() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get all the historialList
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historial.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistorialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(historialRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historialRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistorialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historialRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historialRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get the historial
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL_ID, historial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historial.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(sameInstant(DEFAULT_FECHA)));
    }

    @Test
    @Transactional
    void getNonExistingHistorial() throws Exception {
        // Get the historial
        restHistorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial
        Historial updatedHistorial = historialRepository.findById(historial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistorial are not directly saved in db
        em.detach(updatedHistorial);
        updatedHistorial.fecha(UPDATED_FECHA);

        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHistorial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistorialToMatchAllProperties(updatedHistorial);
    }

    @Test
    @Transactional
    void putNonExistingHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historial.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistorial, historial),
            getPersistedHistorial(historial)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        partialUpdatedHistorial.fecha(UPDATED_FECHA);

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialUpdatableFieldsEquals(partialUpdatedHistorial, getPersistedHistorial(partialUpdatedHistorial));
    }

    @Test
    @Transactional
    void patchNonExistingHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historial
        restHistorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, historial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historialRepository.count();
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

    protected Historial getPersistedHistorial(Historial historial) {
        return historialRepository.findById(historial.getId()).orElseThrow();
    }

    protected void assertPersistedHistorialToMatchAllProperties(Historial expectedHistorial) {
        assertHistorialAllPropertiesEquals(expectedHistorial, getPersistedHistorial(expectedHistorial));
    }

    protected void assertPersistedHistorialToMatchUpdatableProperties(Historial expectedHistorial) {
        assertHistorialAllUpdatablePropertiesEquals(expectedHistorial, getPersistedHistorial(expectedHistorial));
    }
}
