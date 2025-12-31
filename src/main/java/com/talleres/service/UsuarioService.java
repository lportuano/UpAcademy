package com.talleres.service;

import com.talleres.entity.Usuario;
import com.talleres.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public List<Usuario> mostrarEstudiante() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarEstudianteById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarEstudianteByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }

    public List<Usuario> buscarEstudiantePorNombre(String buscarEstudiante) {
        if (buscarEstudiante == null || buscarEstudiante.isEmpty()) {
            return usuarioRepository.findAll();
        } else {
            return usuarioRepository.findByNombreContainingIgnoreCase(buscarEstudiante);
        }
    }

    public Usuario guardarEstudiante(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            if (!usuario.getPassword().startsWith("$2a$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("ROLE_ESTUDIANTE");
        } else if (!usuario.getRole().startsWith("ROLE_")) {
            usuario.setRole("ROLE_" + usuario.getRole());
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarEstudiante(Long id, Usuario usuario) {
        Usuario estudianteExistente = buscarEstudianteById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        estudianteExistente.setNombre(usuario.getNombre());
        estudianteExistente.setApellido(usuario.getApellido());
        estudianteExistente.setCedula(usuario.getCedula());
        estudianteExistente.setEmail(usuario.getEmail());
        estudianteExistente.setTelefono(usuario.getTelefono());
        estudianteExistente.setFechaNacimiento(usuario.getFechaNacimiento());
        estudianteExistente.setDireccion(usuario.getDireccion());
        estudianteExistente.setNivelIngles(usuario.getNivelIngles());

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            estudianteExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        if (usuario.getRole() != null) {
            estudianteExistente.setRole(usuario.getRole());
        }

        return usuarioRepository.save(estudianteExistente);
    }

    public void eliminarEstudiante(Long id) {
        Usuario usuario = buscarEstudianteById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudiante no EXISTE"
                ));
        usuarioRepository.delete(usuario);
    }

    // Método específico para el formulario de gestión de accesos
    public Usuario actualizarCredenciales(Long id, String password, String role) {
        Usuario usuario = buscarEstudianteById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRole(role);

        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con cédula: " + cedula));

        if (usuario.getPassword() == null || usuario.getRole() == null) {
            throw new UsernameNotFoundException("El usuario existe pero no tiene credenciales configuradas");
        }
        return User.builder()
                .username(usuario.getCedula())
                .password(usuario.getPassword())
                .roles(usuario.getRole().replace("ROLE_", ""))
                .build();
    }

    public List<Usuario> listarDocentes() {
        return usuarioRepository.findAll().stream()
                .filter(u -> "ROLE_DOCENTE".equals(u.getRole()))
                .toList();
    }

}