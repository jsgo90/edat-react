package com.edat.service.dto;

import java.time.ZonedDateTime;

public class HistorialDTO {

    private Long alumnoId;
    private Long autorizadoId;
    private byte[] autorizadoDni;
    private byte[] autorizadoRostro;

    public HistorialDTO() {
        super();
    }

    public HistorialDTO(Long alumnoId, Long autorizadoId, byte[] autorizadoDni, byte[] autorizadoRostro) {
        super();
        this.alumnoId = alumnoId;
        this.autorizadoId = autorizadoId;
        this.autorizadoDni = autorizadoDni;
        this.autorizadoRostro = autorizadoRostro;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Long getAutorizadoId() {
        return autorizadoId;
    }

    public void setAutorizadoId(Long autorizadoId) {
        this.autorizadoId = autorizadoId;
    }

    public byte[] getAutorizadoDni() {
        return autorizadoDni;
    }

    public void setAutorizadoDni(byte[] autorizadoDni) {
        this.autorizadoDni = autorizadoDni;
    }

    public byte[] getAutorizadoRostro() {
        return autorizadoRostro;
    }

    public void setAutorizadoRostro(byte[] autorizadoRostro) {
        this.autorizadoRostro = autorizadoRostro;
    }
}
