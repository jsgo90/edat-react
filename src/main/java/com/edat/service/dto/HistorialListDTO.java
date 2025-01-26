package com.edat.service.dto;

import java.time.ZonedDateTime;

public class HistorialListDTO {

    private ZonedDateTime fecha;
    private String nombre_autorizado;
    private String apellido_autorizado;

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombre_autorizado() {
        return nombre_autorizado;
    }

    public void setNombre_autorizado(String nombre_autorizado) {
        this.nombre_autorizado = nombre_autorizado;
    }

    public String getApellido_autorizado() {
        return apellido_autorizado;
    }

    public void setApellido_autorizado(String apellido_autorizado) {
        this.apellido_autorizado = apellido_autorizado;
    }

    public HistorialListDTO() {
        super();
    }

    public HistorialListDTO(ZonedDateTime fecha, String nombre_autorizado, String apellido_autorizado) {
        super();
        this.fecha = fecha;
        this.nombre_autorizado = nombre_autorizado;
        this.apellido_autorizado = apellido_autorizado;
    }
}
