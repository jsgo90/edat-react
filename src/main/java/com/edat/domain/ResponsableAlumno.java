package com.edat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ResponsableAlumno.
 */
@Entity
@Table(name = "responsable_alumno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponsableAlumno implements Serializable {

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_responsable_alumno__alumno",
        joinColumns = @JoinColumn(name = "responsable_alumno_id"),
        inverseJoinColumns = @JoinColumn(name = "alumno_id")
    )
    @JsonIgnoreProperties(value = { "responsableAlumnos", "autorizados" }, allowSetters = true)
    private Set<Alumno> alumnos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_responsable_alumno__autorizado",
        joinColumns = @JoinColumn(name = "responsable_alumno_id"),
        inverseJoinColumns = @JoinColumn(name = "autorizado_id")
    )
    @JsonIgnoreProperties(value = { "alumnos", "responsableAlumnos" }, allowSetters = true)
    private Set<Autorizado> autorizados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResponsableAlumno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ResponsableAlumno nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public ResponsableAlumno apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDni() {
        return this.dni;
    }

    public ResponsableAlumno dni(Long dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public ResponsableAlumno telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ResponsableAlumno user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Alumno> getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Set<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public ResponsableAlumno alumnos(Set<Alumno> alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public ResponsableAlumno addAlumno(Alumno alumno) {
        this.alumnos.add(alumno);
        return this;
    }

    public ResponsableAlumno removeAlumno(Alumno alumno) {
        this.alumnos.remove(alumno);
        return this;
    }

    public Set<Autorizado> getAutorizados() {
        return this.autorizados;
    }

    public void setAutorizados(Set<Autorizado> autorizados) {
        this.autorizados = autorizados;
    }

    public ResponsableAlumno autorizados(Set<Autorizado> autorizados) {
        this.setAutorizados(autorizados);
        return this;
    }

    public ResponsableAlumno addAutorizado(Autorizado autorizado) {
        this.autorizados.add(autorizado);
        return this;
    }

    public ResponsableAlumno removeAutorizado(Autorizado autorizado) {
        this.autorizados.remove(autorizado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponsableAlumno)) {
            return false;
        }
        return getId() != null && getId().equals(((ResponsableAlumno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponsableAlumno{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni=" + getDni() +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
