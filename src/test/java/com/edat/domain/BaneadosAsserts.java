package com.edat.domain;

import static com.edat.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class BaneadosAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBaneadosAllPropertiesEquals(Baneados expected, Baneados actual) {
        assertBaneadosAutoGeneratedPropertiesEquals(expected, actual);
        assertBaneadosAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBaneadosAllUpdatablePropertiesEquals(Baneados expected, Baneados actual) {
        assertBaneadosUpdatableFieldsEquals(expected, actual);
        assertBaneadosUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBaneadosAutoGeneratedPropertiesEquals(Baneados expected, Baneados actual) {
        assertThat(expected)
            .as("Verify Baneados auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBaneadosUpdatableFieldsEquals(Baneados expected, Baneados actual) {
        assertThat(expected)
            .as("Verify Baneados relevant properties")
            .satisfies(e -> assertThat(e.getDni()).as("check dni").isEqualTo(actual.getDni()))
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getApellido()).as("check apellido").isEqualTo(actual.getApellido()))
            .satisfies(e -> assertThat(e.getMotivo()).as("check motivo").isEqualTo(actual.getMotivo()))
            .satisfies(
                e ->
                    assertThat(e.getFechaBaneo())
                        .as("check fechaBaneo")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getFechaBaneo())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBaneadosUpdatableRelationshipsEquals(Baneados expected, Baneados actual) {
        assertThat(expected)
            .as("Verify Baneados relationships")
            .satisfies(e -> assertThat(e.getAlumnos()).as("check alumnos").isEqualTo(actual.getAlumnos()));
    }
}
