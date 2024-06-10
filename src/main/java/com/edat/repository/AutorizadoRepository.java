package com.edat.repository;

import com.edat.domain.Autorizado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Autorizado entity.
 *
 * When extending this class, extend AutorizadoRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AutorizadoRepository extends AutorizadoRepositoryWithBagRelationships, JpaRepository<Autorizado, Long> {
    default Optional<Autorizado> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Autorizado> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Autorizado> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query("SELECT a FROM Autorizado a LEFT JOIN FETCH a.alumnos WHERE a.dni = :dni")
    Optional<Autorizado> findByDniWithAlumnos(@Param("dni") Long dni);
}
