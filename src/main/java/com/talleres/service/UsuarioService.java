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

    //Leer
    public List<Usuario> mostrarUsuarios(){
        return usuarioRepository.findAll();
    }

    //buscar por ID
    public Optional<Usuario> buscarUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }

    //Guardar Usuario
    public Usuario guardarUsuario(Usuario usuario){

        //Encriptar la contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        //Asignar el rol de esudiante por default
        usuario.setRol(Rol.ROLE_ESTUDIANTE);
        usuarioRepository.save(usuario);
        return usuarioRepository.save(usuario);
    }

    //Actualizar Usuario
    public Usuario actualizarUsuario(Long id, Usuario usuario){
        Usuario usuarioExistente = buscarUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setUsername(usuario.getUsername());
        //Actualizar el password solo si el usuario la cambia
        if (usuario.getPassword() != null && usuario.getPassword().isBlank()){
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuarioExistente);
    }

    //Eliminar Usuario
    public void eliminarUsuario(Long id){
        Usuario usuario = buscarUsuarioById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no existe"
                ));
        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Buscar el usurio que coincida y si no lo encuentra lanza un exeption
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Usuario no existe: " +username));

        //Usar builder para construir un objeto al que se conoce como objeto autenticado
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(usuario.getRol().name())
                .build();
    }
}