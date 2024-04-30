package com.edat.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlumnoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlumnoAllPropertiesEquals(Alumno expected, Alumno actual) {
        assertAlumnoAutoGeneratedPropertiesEquals(expected, actual);
        assertAlumnoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlumnoAllUpdatablePropertiesEquals(Alumno expected, Alumno actual) {
        assertAlumnoUpdatableFieldsEquals(expected, actual);
        assertAlumnoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlumnoAutoGeneratedPropertiesEquals(Alumno expected, Alumno actual) {
        assertThat(expected)
            .as("Verify Alumno auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlumnoUpdatableFieldsEquals(Alumno expected, Alumno actual) {
        assertThat(expected)
            .as("Verify Alumno relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getApellido()).as("check apellido").isEqualTo(actual.getApellido()))
            .satisfies(e -> assertThat(e.getDni()).as("check dni").isEqualTo(actual.getDni()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlumnoUpdatableRelationshipsEquals(Alumno expected, Alumno actual) {}
}
