package com.edat.web.rest;

import com.edat.domain.Alumno;
import com.edat.domain.Autorizado;
import com.edat.domain.Historial;
import com.edat.repository.AlumnoRepository;
import com.edat.repository.AutorizadoRepository;
import com.edat.repository.HistorialRepository;
import com.edat.service.dto.HistorialDTO;
import com.edat.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.edat.domain.Historial}.
 */
@RestController
@RequestMapping("/api/historials")
@Transactional
public class HistorialResource {

    private final Logger log = LoggerFactory.getLogger(HistorialResource.class);

    private static final String ENTITY_NAME = "historial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistorialRepository historialRepository;
    private final AutorizadoRepository autorizadoRepo;
    private final AlumnoRepository alumnoRepo;

    public HistorialResource(
        HistorialRepository historialRepository,
        AutorizadoRepository autorizadoRepository,
        AlumnoRepository alumnoRepository
    ) {
        this.historialRepository = historialRepository;
        this.autorizadoRepo = autorizadoRepository;
        this.alumnoRepo = alumnoRepository;
    }

    /**
     * {@code POST  /historials} : Create a new historial.
     *
     * @param historial the historial to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historial, or with status {@code 400 (Bad Request)} if the historial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Historial> createHistorial(@Valid @RequestBody Historial historial) throws URISyntaxException {
        log.debug("REST request to save Historial : {}", historial);
        if (historial.getId() != null) {
            throw new BadRequestAlertException("A new historial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historial = historialRepository.save(historial);
        return ResponseEntity.created(new URI("/api/historials/" + historial.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, historial.getId().toString()))
            .body(historial);
    }

    /**
     * {@code PUT  /historials/:id} : Updates an existing historial.
     *
     * @param id the id of the historial to save.
     * @param historial the historial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historial,
     * or with status {@code 400 (Bad Request)} if the historial is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Historial> updateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Historial historial
    ) throws URISyntaxException {
        log.debug("REST request to update Historial : {}, {}", id, historial);
        if (historial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historial = historialRepository.save(historial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historial.getId().toString()))
            .body(historial);
    }

    /**
     * {@code PATCH  /historials/:id} : Partial updates given fields of an existing historial, field will ignore if it is null
     *
     * @param id the id of the historial to save.
     * @param historial the historial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historial,
     * or with status {@code 400 (Bad Request)} if the historial is not valid,
     * or with status {@code 404 (Not Found)} if the historial is not found,
     * or with status {@code 500 (Internal Server Error)} if the historial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Historial> partialUpdateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Historial historial
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historial partially : {}, {}", id, historial);
        if (historial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Historial> result = historialRepository
            .findById(historial.getId())
            .map(existingHistorial -> {
                if (historial.getFecha() != null) {
                    existingHistorial.setFecha(historial.getFecha());
                }

                return existingHistorial;
            })
            .map(historialRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, historial.getId().toString())
        );
    }

    /**
     * {@code GET  /historials} : get all the historials.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historials in body.
     */
    @GetMapping("")
    public List<Historial> getAllHistorials(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Historials");
        if (eagerload) {
            return historialRepository.findAllWithEagerRelationships();
        } else {
            return historialRepository.findAll();
        }
    }

    /**
     * {@code GET  /historials/:id} : get the "id" historial.
     *
     * @param id the id of the historial to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historial, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Historial> getHistorial(@PathVariable("id") Long id) {
        log.debug("REST request to get Historial : {}", id);
        Optional<Historial> historial = historialRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(historial);
    }

    /**
     * {@code DELETE  /historials/:id} : delete the "id" historial.
     *
     * @param id the id of the historial to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorial(@PathVariable("id") Long id) {
        log.debug("REST request to delete Historial : {}", id);
        historialRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<Historial> registrarSalida(@Valid @RequestBody HistorialDTO historialdto) throws URISyntaxException {
        Optional<Alumno> alumnoOptional = alumnoRepo.findById(historialdto.getAlumnoId());
        Optional<Autorizado> autorizadoOptional = autorizadoRepo.findById(historialdto.getAutorizadoId());

        if (alumnoOptional.isEmpty() || autorizadoOptional.isEmpty()) return null;

        Historial historial = new Historial();
        historial.setAlumno(alumnoOptional.get());
        historial.setAutorizado(autorizadoOptional.get());

        ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");
        ZonedDateTime fechaRegistro = ZonedDateTime.now(zoneId);

        historial.setFecha(fechaRegistro);

        historial = historialRepository.save(historial);
        return ResponseEntity.created(new URI("/api/historials/" + historial.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, historial.getId().toString()))
            .body(historial);
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<Page<Historial>> getHistorialesPorAlumno(
        @PathVariable Long alumnoId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Historial> historiales = historialRepository.findByAlumnoIdOrderByFechaDesc(alumnoId, pageable);

        return ResponseEntity.ok(historiales);
    }
}
