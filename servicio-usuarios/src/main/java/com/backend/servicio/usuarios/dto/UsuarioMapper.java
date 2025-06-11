package com.backend.servicio.usuarios.dto;

import com.backend.servicio.usuarios.models.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponse(
            usuario.getId(),
            usuario.getCreado(),
            usuario.getModificado(),
            usuario.getUltimoLogin(),
            usuario.getToken(),
            usuario.getActivo()
        );
    }
}
