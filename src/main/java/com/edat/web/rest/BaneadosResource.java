package com.edat.web.rest;

import com.edat.domain.Alumno;
import com.edat.domain.Baneados;
import com.edat.repository.AlumnoRepository;
import com.edat.repository.BaneadosRepository;
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
 * REST controller for managing {@link com.edat.domain.Baneados}.
 */
@RestController
@RequestMapping("/api/baneados")
@Transactional
public class BaneadosResource {

    private final Logger log = LoggerFactory.getLogger(BaneadosResource.class);

    private static final String ENTITY_NAME = "baneados";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaneadosRepository baneadosRepository;
    private final AlumnoRepository alumnoRepo;

    public BaneadosResource(BaneadosRepository baneadosRepository, AlumnoRepository alumnoRepo) {
        this.baneadosRepository = baneadosRepository;
        this.alumnoRepo = alumnoRepo;
    }

    /**
     * {@code POST  /baneados} : Create a new baneados.
     *
     * @param baneados the baneados to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baneados, or with status {@code 400 (Bad Request)} if the baneados has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Baneados> createBaneados(@RequestBody Baneados baneados) throws URISyntaxException {
        log.debug("REST request to save Baneados : {}", baneados);
        if (baneados.getId() != null) {
            throw new BadRequestAlertException("A new baneados cannot already have an ID", ENTITY_NAME, "idexists");
        }
        baneados = baneadosRepository.save(baneados);
        return ResponseEntity.created(new URI("/api/baneados/" + baneados.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, baneados.getId().toString()))
            .body(baneados);
    }

    /**
     * {@code PUT  /baneados/:id} : Updates an existing baneados.
     *
     * @param id the id of the baneados to save.
     * @param baneados the baneados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baneados,
     * or with status {@code 400 (Bad Request)} if the baneados is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baneados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Baneados> updateBaneados(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Baneados baneados
    ) throws URISyntaxException {
        log.debug("REST request to update Baneados : {}, {}", id, baneados);
        if (baneados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baneados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baneadosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        baneados = baneadosRepository.save(baneados);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baneados.getId().toString()))
            .body(baneados);
    }

    /**
     * {@code PATCH  /baneados/:id} : Partial updates given fields of an existing baneados, field will ignore if it is null
     *
     * @param id the id of the baneados to save.
     * @param baneados the baneados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baneados,
     * or with status {@code 400 (Bad Request)} if the baneados is not valid,
     * or with status {@code 404 (Not Found)} if the baneados is not found,
     * or with status {@code 500 (Internal Server Error)} if the baneados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Baneados> partialUpdateBaneados(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Baneados baneados
    ) throws URISyntaxException {
        log.debug("REST request to partial update Baneados partially : {}, {}", id, baneados);
        if (baneados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baneados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baneadosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Baneados> result = baneadosRepository
            .findById(baneados.getId())
            .map(existingBaneados -> {
                if (baneados.getDni() != null) {
                    existingBaneados.setDni(baneados.getDni());
                }
                if (baneados.getNombre() != null) {
                    existingBaneados.setNombre(baneados.getNombre());
                }
                if (baneados.getApellido() != null) {
                    existingBaneados.setApellido(baneados.getApellido());
                }
                if (baneados.getMotivo() != null) {
                    existingBaneados.setMotivo(baneados.getMotivo());
                }
                if (baneados.getFechaBaneo() != null) {
                    existingBaneados.setFechaBaneo(baneados.getFechaBaneo());
                }

                return existingBaneados;
            })
            .map(baneadosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baneados.getId().toString())
        );
    }

    /**
     * {@code GET  /baneados} : get all the baneados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baneados in body.
     */
    @GetMapping("")
    public List<Baneados> getAllBaneados(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Baneados");
        if (eagerload) {
            return baneadosRepository.findAllWithEagerRelationships();
        } else {
            return baneadosRepository.findAll();
        }
    }

    /**
     * {@code GET  /baneados/:id} : get the "id" baneados.
     *
     * @param id the id of the baneados to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baneados, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Baneados> getBaneados(@PathVariable("id") Long id) {
        log.debug("REST request to get Baneados : {}", id);
        Optional<Baneados> baneados = baneadosRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(baneados);
    }

    /**
     * {@code DELETE  /baneados/:id} : delete the "id" baneados.
     *
     * @param id the id of the baneados to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaneados(@PathVariable("id") Long id) {
        log.debug("REST request to delete Baneados : {}", id);
        baneadosRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/check")
    public ResponseEntity<Baneados> checkIfBaneado(@RequestParam("dnitocheck") Long dni, @RequestParam("idalumno") Long idAlumno) {
        Optional<Alumno> alumnoOptional = alumnoRepo.findById(idAlumno);

        if (alumnoOptional.isEmpty()) return null;

        List<Baneados> baneados = baneadosRepository.findByAlumnoId(idAlumno);

        boolean isBaneado =
            Objects.nonNull(baneados) && !baneados.isEmpty() && baneados.stream().anyMatch(baneado -> baneado.getDni().equals(dni));

        if (isBaneado) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
