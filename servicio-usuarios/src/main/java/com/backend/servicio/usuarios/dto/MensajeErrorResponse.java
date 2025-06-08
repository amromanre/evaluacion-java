package com.backend.servicio.usuarios.dto;

public class MensajeErrorResponse {
    private String mensaje;

    public MensajeErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
