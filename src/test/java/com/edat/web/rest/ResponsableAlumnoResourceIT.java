package com.edat.web.rest;

import static com.edat.domain.ResponsableAlumnoAsserts.*;
import static com.edat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.edat.IntegrationTest;
import com.edat.domain.ResponsableAlumno;
import com.edat.domain.User;
import com.edat.repository.ResponsableAlumnoRepository;
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
 * Integration tests for the {@link ResponsableAlumnoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResponsableAlumnoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final Long DEFAULT_DNI = 1L;
    private static final Long UPDATED_DNI = 2L;

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsable-alumnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResponsableAlumnoRepository responsableAlumnoRepository;

    @Mock
    private ResponsableAlumnoRepository responsableAlumnoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponsableAlumnoMockMvc;

    private ResponsableAlumno responsableAlumno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsableAlumno createEntity(EntityManager em) {
        ResponsableAlumno responsableAlumno = new ResponsableAlumno()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .dni(DEFAULT_DNI)
            .telefono(DEFAULT_TELEFONO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        responsableAlumno.setUser(user);
        return responsableAlumno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsableAlumno createUpdatedEntity(EntityManager em) {
        ResponsableAlumno responsableAlumno = new ResponsableAlumno()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dni(UPDATED_DNI)
            .telefono(UPDATED_TELEFONO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        responsableAlumno.setUser(user);
        return responsableAlumno;
    }

    @BeforeEach
    public void initTest() {
        responsableAlumno = createEntity(em);
    }

    @Test
    @Transactional
    void createResponsableAlumno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResponsableAlumno
        var returnedResponsableAlumno = om.readValue(
            restResponsableAlumnoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsableAlumno)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResponsableAlumno.class
        );

        // Validate the ResponsableAlumno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResponsableAlumnoUpdatableFieldsEquals(returnedResponsableAlumno, getPersistedResponsableAlumno(returnedResponsableAlumno));
    }

    @Test
    @Transactional
    void createResponsableAlumnoWithExistingId() throws Exception {
        // Create the ResponsableAlumno with an existing ID
        responsableAlumno.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsableAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsableAlumno)))
            .andExpect(status().isBadRequest());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResponsableAlumnos() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        // Get all the responsableAlumnoList
        restResponsableAlumnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsableAlumno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI.intValue())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResponsableAlumnosWithEagerRelationshipsIsEnabled() throws Exception {
        when(responsableAlumnoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResponsableAlumnoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(responsableAlumnoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResponsableAlumnosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(responsableAlumnoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResponsableAlumnoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(responsableAlumnoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getResponsableAlumno() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        // Get the responsableAlumno
        restResponsableAlumnoMockMvc
            .perform(get(ENTITY_API_URL_ID, responsableAlumno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responsableAlumno.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI.intValue()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingResponsableAlumno() throws Exception {
        // Get the responsableAlumno
        restResponsableAlumnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponsableAlumno() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsableAlumno
        ResponsableAlumno updatedResponsableAlumno = responsableAlumnoRepository.findById(responsableAlumno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResponsableAlumno are not directly saved in db
        em.detach(updatedResponsableAlumno);
        updatedResponsableAlumno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).dni(UPDATED_DNI).telefono(UPDATED_TELEFONO);

        restResponsableAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResponsableAlumno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedResponsableAlumno))
            )
            .andExpect(status().isOk());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResponsableAlumnoToMatchAllProperties(updatedResponsableAlumno);
    }

    @Test
    @Transactional
    void putNonExistingResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsableAlumno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsableAlumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsableAlumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsableAlumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponsableAlumnoWithPatch() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsableAlumno using partial update
        ResponsableAlumno partialUpdatedResponsableAlumno = new ResponsableAlumno();
        partialUpdatedResponsableAlumno.setId(responsableAlumno.getId());

        partialUpdatedResponsableAlumno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).dni(UPDATED_DNI);

        restResponsableAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsableAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsableAlumno))
            )
            .andExpect(status().isOk());

        // Validate the ResponsableAlumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsableAlumnoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResponsableAlumno, responsableAlumno),
            getPersistedResponsableAlumno(responsableAlumno)
        );
    }

    @Test
    @Transactional
    void fullUpdateResponsableAlumnoWithPatch() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsableAlumno using partial update
        ResponsableAlumno partialUpdatedResponsableAlumno = new ResponsableAlumno();
        partialUpdatedResponsableAlumno.setId(responsableAlumno.getId());

        partialUpdatedResponsableAlumno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).dni(UPDATED_DNI).telefono(UPDATED_TELEFONO);

        restResponsableAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsableAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsableAlumno))
            )
            .andExpect(status().isOk());

        // Validate the ResponsableAlumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsableAlumnoUpdatableFieldsEquals(
            partialUpdatedResponsableAlumno,
            getPersistedResponsableAlumno(partialUpdatedResponsableAlumno)
        );
    }

    @Test
    @Transactional
    void patchNonExistingResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responsableAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsableAlumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsableAlumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponsableAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsableAlumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsableAlumnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(responsableAlumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsableAlumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponsableAlumno() throws Exception {
        // Initialize the database
        responsableAlumnoRepository.saveAndFlush(responsableAlumno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the responsableAlumno
        restResponsableAlumnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, responsableAlumno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return responsableAlumnoRepository.count();
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

    protected ResponsableAlumno getPersistedResponsableAlumno(ResponsableAlumno responsableAlumno) {
        return responsableAlumnoRepository.findById(responsableAlumno.getId()).orElseThrow();
    }

    protected void assertPersistedResponsableAlumnoToMatchAllProperties(ResponsableAlumno expectedResponsableAlumno) {
        assertResponsableAlumnoAllPropertiesEquals(expectedResponsableAlumno, getPersistedResponsableAlumno(expectedResponsableAlumno));
    }

    protected void assertPersistedResponsableAlumnoToMatchUpdatableProperties(ResponsableAlumno expectedResponsableAlumno) {
        assertResponsableAlumnoAllUpdatablePropertiesEquals(
            expectedResponsableAlumno,
            getPersistedResponsableAlumno(expectedResponsableAlumno)
        );
    }
}
