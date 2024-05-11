package com.edat.web.rest;

import com.edat.domain.Autorizado;
import com.edat.domain.ResponsableAlumno;
import com.edat.domain.User;
import com.edat.repository.AutorizadoRepository;
import com.edat.repository.ResponsableAlumnoRepository;
import com.edat.repository.UserRepository;
import com.edat.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.edat.domain.ResponsableAlumno}.
 */
@RestController
@RequestMapping("/api/responsable-alumnos")
@Transactional
public class ResponsableAlumnoResource {

    private final Logger log = LoggerFactory.getLogger(ResponsableAlumnoResource.class);

    private static final String ENTITY_NAME = "responsableAlumno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsableAlumnoRepository responsableAlumnoRepository;
    private final AutorizadoRepository autorizadoRepository;
    private final UserRepository userRepository;

    public ResponsableAlumnoResource(
        ResponsableAlumnoRepository responsableAlumnoRepository,
        AutorizadoRepository autorizadoRepository,
        UserRepository userRepository
    ) {
        this.responsableAlumnoRepository = responsableAlumnoRepository;
        this.autorizadoRepository = autorizadoRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /responsable-alumnos} : Create a new responsableAlumno.
     *
     * @param responsableAlumno the responsableAlumno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsableAlumno, or with status {@code 400 (Bad Request)} if the responsableAlumno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ResponsableAlumno> createResponsableAlumno(@Valid @RequestBody ResponsableAlumno responsableAlumno)
        throws URISyntaxException {
        log.debug("REST request to save ResponsableAlumno : {}", responsableAlumno);
        if (responsableAlumno.getId() != null) {
            throw new BadRequestAlertException("A new responsableAlumno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        responsableAlumno = responsableAlumnoRepository.save(responsableAlumno);
        return ResponseEntity.created(new URI("/api/responsable-alumnos/" + responsableAlumno.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, responsableAlumno.getId().toString()))
            .body(responsableAlumno);
    }

    /**
     * {@code PUT  /responsable-alumnos/:id} : Updates an existing responsableAlumno.
     *
     * @param id the id of the responsableAlumno to save.
     * @param responsableAlumno the responsableAlumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsableAlumno,
     * or with status {@code 400 (Bad Request)} if the responsableAlumno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsableAlumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponsableAlumno> updateResponsableAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResponsableAlumno responsableAlumno
    ) throws URISyntaxException {
        log.debug("REST request to update ResponsableAlumno : {}, {}", id, responsableAlumno);
        if (responsableAlumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsableAlumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsableAlumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        responsableAlumno = responsableAlumnoRepository.save(responsableAlumno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, responsableAlumno.getId().toString()))
            .body(responsableAlumno);
    }

    /**
     * {@code PATCH  /responsable-alumnos/:id} : Partial updates given fields of an existing responsableAlumno, field will ignore if it is null
     *
     * @param id the id of the responsableAlumno to save.
     * @param responsableAlumno the responsableAlumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsableAlumno,
     * or with status {@code 400 (Bad Request)} if the responsableAlumno is not valid,
     * or with status {@code 404 (Not Found)} if the responsableAlumno is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsableAlumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResponsableAlumno> partialUpdateResponsableAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResponsableAlumno responsableAlumno
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResponsableAlumno partially : {}, {}", id, responsableAlumno);
        if (responsableAlumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsableAlumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsableAlumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResponsableAlumno> result = responsableAlumnoRepository
            .findById(responsableAlumno.getId())
            .map(existingResponsableAlumno -> {
                if (responsableAlumno.getNombre() != null) {
                    existingResponsableAlumno.setNombre(responsableAlumno.getNombre());
                }
                if (responsableAlumno.getApellido() != null) {
                    existingResponsableAlumno.setApellido(responsableAlumno.getApellido());
                }
                if (responsableAlumno.getDni() != null) {
                    existingResponsableAlumno.setDni(responsableAlumno.getDni());
                }
                if (responsableAlumno.getTelefono() != null) {
                    existingResponsableAlumno.setTelefono(responsableAlumno.getTelefono());
                }

                return existingResponsableAlumno;
            })
            .map(responsableAlumnoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, responsableAlumno.getId().toString())
        );
    }

    /**
     * {@code GET  /responsable-alumnos} : get all the responsableAlumnos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsableAlumnos in body.
     */
    @GetMapping("")
    public List<ResponsableAlumno> getAllResponsableAlumnos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all ResponsableAlumnos");
        if (eagerload) {
            return responsableAlumnoRepository.findAllWithEagerRelationships();
        } else {
            return responsableAlumnoRepository.findAll();
        }
    }

    /**
     * {@code GET  /responsable-alumnos/:id} : get the "id" responsableAlumno.
     *
     * @param id the id of the responsableAlumno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsableAlumno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponsableAlumno> getResponsableAlumno(@PathVariable("id") Long id) {
        log.debug("REST request to get ResponsableAlumno : {}", id);
        Optional<ResponsableAlumno> responsableAlumno = responsableAlumnoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(responsableAlumno);
    }

    /**
     * {@code DELETE  /responsable-alumnos/:id} : delete the "id" responsableAlumno.
     *
     * @param id the id of the responsableAlumno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponsableAlumno(@PathVariable("id") Long id) {
        log.debug("REST request to delete ResponsableAlumno : {}", id);
        responsableAlumnoRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponsableAlumno> getResponsableAlumnoByUserId(@PathVariable("id") Long userId) {
        log.debug("REST request to get ResponsableAlumno : {}", userId);

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<ResponsableAlumno> optionalResponsableAlumno = responsableAlumnoRepository.findByUserId(userId);

        // TODO Manejar el 404 not found como corresponde

        return ResponseUtil.wrapOrNotFound(optionalResponsableAlumno);
    }

    @GetMapping("/{id}/autorizados")
    public Set<Autorizado> getAutorizados(@PathVariable("id") Long id) {
        Optional<ResponsableAlumno> optionalResponsable = responsableAlumnoRepository.findById(id);
        if (optionalResponsable.isEmpty()) {
            return null;
        }
        Set<Autorizado> autorizados = null; //optionalResponsable.get().
        return autorizados;
    }
    //    @PostMapping("/{dni_responsable_alumno}/autorizados")
    //    public Autorizado createAutorizado(@PathVariable("dni_responsable_alumno") Long dni_autorizado, @RequestBody Autorizado autorizado) {
    //        // TODO: move la logica que vamos a ejecutar ahora a un servicio !!
    //        Optional<ResponsableAlumno> optionalResponsable = responsableAlumnoRepository.findByDni(dni_autorizado);
    //        if (optionalResponsable.isEmpty()) {
    //            return null;
    //        }
    //        ResponsableAlumno responsableAlumno = optionalResponsable.get();
    //
    //        autorizado = autorizadoRepository.save(autorizado);
    //
    //        responsableAlumno.addA(autorizado);
    //        responsableAlumno = responsableAlumnoRepository.save(responsableAlumno);
    //
    //        return autorizado;
    //    }
    //    // TODO si alguien desasigna un autorizado tengo que borrar al menos 2 registros
    //    // registro autorizado | registro autorizado dentro de responsable_alumno
    //    // registro autorizado dentro de alumno???

}
