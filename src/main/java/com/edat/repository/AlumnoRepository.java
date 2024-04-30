package com.edat.repository;

import com.edat.domain.Alumno;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByDni(Long dni);
}
