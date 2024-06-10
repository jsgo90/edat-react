package com.edat.web.rest;

import com.edat.domain.Alumno;
import com.edat.domain.Autorizado;
import com.edat.repository.AlumnoRepository;
import com.edat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.edat.domain.Alumno}.
 */
@RestController
@RequestMapping("/api/alumnos")
@Transactional
public class AlumnoResource {

    private final Logger log = LoggerFactory.getLogger(AlumnoResource.class);

    private static final String ENTITY_NAME = "alumno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlumnoRepository alumnoRepository;

    public AlumnoResource(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * {@code POST  /alumnos} : Create a new alumno.
     *
     * @param alumno the alumno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alumno, or with status {@code 400 (Bad Request)} if the alumno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Alumno> createAlumno(@RequestBody Alumno alumno) throws URISyntaxException {
        log.debug("REST request to save Alumno : {}", alumno);
        if (alumno.getId() != null) {
            throw new BadRequestAlertException("A new alumno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alumno = alumnoRepository.save(alumno);
        return ResponseEntity.created(new URI("/api/alumnos/" + alumno.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, alumno.getId().toString()))
            .body(alumno);
    }

    /**
     * {@code PUT  /alumnos/:id} : Updates an existing alumno.
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable(value = "id", required = false) final Long id, @RequestBody Alumno alumno)
        throws URISyntaxException {
        log.debug("REST request to update Alumno : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alumno = alumnoRepository.save(alumno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alumno.getId().toString()))
            .body(alumno);
    }

    /**
     * {@code PATCH  /alumnos/:id} : Partial updates given fields of an existing alumno, field will ignore if it is null
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 404 (Not Found)} if the alumno is not found,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alumno> partialUpdateAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alumno alumno
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alumno partially : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alumno> result = alumnoRepository
            .findById(alumno.getId())
            .map(existingAlumno -> {
                if (alumno.getNombre() != null) {
                    existingAlumno.setNombre(alumno.getNombre());
                }
                if (alumno.getApellido() != null) {
                    existingAlumno.setApellido(alumno.getApellido());
                }
                if (alumno.getDni() != null) {
                    existingAlumno.setDni(alumno.getDni());
                }

                return existingAlumno;
            })
            .map(alumnoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alumno.getId().toString())
        );
    }

    /**
     * {@code GET  /alumnos} : get all the alumnos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alumnos in body.
     */
    @GetMapping("")
    public List<Alumno> getAllAlumnos() {
        log.debug("REST request to get all Alumnos");
        return alumnoRepository.findAll();
    }

    @GetMapping("/paginated")
    public Page<Alumno> getAllAlumnosPaginated(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return alumnoRepository.findAll(pageable);
    }

    /**
     * {@code GET  /alumnos/:id} : get the "id" alumno.
     *
     * @param id the id of the alumno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alumno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumno(@PathVariable("id") Long id) {
        log.debug("REST request to get Alumno : {}", id);
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alumno);
    }

    /**
     * {@code DELETE  /alumnos/:id} : delete the "id" alumno.
     *
     * @param id the id of the alumno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable("id") Long id) {
        log.debug("REST request to delete Alumno : {}", id);
        alumnoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/{id}/autorizados")
    public Set<Autorizado> getAutorizados(@PathVariable("id") Long id) {
        log.debug("REST request to get all Alumnos");

        Optional<Alumno> alumno = alumnoRepository.findById(id);

        if (alumno.isEmpty()) return null;

        return alumno.get().getAutorizados();
    }
}
