package com.edat.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OcrResults {

    @JsonProperty("Apellido")
    private String apellido;

    @JsonProperty("Nombre")
    private String nombre;

    @JsonProperty("Numero_DNI")
    private String numero_DNI;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero_DNI() {
        return numero_DNI;
    }

    public void setNumero_DNI(String numero_DNI) {
        this.numero_DNI = numero_DNI;
    }
}
