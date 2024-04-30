package com.edat.repository;

import com.edat.domain.Autorizado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AutorizadoRepositoryWithBagRelationships {
    Optional<Autorizado> fetchBagRelationships(Optional<Autorizado> autorizado);

    List<Autorizado> fetchBagRelationships(List<Autorizado> autorizados);

    Page<Autorizado> fetchBagRelationships(Page<Autorizado> autorizados);
}
