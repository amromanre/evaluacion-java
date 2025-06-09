package com.backend.servicio.usuarios.models.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "telefonos")
public class Telefono implements Serializable {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @NotBlank (message = "El número de teléfono es requerido")
    @Column(name = "numero")
    private String numero;

    @NotBlank (message = "El código de ciudad es requerido")
    @Column(name = "codigo_ciudad")
    private String codigoCiudad;

    @NotBlank (message = "El código de país es requerido")
    @Column(name = "codigo_pais")
    private String codigoPais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public Telefono() {
        this.id = UUID.randomUUID().toString();
    }

    public Telefono(String numero, String codigoCiudad, String codigoPais) {
        this.id = UUID.randomUUID().toString();
        this.numero = numero;
        this.codigoCiudad = codigoCiudad;
        this.codigoPais = codigoPais;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
