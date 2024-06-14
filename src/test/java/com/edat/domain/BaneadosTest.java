package com.edat.domain;

import static com.edat.domain.AlumnoTestSamples.*;
import static com.edat.domain.BaneadosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.edat.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BaneadosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Baneados.class);
        Baneados baneados1 = getBaneadosSample1();
        Baneados baneados2 = new Baneados();
        assertThat(baneados1).isNotEqualTo(baneados2);

        baneados2.setId(baneados1.getId());
        assertThat(baneados1).isEqualTo(baneados2);

        baneados2 = getBaneadosSample2();
        assertThat(baneados1).isNotEqualTo(baneados2);
    }

    @Test
    void alumnosTest() throws Exception {
        Baneados baneados = getBaneadosRandomSampleGenerator();
        Alumno alumnoBack = getAlumnoRandomSampleGenerator();

        baneados.addAlumnos(alumnoBack);
        assertThat(baneados.getAlumnos()).containsOnly(alumnoBack);

        baneados.removeAlumnos(alumnoBack);
        assertThat(baneados.getAlumnos()).doesNotContain(alumnoBack);

        baneados.alumnos(new HashSet<>(Set.of(alumnoBack)));
        assertThat(baneados.getAlumnos()).containsOnly(alumnoBack);

        baneados.setAlumnos(new HashSet<>());
        assertThat(baneados.getAlumnos()).doesNotContain(alumnoBack);
    }
}
