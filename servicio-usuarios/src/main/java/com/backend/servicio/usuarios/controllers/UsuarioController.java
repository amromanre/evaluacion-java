package com.backend.servicio.usuarios.controllers;

import com.backend.servicio.usuarios.dto.MensajeErrorResponse;
import com.backend.servicio.usuarios.dto.UsuarioResponse;
import com.backend.servicio.usuarios.models.entity.Usuario;
import com.backend.servicio.usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/usuarios", produces = "application/json")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(consumes = "*/*")
    public ResponseEntity<?> findAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeErrorResponse("No se encontraron registros"));
            }
            return ResponseEntity.ok(usuarios);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdUsuario(@PathVariable String id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeErrorResponse("Usuario no encontrado con el id: " + id));
            }
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            UsuarioResponse response = usuarioService.save(usuario);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensajeErrorResponse("Error al crear el usuario"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable String id, @Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.update(id, usuario);
            if (usuarioActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeErrorResponse("Usuario no encontrado con el id: " + id));
            }
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> parcialUpdateUsuario(@PathVariable String id,@Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.parcialUpdate(id, usuario);
            if (usuarioActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensajeErrorResponse("Usuario no encontrado con el id: " + id));
            }
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{id}", consumes = "*/*")
    public ResponseEntity<?> deleteUsuario(@PathVariable String id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensajeErrorResponse(e.getMessage()));
        }
    }
}


