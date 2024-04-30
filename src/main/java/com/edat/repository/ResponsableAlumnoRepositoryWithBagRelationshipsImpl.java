package com.edat.repository;

import com.edat.domain.ResponsableAlumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ResponsableAlumnoRepositoryWithBagRelationshipsImpl implements ResponsableAlumnoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RESPONSABLEALUMNOS_PARAMETER = "responsableAlumnos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ResponsableAlumno> fetchBagRelationships(Optional<ResponsableAlumno> responsableAlumno) {
        return responsableAlumno.map(this::fetchAlumnos);
    }

    @Override
    public Page<ResponsableAlumno> fetchBagRelationships(Page<ResponsableAlumno> responsableAlumnos) {
        return new PageImpl<>(
            fetchBagRelationships(responsableAlumnos.getContent()),
            responsableAlumnos.getPageable(),
            responsableAlumnos.getTotalElements()
        );
    }

    @Override
    public List<ResponsableAlumno> fetchBagRelationships(List<ResponsableAlumno> responsableAlumnos) {
        return Optional.of(responsableAlumnos).map(this::fetchAlumnos).orElse(Collections.emptyList());
    }

    ResponsableAlumno fetchAlumnos(ResponsableAlumno result) {
        return entityManager
            .createQuery(
                "select responsableAlumno from ResponsableAlumno responsableAlumno left join fetch responsableAlumno.alumnos where responsableAlumno.id = :id",
                ResponsableAlumno.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ResponsableAlumno> fetchAlumnos(List<ResponsableAlumno> responsableAlumnos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, responsableAlumnos.size()).forEach(index -> order.put(responsableAlumnos.get(index).getId(), index));
        List<ResponsableAlumno> result = entityManager
            .createQuery(
                "select responsableAlumno from ResponsableAlumno responsableAlumno left join fetch responsableAlumno.alumnos where responsableAlumno in :responsableAlumnos",
                ResponsableAlumno.class
            )
            .setParameter(RESPONSABLEALUMNOS_PARAMETER, responsableAlumnos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
