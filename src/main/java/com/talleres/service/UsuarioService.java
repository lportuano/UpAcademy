package com.talleres.service;

import com.talleres.entity.Usuario;
import com.talleres.repository.UsuarioRepository;
import com.talleres.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Leer
    public List<Usuario> mostrarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Optional<Usuario> buscarUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar por username
    public Optional<Usuario> buscarUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Guardar Usuario
    public Usuario guardarUsuario(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        // Si no se especifica rol, asignar ESTUDIANTE por default
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ROLE_ESTUDIANTE);
        }

        // Verificar si el username ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está en uso");
        }

        return usuarioRepository.save(usuario);
    }

    // Actualizar Usuario
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = buscarUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setRol(usuario.getRol());

        // Actualizar el password solo si se proporciona uno nuevo
        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword().trim()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // Eliminar Usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarUsuarioById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no existe"
                ));
        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name().replace("ROLE_", ""))
                .build();
    }
}