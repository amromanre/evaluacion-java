package com.backend.servicio.usuarios.services;

import com.backend.servicio.usuarios.dto.UsuarioResponse;
import com.backend.servicio.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    List<Usuario> findAll();
    Usuario findById(String id) throws Exception;
    UsuarioResponse save(Usuario usuario) throws Exception;
    Usuario update(String id, Usuario usuario);
    Usuario parcialUpdate(String id, Usuario usuario) throws Exception;
    void delete(String id) throws Exception;
}

