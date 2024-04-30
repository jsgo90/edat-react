package com.edat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Autorizado.
 */
@Entity
@Table(name = "autorizado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Autorizado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "dni")
    private Long dni;

    @Column(name = "telefono")
    private String telefono;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_autorizado__alumno",
        joinColumns = @JoinColumn(name = "autorizado_id"),
        inverseJoinColumns = @JoinColumn(name = "alumno_id")
    )
    @JsonIgnoreProperties(value = { "responsableAlumnos", "autorizados" }, allowSetters = true)
    private Set<Alumno> alumnos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Autorizado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Autorizado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Autorizado apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDni() {
        return this.dni;
    }

    public Autorizado dni(Long dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Autorizado telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Alumno> getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public Autorizado alumnos(Set<Alumno> alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public Autorizado addAlumno(Alumno alumno) {
        this.alumnos.add(alumno);
        return this;
    }

    public Autorizado removeAlumno(Alumno alumno) {
        this.alumnos.remove(alumno);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autorizado)) {
            return false;
        }
        return getId() != null && getId().equals(((Autorizado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Autorizado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni=" + getDni() +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
