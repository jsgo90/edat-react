package com.edat.web.rest;

import static com.edat.domain.AutorizadoAsserts.*;
import static com.edat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edat.IntegrationTest;
import com.edat.domain.Autorizado;
import com.edat.repository.AutorizadoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AutorizadoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AutorizadoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final Long DEFAULT_DNI = 1L;
    private static final Long UPDATED_DNI = 2L;

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/autorizados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AutorizadoRepository autorizadoRepository;

    @Mock
    private AutorizadoRepository autorizadoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutorizadoMockMvc;

    private Autorizado autorizado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autorizado createEntity(EntityManager em) {
        Autorizado autorizado = new Autorizado()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .dni(DEFAULT_DNI)
            .telefono(DEFAULT_TELEFONO);
        return autorizado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autorizado createUpdatedEntity(EntityManager em) {
        Autorizado autorizado = new Autorizado()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dni(UPDATED_DNI)
            .telefono(UPDATED_TELEFONO);
        return autorizado;
    }

    @BeforeEach
    public void initTest() {
        autorizado = createEntity(em);
    }

    @Test
    @Transactional
    void createAutorizado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Autorizado
        var returnedAutorizado = om.readValue(
            restAutorizadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorizado)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Autorizado.class
        );

        // Validate the Autorizado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAutorizadoUpdatableFieldsEquals(returnedAutorizado, getPersistedAutorizado(returnedAutorizado));
    }

    @Test
    @Transactional
    void createAutorizadoWithExistingId() throws Exception {
        // Create the Autorizado with an existing ID
        autorizado.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutorizadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorizado)))
            .andExpect(status().isBadRequest());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAutorizados() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        // Get all the autorizadoList
        restAutorizadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autorizado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.intValue())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutorizadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(autorizadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutorizadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(autorizadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutorizadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(autorizadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutorizadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(autorizadoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAutorizado() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        // Get the autorizado
        restAutorizadoMockMvc
            .perform(get(ENTITY_API_URL_ID, autorizado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autorizado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.intValue()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingAutorizado() throws Exception {
        // Get the autorizado
        restAutorizadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutorizado() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorizado
        Autorizado updatedAutorizado = autorizadoRepository.findById(autorizado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAutorizado are not directly saved in db
        em.detach(updatedAutorizado);
        updatedAutorizado.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).dni(UPDATED_DNI).telefono(UPDATED_TELEFONO);

        restAutorizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAutorizado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAutorizado))
            )
            .andExpect(status().isOk());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAutorizadoToMatchAllProperties(updatedAutorizado);
    }

    @Test
    @Transactional
    void putNonExistingAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autorizado.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorizado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autorizado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorizado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutorizadoWithPatch() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorizado using partial update
        Autorizado partialUpdatedAutorizado = new Autorizado();
        partialUpdatedAutorizado.setId(autorizado.getId());

        restAutorizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutorizado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutorizado))
            )
            .andExpect(status().isOk());

        // Validate the Autorizado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutorizadoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAutorizado, autorizado),
            getPersistedAutorizado(autorizado)
        );
    }

    @Test
    @Transactional
    void fullUpdateAutorizadoWithPatch() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorizado using partial update
        Autorizado partialUpdatedAutorizado = new Autorizado();
        partialUpdatedAutorizado.setId(autorizado.getId());

        partialUpdatedAutorizado.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).dni(UPDATED_DNI).telefono(UPDATED_TELEFONO);

        restAutorizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutorizado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutorizado))
            )
            .andExpect(status().isOk());

        // Validate the Autorizado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutorizadoUpdatableFieldsEquals(partialUpdatedAutorizado, getPersistedAutorizado(partialUpdatedAutorizado));
    }

    @Test
    @Transactional
    void patchNonExistingAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autorizado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autorizado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autorizado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutorizado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorizado.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorizadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(autorizado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autorizado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutorizado() throws Exception {
        // Initialize the database
        autorizadoRepository.saveAndFlush(autorizado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the autorizado
        restAutorizadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, autorizado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return autorizadoRepository.count();
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

    protected Autorizado getPersistedAutorizado(Autorizado autorizado) {
        return autorizadoRepository.findById(autorizado.getId()).orElseThrow();
    }

    protected void assertPersistedAutorizadoToMatchAllProperties(Autorizado expectedAutorizado) {
        assertAutorizadoAllPropertiesEquals(expectedAutorizado, getPersistedAutorizado(expectedAutorizado));
    }

    protected void assertPersistedAutorizadoToMatchUpdatableProperties(Autorizado expectedAutorizado) {
        assertAutorizadoAllUpdatablePropertiesEquals(expectedAutorizado, getPersistedAutorizado(expectedAutorizado));
    }
}
