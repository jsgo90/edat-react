package com.edat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BaneadosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Baneados getBaneadosSample1() {
        return new Baneados().id(1L).dni(1L).nombre("nombre1").apellido("apellido1").motivo("motivo1");
    }

    public static Baneados getBaneadosSample2() {
        return new Baneados().id(2L).dni(2L).nombre("nombre2").apellido("apellido2").motivo("motivo2");
    }

    public static Baneados getBaneadosRandomSampleGenerator() {
        return new Baneados()
            .id(longCount.incrementAndGet())
            .dni(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .motivo(UUID.randomUUID().toString());
    }
}
