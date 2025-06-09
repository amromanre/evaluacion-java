package com.backend.servicio.usuarios.controllers;

import com.backend.servicio.usuarios.dto.MensajeErrorResponse;
import com.backend.servicio.usuarios.dto.UsuarioResponse;
import com.backend.servicio.usuarios.models.entity.Telefono;
import com.backend.servicio.usuarios.models.entity.Usuario;
import com.backend.servicio.usuarios.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;
    private List<Usuario> usuarioList;
    private String userId;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();

        usuario = new Usuario();
        usuario.setId(userId);
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("test@test.com");
        usuario.setContrasena("password123");
        usuario.setCreado(LocalDateTime.now());
        usuario.setModificado(LocalDateTime.now());
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setTelefonos(new ArrayList<>());

        List<Telefono> telefonos = new ArrayList<>();
        Telefono telefono = new Telefono();
        telefono.setNumero("123456789");
        telefono.setCodigoCiudad("1");
        telefono.setCodigoPais("56");
        telefonos.add(telefono);
        usuario.setTelefonos(telefonos);

        usuarioList = new ArrayList<>();
        usuarioList.add(usuario);

        usuarioResponse = new UsuarioResponse(
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "jwt-token",
                true
        );
    }

    @Test
    @DisplayName("Test para obtener todos los usuarios con éxito")
    void findAllUsuariosSuccess() {
        // Given
        given(usuarioService.findAll()).willReturn(usuarioList);

        // When
        ResponseEntity<?> response = usuarioController.findAllUsuarios();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioList, response.getBody());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener todos los usuarios cuando la lista está vacía")
    void findAllUsuariosEmpty() {
        // Given
        given(usuarioService.findAll()).willReturn(new ArrayList<>());

        // When
        ResponseEntity<?> response = usuarioController.findAllUsuarios();

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("No se encontraron registros", errorResponse.getMensaje());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener todos los usuarios cuando ocurre una excepción")
    void findAllUsuariosException() {
        // Given
        given(usuarioService.findAll()).willThrow(new RuntimeException("Error al obtener usuarios"));

        // When
        ResponseEntity<?> response = usuarioController.findAllUsuarios();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al obtener usuarios", errorResponse.getMensaje());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para obtener un usuario por ID con éxito")
    void findByIdUsuarioSuccess() throws Exception{
        // Given
        given(usuarioService.findById(userId)).willReturn(usuario);

        // When
        ResponseEntity<?> response = usuarioController.findByIdUsuario(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(usuarioService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test para obtener un usuario por ID cuando no existe")
    void findByIdUsuarioNotFound() throws Exception {
        // Given
        given(usuarioService.findById(userId)).willReturn(null);

        // When
        ResponseEntity<?> response = usuarioController.findByIdUsuario(userId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Usuario no encontrado con el id: " + userId, errorResponse.getMensaje());
        verify(usuarioService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test para obtener un usuario por ID cuando ocurre una excepción")
    void findByIdUsuarioException() throws Exception {
        // Given
        given(usuarioService.findById(userId)).willThrow(new RuntimeException("Error al obtener usuario"));

        // When
        ResponseEntity<?> response = usuarioController.findByIdUsuario(userId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al obtener usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test para crear un usuario con éxito")
    void createUsuarioSuccess() throws Exception {
        // Given
        given(usuarioService.save(any(Usuario.class))).willReturn(usuarioResponse);

        // When
        ResponseEntity<?> response = usuarioController.createUsuario(usuario);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuarioResponse, response.getBody());
        verify(usuarioService, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Test para crear un usuario con error")
    void createUsuarioError() throws Exception {
        // Given
        given(usuarioService.save(any(Usuario.class))).willReturn(null);

        // When
        ResponseEntity<?> response = usuarioController.createUsuario(usuario);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al crear el usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Test para crear un usuario cuando ocurre una excepción")
    void createUsuarioException() throws Exception {
        // Given
        given(usuarioService.save(any(Usuario.class))).willThrow(new RuntimeException("Error al guardar usuario"));

        // When
        ResponseEntity<?> response = usuarioController.createUsuario(usuario);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al guardar usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Test para actualizar un usuario con éxito")
    void updateUsuarioSuccess() {
        // Given
        given(usuarioService.update(eq(userId), any(Usuario.class))).willReturn(usuario);

        // When
        ResponseEntity<?> response = usuarioController.updateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(usuarioService, times(1)).update(userId, usuario);
    }

    @Test
    @DisplayName("Test para actualizar un usuario cuando no existe")
    void updateUsuarioNotFound() {
        // Given
        given(usuarioService.update(eq(userId), any(Usuario.class))).willReturn(null);

        // When
        ResponseEntity<?> response = usuarioController.updateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Usuario no encontrado con el id: " + userId, errorResponse.getMensaje());
        verify(usuarioService, times(1)).update(userId, usuario);
    }

    @Test
    @DisplayName("Test para actualizar un usuario cuando ocurre una excepción")
    void updateUsuarioException() {
        // Given
        given(usuarioService.update(eq(userId), any(Usuario.class))).willThrow(new RuntimeException("Error al actualizar usuario"));

        // When
        ResponseEntity<?> response = usuarioController.updateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al actualizar usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).update(userId, usuario);
    }

    @Test
    @DisplayName("Test para actualizar parcialmente un usuario con éxito")
    void parcialUpdateUsuarioSuccess() throws Exception {
        // Given
        given(usuarioService.parcialUpdate(eq(userId), any(Usuario.class))).willReturn(usuario);

        // When
        ResponseEntity<?> response = usuarioController.parcialUpdateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(usuarioService, times(1)).parcialUpdate(userId, usuario);
    }

    @Test
    @DisplayName("Test para actualizar parcialmente un usuario cuando no existe")
    void parcialUpdateUsuarioNotFound() throws Exception {
        // Given
        given(usuarioService.parcialUpdate(eq(userId), any(Usuario.class))).willReturn(null);

        // When
        ResponseEntity<?> response = usuarioController.parcialUpdateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Usuario no encontrado con el id: " + userId, errorResponse.getMensaje());
        verify(usuarioService, times(1)).parcialUpdate(userId, usuario);
    }

    @Test
    @DisplayName("Test para actualizar parcialmente un usuario cuando ocurre una excepción")
    void parcialUpdateUsuarioException() throws Exception {
        // Given
        given(usuarioService.parcialUpdate(eq(userId), any(Usuario.class))).willThrow(new RuntimeException("Error al actualizar usuario"));

        // When
        ResponseEntity<?> response = usuarioController.parcialUpdateUsuario(userId, usuario);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al actualizar usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).parcialUpdate(userId, usuario);
    }

    @Test
    @DisplayName("Test para eliminar un usuario con éxito")
    void deleteUsuarioSuccess() throws Exception {
        // Given
        doNothing().when(usuarioService).delete(userId);

        // When
        ResponseEntity<?> response = usuarioController.deleteUsuario(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).delete(userId);
    }

    @Test
    @DisplayName("Test para eliminar un usuario cuando ocurre una excepción")
    void deleteUsuarioException() throws Exception {
        // Given
        doThrow(new RuntimeException("Error al eliminar usuario")).when(usuarioService).delete(userId);

        // When
        ResponseEntity<?> response = usuarioController.deleteUsuario(userId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(MensajeErrorResponse.class, response.getBody());
        MensajeErrorResponse errorResponse = (MensajeErrorResponse) response.getBody();
        assertEquals("Error al eliminar usuario", errorResponse.getMensaje());
        verify(usuarioService, times(1)).delete(userId);
    }
}
