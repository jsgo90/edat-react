package com.edat.repository;

import com.edat.domain.ResponsableAlumno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ResponsableAlumnoRepositoryWithBagRelationships {
    Optional<ResponsableAlumno> fetchBagRelationships(Optional<ResponsableAlumno> responsableAlumno);

    List<ResponsableAlumno> fetchBagRelationships(List<ResponsableAlumno> responsableAlumnos);

    Page<ResponsableAlumno> fetchBagRelationships(Page<ResponsableAlumno> responsableAlumnos);
}
