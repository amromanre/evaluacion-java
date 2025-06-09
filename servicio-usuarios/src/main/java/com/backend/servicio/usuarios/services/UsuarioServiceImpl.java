package com.backend.servicio.usuarios.services;

import com.backend.servicio.usuarios.dto.UsuarioResponse;
import com.backend.servicio.usuarios.jwt.JwtAuthenticationFilter;
import com.backend.servicio.usuarios.models.entity.Usuario;
import com.backend.servicio.usuarios.models.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${usuario.contrasena.patron}")
    private String passwordRegex;

    // para validar formato de correo electrónico
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public List<Usuario> findAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(String id) throws Exception {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (!optionalUsuario.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado con el id: " + id);
        }
        return optionalUsuario.get();
    }

    @Override
    @Transactional
    public UsuarioResponse save(Usuario usuario)throws Exception {
        // Validar si el correo ya existe
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Validar contraseña
        if (validarContrasena(usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres y contener letras y números.");
        }

        // Validar formato de correo
        if (validarCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }

        // Generar y asignar un token JWT
        String token = JwtAuthenticationFilter.generateToken(usuario.getCorreo());
        usuario.setToken(token);
        usuario.setUltimoLogin(LocalDateTime.now());

        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        UsuarioResponse usuarioResponse =new UsuarioResponse(
                nuevoUsuario.getId(),
                nuevoUsuario.getCreado(),
                nuevoUsuario.getModificado(),
                nuevoUsuario.getUltimoLogin(),
                nuevoUsuario.getToken(),
                nuevoUsuario.getActivo());
        return usuarioResponse;

    }

    @Override
    @Transactional
    public Usuario update(String id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (!usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado con el id: " + id);
        }

        Usuario existente = usuarioExistente.get();

        // Validar si el correo ya está en uso por otro usuario
        if (!existente.getCorreo().equals(usuario.getCorreo()) &&
                usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
        }

        // Validar contraseña si se está actualizando
        if (usuario.getContrasena() != null &&
                validarContrasena(usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres y contener letras y números.");
        }

        // Validar formato de correo electrónico
        if (validarCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }

        existente.setNombre(usuario.getNombre());
        existente.setCorreo(usuario.getCorreo());

        if (usuario.getContrasena() != null) {
            existente.setContrasena(usuario.getContrasena());
        }

        // Si hay teléfonos, actualizar la relación
        if (usuario.getTelefonos() != null && !usuario.getTelefonos().isEmpty()) {
            existente.getTelefonos().clear();
            usuario.getTelefonos().forEach(existente::addTelefono);
        }
        existente.setModificado(LocalDateTime.now());
        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    public Usuario parcialUpdate(String id, Usuario usuarioParcial) throws Exception {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (!usuarioExiste.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado con el id: " + id);
        }

        Usuario usuario = usuarioExiste.get();

        // Actualizar solo los campos que se proporcionan
        if (usuarioParcial.getNombre() != null) {
            usuario.setNombre(usuarioParcial.getNombre());
        }
        if (usuarioParcial.getCorreo() != null) {
            // Validar si el correo ya existe en otro usuario
            if (!usuario.getCorreo().equals(usuarioParcial.getCorreo()) && usuarioRepository.existsByCorreo(usuarioParcial.getCorreo())) {
                throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
            }
            // Validar formato de correo electrónico
            if (validarCorreo(usuarioParcial.getCorreo())) {
                throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
            }
            usuario.setCorreo(usuarioParcial.getCorreo());
        }
        if (usuarioParcial.getContrasena() != null) {
            if(validarContrasena(usuarioParcial.getContrasena())){
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres y contener letras y números.");
            }
            usuario.setContrasena(usuarioParcial.getContrasena());
        }

        // Si hay teléfonos, actualizar la relación
        if (usuarioParcial.getTelefonos() != null && !usuarioParcial.getTelefonos().isEmpty()) {
            usuarioParcial.getTelefonos().forEach(usuario::addTelefono);
        }
        if (usuarioParcial.getActivo() != null) {
            usuario.setActivo(usuarioParcial.getActivo());
        }
        usuario.setModificado(LocalDateTime.now());

        return usuarioRepository.save(usuario);

    }

    @Override
    @Transactional
    public void delete(String id) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (!optionalUsuario.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado con el id: " + id);
        }
        usuarioRepository.deleteById(optionalUsuario.get().getId());
    }

    //metodo para validar la contraseña
    public boolean validarContrasena(String contrasena) {
        return !Pattern.matches(passwordRegex, contrasena);
    }

    //metodo para validar el correo
    public boolean validarCorreo(String correo) {
        return !EMAIL_PATTERN.matcher(correo).matches();
    }

}
