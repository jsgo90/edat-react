package com.edat.service.dto;

public class HistorialDTO {

    private Long alumnoId;
    private Long autorizadoId;

    public HistorialDTO() {
        super();
    }

    public HistorialDTO(Long alumnoId, Long autorizadoId) {
        super();
        this.alumnoId = alumnoId;
        this.autorizadoId = autorizadoId;
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
}
