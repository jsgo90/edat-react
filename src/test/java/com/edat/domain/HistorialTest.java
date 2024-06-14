package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.AutorizadoTestSamples.*;
import static com.edat.domain.HistorialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.edat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historial.class);
        Historial historial1 = getHistorialSample1();
        Historial historial2 = new Historial();
        assertThat(historial1).isNotEqualTo(historial2);

        historial2.setId(historial1.getId());
        assertThat(historial1).isEqualTo(historial2);

        historial2 = getHistorialSample2();
        assertThat(historial1).isNotEqualTo(historial2);
    }

    @Test
    void alumnoTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Alumno alumnoBack = getAlumnoRandomSampleGenerator();

        historial.setAlumno(alumnoBack);
        assertThat(historial.getAlumno()).isEqualTo(alumnoBack);

        historial.alumno(null);
        assertThat(historial.getAlumno()).isNull();
    }

    @Test
    void autorizadoTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Autorizado autorizadoBack = getAutorizadoRandomSampleGenerator();

        historial.setAutorizado(autorizadoBack);
        assertThat(historial.getAutorizado()).isEqualTo(autorizadoBack);

        historial.autorizado(null);
        assertThat(historial.getAutorizado()).isNull();
    }
}
