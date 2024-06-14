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
    @JsonIgnoreProperties(value = { "responsableAlumnos", "autorizados", "historials", "baneados" }, allowSetters = true)
    private Set<Alumno> alumnos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "autorizados")
    @JsonIgnoreProperties(value = { "user", "alumnos", "autorizados" }, allowSetters = true)
    private Set<ResponsableAlumno> responsableAlumnos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "autorizado")
    @JsonIgnoreProperties(value = { "alumno", "autorizado" }, allowSetters = true)
    private Set<Historial> historials = new HashSet<>();

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

    public Set<ResponsableAlumno> getResponsableAlumnos() {
        return this.responsableAlumnos;
    }

    public void setResponsableAlumnos(Set<ResponsableAlumno> responsableAlumnos) {
        if (this.responsableAlumnos != null) {
            this.responsableAlumnos.forEach(i -> i.removeAutorizado(this));
        }
        if (responsableAlumnos != null) {
            responsableAlumnos.forEach(i -> i.addAutorizado(this));
        }
        this.responsableAlumnos = responsableAlumnos;
    }

    public Autorizado responsableAlumnos(Set<ResponsableAlumno> responsableAlumnos) {
        this.setResponsableAlumnos(responsableAlumnos);
        return this;
    }

    public Autorizado addResponsableAlumno(ResponsableAlumno responsableAlumno) {
        this.responsableAlumnos.add(responsableAlumno);
        responsableAlumno.getAutorizados().add(this);
        return this;
    }

    public Autorizado removeResponsableAlumno(ResponsableAlumno responsableAlumno) {
        this.responsableAlumnos.remove(responsableAlumno);
        responsableAlumno.getAutorizados().remove(this);
        return this;
    }

    public Set<Historial> getHistorials() {
        return this.historials;
    }

    public void setHistorials(Set<Historial> historials) {
        if (this.historials != null) {
            this.historials.forEach(i -> i.setAutorizado(null));
        }
        if (historials != null) {
            historials.forEach(i -> i.setAutorizado(this));
        }
        this.historials = historials;
    }

    public Autorizado historials(Set<Historial> historials) {
        this.setHistorials(historials);
        return this;
    }

    public Autorizado addHistorial(Historial historial) {
        this.historials.add(historial);
        historial.setAutorizado(this);
        return this;
    }

    public Autorizado removeHistorial(Historial historial) {
        this.historials.remove(historial);
        historial.setAutorizado(null);
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
