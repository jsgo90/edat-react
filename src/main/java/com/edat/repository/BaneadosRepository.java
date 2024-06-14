package com.edat.repository;

import com.edat.domain.Baneados;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Baneados entity.
 *
 * When extending this class, extend BaneadosRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface BaneadosRepository extends BaneadosRepositoryWithBagRelationships, JpaRepository<Baneados, Long> {
    default Optional<Baneados> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Baneados> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Baneados> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query("SELECT b FROM Baneados b JOIN b.alumnos a WHERE a.id = :alumnoId")
    List<Baneados> findByAlumnoId(@Param("alumnoId") Long alumnoId);
}
