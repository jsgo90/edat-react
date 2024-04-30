package com.edat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlumnoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Alumno getAlumnoSample1() {
        return new Alumno().id(1L).nombre("nombre1").apellido("apellido1").dni(1L);
    }

    public static Alumno getAlumnoSample2() {
        return new Alumno().id(2L).nombre("nombre2").apellido("apellido2").dni(2L);
    }

    public static Alumno getAlumnoRandomSampleGenerator() {
        return new Alumno()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .dni(longCount.incrementAndGet());
    }
}
