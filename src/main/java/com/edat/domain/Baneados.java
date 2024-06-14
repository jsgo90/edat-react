package com.edat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Baneados.
 */
@Entity
@Table(name = "baneados")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Baneados implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dni")
    private Long dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "fecha_baneo")
    private ZonedDateTime fechaBaneo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_baneados__alumnos",
        joinColumns = @JoinColumn(name = "baneados_id"),
        inverseJoinColumns = @JoinColumn(name = "alumnos_id")
    )
    @JsonIgnoreProperties(value = { "responsableAlumnos", "autorizados", "historials", "baneados" }, allowSetters = true)
    private Set<Alumno> alumnos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Baneados id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDni() {
        return this.dni;
    }

    public Baneados dni(Long dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Baneados nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Baneados apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public Baneados motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public ZonedDateTime getFechaBaneo() {
        return this.fechaBaneo;
    }

    public Baneados fechaBaneo(ZonedDateTime fechaBaneo) {
        this.setFechaBaneo(fechaBaneo);
        return this;
    }

    public void setFechaBaneo(ZonedDateTime fechaBaneo) {
        this.fechaBaneo = fechaBaneo;
    }

    public Set<Alumno> getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public Baneados alumnos(Set<Alumno> alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public Baneados addAlumnos(Alumno alumno) {
        this.alumnos.add(alumno);
        return this;
    }

    public Baneados removeAlumnos(Alumno alumno) {
        this.alumnos.remove(alumno);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Baneados)) {
            return false;
        }
        return getId() != null && getId().equals(((Baneados) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Baneados{" +
            "id=" + getId() +
            ", dni=" + getDni() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", fechaBaneo='" + getFechaBaneo() + "'" +
            "}";
    }
}
