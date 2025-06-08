package com.backend.servicio.usuarios.services;

import com.backend.servicio.usuarios.dto.UsuarioResponse;
import com.backend.servicio.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    List<Usuario> findAll();
    Usuario findById(UUID id);
    UsuarioResponse save(Usuario usuario);
    Usuario update(UUID id, Usuario usuario);
    Usuario parcialUpdate(UUID id, Usuario usuario) throws Exception;
    void delete(UUID id) throws Exception;
}

