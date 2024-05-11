package com.edat.web.rest;

import com.edat.domain.Alumno;
import com.edat.domain.Autorizado;
import com.edat.repository.AlumnoRepository;
import com.edat.repository.AutorizadoRepository;
import com.edat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.edat.domain.Autorizado}.
 */
@RestController
@RequestMapping("/api/autorizados")
@Transactional
public class AutorizadoResource {

    private final Logger log = LoggerFactory.getLogger(AutorizadoResource.class);

    private static final String ENTITY_NAME = "autorizado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutorizadoRepository autorizadoRepository;

    private final AlumnoRepository alumnoRepository;

    public AutorizadoResource(AutorizadoRepository autorizadoRepository, AlumnoRepository alumnoRepository) {
        this.autorizadoRepository = autorizadoRepository;
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * {@code POST  /autorizados} : Create a new autorizado.
     *
     * @param autorizado the autorizado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autorizado, or with status {@code 400 (Bad Request)} if the autorizado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Autorizado> createAutorizado(@RequestBody Autorizado autorizado) throws URISyntaxException {
        log.debug("REST request to save Autorizado : {}", autorizado);
        if (autorizado.getId() != null) {
            throw new BadRequestAlertException("A new autorizado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autorizado = autorizadoRepository.save(autorizado);
        return ResponseEntity.created(new URI("/api/autorizados/" + autorizado.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, autorizado.getId().toString()))
            .body(autorizado);
    }

    /**
     * {@code PUT  /autorizados/:id} : Updates an existing autorizado.
     *
     * @param id the id of the autorizado to save.
     * @param autorizado the autorizado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autorizado,
     * or with status {@code 400 (Bad Request)} if the autorizado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autorizado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Autorizado> updateAutorizado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Autorizado autorizado
    ) throws URISyntaxException {
        log.debug("REST request to update Autorizado : {}, {}", id, autorizado);
        if (autorizado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autorizado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorizadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autorizado = autorizadoRepository.save(autorizado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, autorizado.getId().toString()))
            .body(autorizado);
    }

    /**
     * {@code PATCH  /autorizados/:id} : Partial updates given fields of an existing autorizado, field will ignore if it is null
     *
     * @param id the id of the autorizado to save.
     * @param autorizado the autorizado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autorizado,
     * or with status {@code 400 (Bad Request)} if the autorizado is not valid,
     * or with status {@code 404 (Not Found)} if the autorizado is not found,
     * or with status {@code 500 (Internal Server Error)} if the autorizado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Autorizado> partialUpdateAutorizado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Autorizado autorizado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Autorizado partially : {}, {}", id, autorizado);
        if (autorizado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autorizado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorizadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Autorizado> result = autorizadoRepository
            .findById(autorizado.getId())
            .map(existingAutorizado -> {
                if (autorizado.getNombre() != null) {
                    existingAutorizado.setNombre(autorizado.getNombre());
                }
                if (autorizado.getApellido() != null) {
                    existingAutorizado.setApellido(autorizado.getApellido());
                }
                if (autorizado.getDni() != null) {
                    existingAutorizado.setDni(autorizado.getDni());
                }
                if (autorizado.getTelefono() != null) {
                    existingAutorizado.setTelefono(autorizado.getTelefono());
                }

                return existingAutorizado;
            })
            .map(autorizadoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, autorizado.getId().toString())
        );
    }

    /**
     * {@code GET  /autorizados} : get all the autorizados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autorizados in body.
     */
    @GetMapping("")
    public List<Autorizado> getAllAutorizados(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Autorizados");
        if (eagerload) {
            return autorizadoRepository.findAllWithEagerRelationships();
        } else {
            return autorizadoRepository.findAll();
        }
    }

    /**
     * {@code GET  /autorizados/:id} : get the "id" autorizado.
     *
     * @param id the id of the autorizado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autorizado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Autorizado> getAutorizado(@PathVariable("id") Long id) {
        log.debug("REST request to get Autorizado : {}", id);
        Optional<Autorizado> autorizado = autorizadoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(autorizado);
    }

    /**
     * {@code DELETE  /autorizados/:id} : delete the "id" autorizado.
     *
     * @param id the id of the autorizado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutorizado(@PathVariable("id") Long id) {
        log.debug("REST request to delete Autorizado : {}", id);
        autorizadoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/{id_autorizado}/alumnos")
    public ResponseEntity<String> asignarAlumno(@PathVariable("id_autorizado") Long idAutorizado, @RequestBody Long idAlumno)
        throws URISyntaxException {
        Optional<Autorizado> autorizadoOptional = autorizadoRepository.findById(idAutorizado);
        Optional<Alumno> alumnoOptional = alumnoRepository.findById(idAlumno);

        if (autorizadoOptional.isPresent() && alumnoOptional.isPresent()) {
            Autorizado autorizado = autorizadoOptional.get();
            Alumno alumno = alumnoOptional.get();

            autorizado.addAlumno(alumno);
            autorizado = autorizadoRepository.save(autorizado);

            alumno.addAutorizado(autorizado);
            alumno = alumnoRepository.save(alumno);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autorizado o alumno no encontrado");
        }
    }

    @DeleteMapping("/{id_autorizado}/alumnos/{id_alumno}")
    public ResponseEntity<String> desasignarAlumno(
        @PathVariable("id_autorizado") Long idAutorizado,
        @PathVariable("id_alumno") Long idAlumno
    ) {
        Optional<Autorizado> autorizadoOptional = autorizadoRepository.findById(idAutorizado);
        Optional<Alumno> alumnoOptional = alumnoRepository.findById(idAlumno);

        if (autorizadoOptional.isPresent() && alumnoOptional.isPresent()) {
            Autorizado autorizado = autorizadoOptional.get();
            Alumno alumno = alumnoOptional.get();

            autorizado.removeAlumno(alumno);
            autorizado = autorizadoRepository.save(autorizado);

            alumno.removeAutorizado(autorizado);
            alumno = alumnoRepository.save(alumno);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autorizado o alumno no encontrado");
        }
    }
}
