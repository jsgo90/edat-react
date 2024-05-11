package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.AutorizadoTestSamples.*;
import static com.edat.domain.ResponsableAlumnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.edat.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResponsableAlumnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponsableAlumno.class);
        ResponsableAlumno responsableAlumno1 = getResponsableAlumnoSample1();
        ResponsableAlumno responsableAlumno2 = new ResponsableAlumno();
        assertThat(responsableAlumno1).isNotEqualTo(responsableAlumno2);

        responsableAlumno2.setId(responsableAlumno1.getId());
        assertThat(responsableAlumno1).isEqualTo(responsableAlumno2);

        responsableAlumno2 = getResponsableAlumnoSample2();
        assertThat(responsableAlumno1).isNotEqualTo(responsableAlumno2);
    }

    @Test
    void alumnoTest() throws Exception {
        ResponsableAlumno responsableAlumno = getResponsableAlumnoRandomSampleGenerator();
        Alumno alumnoBack = getAlumnoRandomSampleGenerator();

        responsableAlumno.addAlumno(alumnoBack);
        assertThat(responsableAlumno.getAlumnos()).containsOnly(alumnoBack);

        responsableAlumno.removeAlumno(alumnoBack);
        assertThat(responsableAlumno.getAlumnos()).doesNotContain(alumnoBack);

        responsableAlumno.alumnos(new HashSet<>(Set.of(alumnoBack)));
        assertThat(responsableAlumno.getAlumnos()).containsOnly(alumnoBack);

        responsableAlumno.setAlumnos(new HashSet<>());
        assertThat(responsableAlumno.getAlumnos()).doesNotContain(alumnoBack);
    }

    @Test
    void autorizadoTest() throws Exception {
        ResponsableAlumno responsableAlumno = getResponsableAlumnoRandomSampleGenerator();
        Autorizado autorizadoBack = getAutorizadoRandomSampleGenerator();

        responsableAlumno.addAutorizado(autorizadoBack);
        assertThat(responsableAlumno.getAutorizados()).containsOnly(autorizadoBack);

        responsableAlumno.removeAutorizado(autorizadoBack);
        assertThat(responsableAlumno.getAutorizados()).doesNotContain(autorizadoBack);

        responsableAlumno.autorizados(new HashSet<>(Set.of(autorizadoBack)));
        assertThat(responsableAlumno.getAutorizados()).containsOnly(autorizadoBack);

        responsableAlumno.setAutorizados(new HashSet<>());
        assertThat(responsableAlumno.getAutorizados()).doesNotContain(autorizadoBack);
    }
}
