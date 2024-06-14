package com.edat.repository;

import com.edat.domain.Baneados;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BaneadosRepositoryWithBagRelationships {
    Optional<Baneados> fetchBagRelationships(Optional<Baneados> baneados);

    List<Baneados> fetchBagRelationships(List<Baneados> baneados);

    Page<Baneados> fetchBagRelationships(Page<Baneados> baneados);
}
