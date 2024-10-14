package com.edat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Historial.
 */
@Entity
@Table(name = "historial")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Historial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @Lob
    @Column(name = "autorizado_dni")
    private byte[] autorizado_dni;

    @Column(name = "autorizado_dni_content_type")
    private String autorizado_dniContentType;

    @Lob
    @Column(name = "autorizado_rostro")
    private byte[] autorizado_rostro;

    @Column(name = "autorizado_rostro_content_type")
    private String autorizado_rostroContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responsableAlumnos", "autorizados", "historials", "baneados" }, allowSetters = true)
    private Alumno alumno;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "alumnos", "responsableAlumnos", "historials" }, allowSetters = true)
    private Autorizado autorizado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Historial id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFecha() {
        return this.fecha;
    }

    public Historial fecha(ZonedDateTime fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public byte[] getAutorizado_dni() {
        return this.autorizado_dni;
    }

    public Historial autorizado_dni(byte[] autorizado_dni) {
        this.setAutorizado_dni(autorizado_dni);
        return this;
    }

    public void setAutorizado_dni(byte[] autorizado_dni) {
        this.autorizado_dni = autorizado_dni;
    }

    public String getAutorizado_dniContentType() {
        return this.autorizado_dniContentType;
    }

    public Historial autorizado_dniContentType(String autorizado_dniContentType) {
        this.autorizado_dniContentType = autorizado_dniContentType;
        return this;
    }

    public void setAutorizado_dniContentType(String autorizado_dniContentType) {
        this.autorizado_dniContentType = autorizado_dniContentType;
    }

    public byte[] getAutorizado_rostro() {
        return this.autorizado_rostro;
    }

    public Historial autorizado_rostro(byte[] autorizado_rostro) {
        this.setAutorizado_rostro(autorizado_rostro);
        return this;
    }

    public void setAutorizado_rostro(byte[] autorizado_rostro) {
        this.autorizado_rostro = autorizado_rostro;
    }

    public String getAutorizado_rostroContentType() {
        return this.autorizado_rostroContentType;
    }

    public Historial autorizado_rostroContentType(String autorizado_rostroContentType) {
        this.autorizado_rostroContentType = autorizado_rostroContentType;
        return this;
    }

    public void setAutorizado_rostroContentType(String autorizado_rostroContentType) {
        this.autorizado_rostroContentType = autorizado_rostroContentType;
    }

    public Alumno getAlumno() {
        return this.alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Historial alumno(Alumno alumno) {
        this.setAlumno(alumno);
        return this;
    }

    public Autorizado getAutorizado() {
        return this.autorizado;
    }

    public void setAutorizado(Autorizado autorizado) {
        this.autorizado = autorizado;
    }

    public Historial autorizado(Autorizado autorizado) {
        this.setAutorizado(autorizado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historial)) {
            return false;
        }
        return getId() != null && getId().equals(((Historial) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historial{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", autorizado_dni='" + getAutorizado_dni() + "'" +
            ", autorizado_dniContentType='" + getAutorizado_dniContentType() + "'" +
            ", autorizado_rostro='" + getAutorizado_rostro() + "'" +
            ", autorizado_rostroContentType='" + getAutorizado_rostroContentType() + "'" +
            "}";
    }
}
