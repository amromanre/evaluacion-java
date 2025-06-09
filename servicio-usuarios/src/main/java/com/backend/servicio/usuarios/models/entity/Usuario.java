package com.backend.servicio.usuarios.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El formato del correo electronico no es válido")
    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "La contraseña es requerido")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
    @Valid
    private List<Telefono> telefonos;

    @Column(name = "creado", nullable = false)
    private LocalDateTime creado;

    @Column(name = "modificado", nullable = false)
    private LocalDateTime modificado;

    @Column(name = "ultimo_login", nullable = false)
    private LocalDateTime ultimoLogin;

    @Column(name = "token", nullable = true, length = 1000)
    @JsonIgnore
    private String token;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public Usuario() {
        this.id = UUID.randomUUID().toString();
        this.telefonos = new ArrayList<>();
    }

    public Usuario(String nombre, String correo, String contrasena, List<Telefono> telefonos) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefonos = telefonos;

        if (telefonos != null) {
            telefonos.forEach(telefono -> telefono.setUsuario(this));
        }
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;

        if (telefonos != null) {
            telefonos.forEach(telefono -> telefono.setUsuario(this));
        }
    }

    @PrePersist
    protected void onCreate() {

        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
        this.ultimoLogin = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.modificado = LocalDateTime.now();
    }

    public void addTelefono(Telefono telefono) {
        this.telefonos.add(telefono);
        telefono.setUsuario(this);
    }

}
