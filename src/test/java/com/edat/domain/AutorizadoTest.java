package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.AutorizadoTestSamples.*;
import static com.edat.domain.HistorialTestSamples.*;
import static com.edat.domain.ResponsableAlumnoTestSamples.*;
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

    @Test
    void responsableAlumnoTest() throws Exception {
        Autorizado autorizado = getAutorizadoRandomSampleGenerator();
        ResponsableAlumno responsableAlumnoBack = getResponsableAlumnoRandomSampleGenerator();

        autorizado.addResponsableAlumno(responsableAlumnoBack);
        assertThat(autorizado.getResponsableAlumnos()).containsOnly(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAutorizados()).containsOnly(autorizado);

        autorizado.removeResponsableAlumno(responsableAlumnoBack);
        assertThat(autorizado.getResponsableAlumnos()).doesNotContain(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAutorizados()).doesNotContain(autorizado);

        autorizado.responsableAlumnos(new HashSet<>(Set.of(responsableAlumnoBack)));
        assertThat(autorizado.getResponsableAlumnos()).containsOnly(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAutorizados()).containsOnly(autorizado);

        autorizado.setResponsableAlumnos(new HashSet<>());
        assertThat(autorizado.getResponsableAlumnos()).doesNotContain(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAutorizados()).doesNotContain(autorizado);
    }

    @Test
    void historialTest() throws Exception {
        Autorizado autorizado = getAutorizadoRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        autorizado.addHistorial(historialBack);
        assertThat(autorizado.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getAutorizado()).isEqualTo(autorizado);

        autorizado.removeHistorial(historialBack);
        assertThat(autorizado.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getAutorizado()).isNull();

        autorizado.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(autorizado.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getAutorizado()).isEqualTo(autorizado);

        autorizado.setHistorials(new HashSet<>());
        assertThat(autorizado.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getAutorizado()).isNull();
    }
}
