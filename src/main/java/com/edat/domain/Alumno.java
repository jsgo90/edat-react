package com.edat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Alumno.
 */
@Entity
@Table(name = "alumno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alumno implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "alumnos")
    @JsonIgnoreProperties(value = { "user", "alumnos", "autorizados" }, allowSetters = true)
    private Set<ResponsableAlumno> responsableAlumnos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "alumnos")
    @JsonIgnoreProperties(value = { "alumnos", "responsableAlumnos", "historials" }, allowSetters = true)
    private Set<Autorizado> autorizados = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alumno")
    @JsonIgnoreProperties(value = { "alumno", "autorizado" }, allowSetters = true)
    private Set<Historial> historials = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "alumnos")
    @JsonIgnoreProperties(value = { "alumnos" }, allowSetters = true)
    private Set<Baneados> baneados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alumno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Alumno nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Alumno apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDni() {
        return this.dni;
    }

    public Alumno dni(Long dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Set<ResponsableAlumno> getResponsableAlumnos() {
        return this.responsableAlumnos;
    }

    public void setResponsableAlumnos(Set<ResponsableAlumno> responsableAlumnos) {
        if (this.responsableAlumnos != null) {
            this.responsableAlumnos.forEach(i -> i.removeAlumno(this));
        }
        if (responsableAlumnos != null) {
            responsableAlumnos.forEach(i -> i.addAlumno(this));
        }
        this.responsableAlumnos = responsableAlumnos;
    }

    public Alumno responsableAlumnos(Set<ResponsableAlumno> responsableAlumnos) {
        this.setResponsableAlumnos(responsableAlumnos);
        return this;
    }

    public Alumno addResponsableAlumno(ResponsableAlumno responsableAlumno) {
        this.responsableAlumnos.add(responsableAlumno);
        responsableAlumno.getAlumnos().add(this);
        return this;
    }

    public Alumno removeResponsableAlumno(ResponsableAlumno responsableAlumno) {
        this.responsableAlumnos.remove(responsableAlumno);
        responsableAlumno.getAlumnos().remove(this);
        return this;
    }

    public Set<Autorizado> getAutorizados() {
        return this.autorizados;
    }

    public void setAutorizados(Set<Autorizado> autorizados) {
        if (this.autorizados != null) {
            this.autorizados.forEach(i -> i.removeAlumno(this));
        }
        if (autorizados != null) {
            autorizados.forEach(i -> i.addAlumno(this));
        }
        this.autorizados = autorizados;
    }

    public Alumno autorizados(Set<Autorizado> autorizados) {
        this.setAutorizados(autorizados);
        return this;
    }

    public Alumno addAutorizado(Autorizado autorizado) {
        this.autorizados.add(autorizado);
        autorizado.getAlumnos().add(this);
        return this;
    }

    public Alumno removeAutorizado(Autorizado autorizado) {
        this.autorizados.remove(autorizado);
        autorizado.getAlumnos().remove(this);
        return this;
    }

    public Set<Historial> getHistorials() {
        return this.historials;
    }

    public void setHistorials(Set<Historial> historials) {
        if (this.historials != null) {
            this.historials.forEach(i -> i.setAlumno(null));
        }
        if (historials != null) {
            historials.forEach(i -> i.setAlumno(this));
        }
        this.historials = historials;
    }

    public Alumno historials(Set<Historial> historials) {
        this.setHistorials(historials);
        return this;
    }

    public Alumno addHistorial(Historial historial) {
        this.historials.add(historial);
        historial.setAlumno(this);
        return this;
    }

    public Alumno removeHistorial(Historial historial) {
        this.historials.remove(historial);
        historial.setAlumno(null);
        return this;
    }

    public Set<Baneados> getBaneados() {
        return this.baneados;
    }

    public void setBaneados(Set<Baneados> baneados) {
        if (this.baneados != null) {
            this.baneados.forEach(i -> i.removeAlumnos(this));
        }
        if (baneados != null) {
            baneados.forEach(i -> i.addAlumnos(this));
        }
        this.baneados = baneados;
    }

    public Alumno baneados(Set<Baneados> baneados) {
        this.setBaneados(baneados);
        return this;
    }

    public Alumno addBaneados(Baneados baneados) {
        this.baneados.add(baneados);
        baneados.getAlumnos().add(this);
        return this;
    }

    public Alumno removeBaneados(Baneados baneados) {
        this.baneados.remove(baneados);
        baneados.getAlumnos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alumno)) {
            return false;
        }
        return getId() != null && getId().equals(((Alumno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alumno{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni=" + getDni() +
            "}";
    }
}
