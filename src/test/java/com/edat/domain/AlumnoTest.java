package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.AutorizadoTestSamples.*;
import static com.edat.domain.BaneadosTestSamples.*;
import static com.edat.domain.HistorialTestSamples.*;
import static com.edat.domain.ResponsableAlumnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.edat.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlumnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alumno.class);
        Alumno alumno1 = getAlumnoSample1();
        Alumno alumno2 = new Alumno();
        assertThat(alumno1).isNotEqualTo(alumno2);

        alumno2.setId(alumno1.getId());
        assertThat(alumno1).isEqualTo(alumno2);

        alumno2 = getAlumnoSample2();
        assertThat(alumno1).isNotEqualTo(alumno2);
    }

    @Test
    void responsableAlumnoTest() throws Exception {
        Alumno alumno = getAlumnoRandomSampleGenerator();
        ResponsableAlumno responsableAlumnoBack = getResponsableAlumnoRandomSampleGenerator();

        alumno.addResponsableAlumno(responsableAlumnoBack);
        assertThat(alumno.getResponsableAlumnos()).containsOnly(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAlumnos()).containsOnly(alumno);

        alumno.removeResponsableAlumno(responsableAlumnoBack);
        assertThat(alumno.getResponsableAlumnos()).doesNotContain(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAlumnos()).doesNotContain(alumno);

        alumno.responsableAlumnos(new HashSet<>(Set.of(responsableAlumnoBack)));
        assertThat(alumno.getResponsableAlumnos()).containsOnly(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAlumnos()).containsOnly(alumno);

        alumno.setResponsableAlumnos(new HashSet<>());
        assertThat(alumno.getResponsableAlumnos()).doesNotContain(responsableAlumnoBack);
        assertThat(responsableAlumnoBack.getAlumnos()).doesNotContain(alumno);
    }

    @Test
    void autorizadoTest() throws Exception {
        Alumno alumno = getAlumnoRandomSampleGenerator();
        Autorizado autorizadoBack = getAutorizadoRandomSampleGenerator();

        alumno.addAutorizado(autorizadoBack);
        assertThat(alumno.getAutorizados()).containsOnly(autorizadoBack);
        assertThat(autorizadoBack.getAlumnos()).containsOnly(alumno);

        alumno.removeAutorizado(autorizadoBack);
        assertThat(alumno.getAutorizados()).doesNotContain(autorizadoBack);
        assertThat(autorizadoBack.getAlumnos()).doesNotContain(alumno);

        alumno.autorizados(new HashSet<>(Set.of(autorizadoBack)));
        assertThat(alumno.getAutorizados()).containsOnly(autorizadoBack);
        assertThat(autorizadoBack.getAlumnos()).containsOnly(alumno);

        alumno.setAutorizados(new HashSet<>());
        assertThat(alumno.getAutorizados()).doesNotContain(autorizadoBack);
        assertThat(autorizadoBack.getAlumnos()).doesNotContain(alumno);
    }

    @Test
    void historialTest() throws Exception {
        Alumno alumno = getAlumnoRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        alumno.addHistorial(historialBack);
        assertThat(alumno.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getAlumno()).isEqualTo(alumno);

        alumno.removeHistorial(historialBack);
        assertThat(alumno.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getAlumno()).isNull();

        alumno.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(alumno.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getAlumno()).isEqualTo(alumno);

        alumno.setHistorials(new HashSet<>());
        assertThat(alumno.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getAlumno()).isNull();
    }

    @Test
    void baneadosTest() throws Exception {
        Alumno alumno = getAlumnoRandomSampleGenerator();
        Baneados baneadosBack = getBaneadosRandomSampleGenerator();

        alumno.addBaneados(baneadosBack);
        assertThat(alumno.getBaneados()).containsOnly(baneadosBack);
        assertThat(baneadosBack.getAlumnos()).containsOnly(alumno);

        alumno.removeBaneados(baneadosBack);
        assertThat(alumno.getBaneados()).doesNotContain(baneadosBack);
        assertThat(baneadosBack.getAlumnos()).doesNotContain(alumno);

        alumno.baneados(new HashSet<>(Set.of(baneadosBack)));
        assertThat(alumno.getBaneados()).containsOnly(baneadosBack);
        assertThat(baneadosBack.getAlumnos()).containsOnly(alumno);

        alumno.setBaneados(new HashSet<>());
        assertThat(alumno.getBaneados()).doesNotContain(baneadosBack);
        assertThat(baneadosBack.getAlumnos()).doesNotContain(alumno);
    }
}
