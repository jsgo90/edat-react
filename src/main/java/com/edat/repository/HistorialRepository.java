package com.edat.repository;

import com.edat.domain.Historial;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Historial entity.
 */
@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
    default Optional<Historial> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Historial> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Historial> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select historial from Historial historial left join fetch historial.alumno left join fetch historial.autorizado",
        countQuery = "select count(historial) from Historial historial"
    )
    Page<Historial> findAllWithToOneRelationships(Pageable pageable);

    @Query("select historial from Historial historial left join fetch historial.alumno left join fetch historial.autorizado")
    List<Historial> findAllWithToOneRelationships();

    @Query(
        "select historial from Historial historial left join fetch historial.alumno left join fetch historial.autorizado where historial.id =:id"
    )
    Optional<Historial> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("SELECT h FROM Historial h WHERE h.alumno.id = :alumnoId ORDER BY h.fecha DESC")
    Page<Historial> findByAlumnoIdOrderByFechaDesc(Long alumnoId, Pageable pageable);
    //@Query("SELECT h FROM Historial h WHERE h.alumno.id = :alumnoId ORDER BY h.fecha DESC")
    //List<Historial> findByAlumnoIdOrderByFechaDesc(@Param("alumnoId") Long alumnoId);
}
