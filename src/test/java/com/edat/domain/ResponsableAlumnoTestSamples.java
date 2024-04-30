package com.edat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResponsableAlumnoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ResponsableAlumno getResponsableAlumnoSample1() {
        return new ResponsableAlumno().id(1L).nombre("nombre1").apellido("apellido1").dni(1L).telefono("telefono1");
    }

    public static ResponsableAlumno getResponsableAlumnoSample2() {
        return new ResponsableAlumno().id(2L).nombre("nombre2").apellido("apellido2").dni(2L).telefono("telefono2");
    }

    public static ResponsableAlumno getResponsableAlumnoRandomSampleGenerator() {
        return new ResponsableAlumno()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .dni(longCount.incrementAndGet())
            .telefono(UUID.randomUUID().toString());
    }
}
