package com.edat.repository;

import com.edat.domain.ResponsableAlumno;
import com.edat.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResponsableAlumno entity.
 *
 * When extending this class, extend ResponsableAlumnoRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ResponsableAlumnoRepository
    extends ResponsableAlumnoRepositoryWithBagRelationships, JpaRepository<ResponsableAlumno, Long> {
    default Optional<ResponsableAlumno> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ResponsableAlumno> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ResponsableAlumno> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select responsableAlumno from ResponsableAlumno responsableAlumno left join fetch responsableAlumno.user",
        countQuery = "select count(responsableAlumno) from ResponsableAlumno responsableAlumno"
    )
    Page<ResponsableAlumno> findAllWithToOneRelationships(Pageable pageable);

    @Query("select responsableAlumno from ResponsableAlumno responsableAlumno left join fetch responsableAlumno.user")
    List<ResponsableAlumno> findAllWithToOneRelationships();

    @Query(
        "select responsableAlumno from ResponsableAlumno responsableAlumno left join fetch responsableAlumno.user where responsableAlumno.id =:id"
    )
    Optional<ResponsableAlumno> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<ResponsableAlumno> findByDni(Long dni);

    @Query("SELECT ra FROM ResponsableAlumno ra LEFT JOIN FETCH ra.alumnos LEFT JOIN FETCH ra.autorizados WHERE ra.user.id = :userId")
    Optional<ResponsableAlumno> findByUserId(@Param("userId") Long userId);
}
