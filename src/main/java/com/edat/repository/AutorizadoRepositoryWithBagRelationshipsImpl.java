package com.edat.repository;

import com.edat.domain.Autorizado;
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
public class AutorizadoRepositoryWithBagRelationshipsImpl implements AutorizadoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String AUTORIZADOS_PARAMETER = "autorizados";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Autorizado> fetchBagRelationships(Optional<Autorizado> autorizado) {
        return autorizado.map(this::fetchAlumnos);
    }

    @Override
    public Page<Autorizado> fetchBagRelationships(Page<Autorizado> autorizados) {
        return new PageImpl<>(fetchBagRelationships(autorizados.getContent()), autorizados.getPageable(), autorizados.getTotalElements());
    }

    @Override
    public List<Autorizado> fetchBagRelationships(List<Autorizado> autorizados) {
        return Optional.of(autorizados).map(this::fetchAlumnos).orElse(Collections.emptyList());
    }

    Autorizado fetchAlumnos(Autorizado result) {
        return entityManager
            .createQuery(
                "select autorizado from Autorizado autorizado left join fetch autorizado.alumnos where autorizado.id = :id",
                Autorizado.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Autorizado> fetchAlumnos(List<Autorizado> autorizados) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, autorizados.size()).forEach(index -> order.put(autorizados.get(index).getId(), index));
        List<Autorizado> result = entityManager
            .createQuery(
                "select autorizado from Autorizado autorizado left join fetch autorizado.alumnos where autorizado in :autorizados",
                Autorizado.class
            )
            .setParameter(AUTORIZADOS_PARAMETER, autorizados)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
