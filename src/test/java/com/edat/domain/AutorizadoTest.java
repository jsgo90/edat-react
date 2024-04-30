package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.AutorizadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.edat.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AutorizadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autorizado.class);
        Autorizado autorizado1 = getAutorizadoSample1();
        Autorizado autorizado2 = new Autorizado();
        assertThat(autorizado1).isNotEqualTo(autorizado2);

        autorizado2.setId(autorizado1.getId());
        assertThat(autorizado1).isEqualTo(autorizado2);

        autorizado2 = getAutorizadoSample2();
        assertThat(autorizado1).isNotEqualTo(autorizado2);
    }

    @Test
    void alumnoTest() throws Exception {
        Autorizado autorizado = getAutorizadoRandomSampleGenerator();
        Alumno alumnoBack = getAlumnoRandomSampleGenerator();

        autorizado.addAlumno(alumnoBack);
        assertThat(autorizado.getAlumnos()).containsOnly(alumnoBack);

        autorizado.removeAlumno(alumnoBack);
        assertThat(autorizado.getAlumnos()).doesNotContain(alumnoBack);

        autorizado.alumnos(new HashSet<>(Set.of(alumnoBack)));
        assertThat(autorizado.getAlumnos()).containsOnly(alumnoBack);

        autorizado.setAlumnos(new HashSet<>());
        assertThat(autorizado.getAlumnos()).doesNotContain(alumnoBack);
    }
}
