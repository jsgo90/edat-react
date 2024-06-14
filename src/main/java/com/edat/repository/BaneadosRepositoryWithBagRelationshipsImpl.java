package com.edat.repository;

import com.edat.domain.Baneados;
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
public class BaneadosRepositoryWithBagRelationshipsImpl implements BaneadosRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String BANEADOS_PARAMETER = "baneados";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Baneados> fetchBagRelationships(Optional<Baneados> baneados) {
        return baneados.map(this::fetchAlumnos);
    }

    @Override
    public Page<Baneados> fetchBagRelationships(Page<Baneados> baneados) {
        return new PageImpl<>(fetchBagRelationships(baneados.getContent()), baneados.getPageable(), baneados.getTotalElements());
    }

    @Override
    public List<Baneados> fetchBagRelationships(List<Baneados> baneados) {
        return Optional.of(baneados).map(this::fetchAlumnos).orElse(Collections.emptyList());
    }

    Baneados fetchAlumnos(Baneados result) {
        return entityManager
            .createQuery("select baneados from Baneados baneados left join fetch baneados.alumnos where baneados.id = :id", Baneados.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Baneados> fetchAlumnos(List<Baneados> baneados) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, baneados.size()).forEach(index -> order.put(baneados.get(index).getId(), index));
        List<Baneados> result = entityManager
            .createQuery(
                "select baneados from Baneados baneados left join fetch baneados.alumnos where baneados in :baneados",
                Baneados.class
            )
            .setParameter(BANEADOS_PARAMETER, baneados)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
