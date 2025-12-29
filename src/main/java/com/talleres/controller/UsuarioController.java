package com.talleres.controller;

import com.talleres.entity.Usuario;
import com.talleres.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //LEER
    @GetMapping
    public String listaUsuarios(Model model){
        List<Usuario> usuarios = usuarioService.mostrarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "pages/usuarioLista";
    }

    //GUARDAR
    //primer paso ---> llamar al formulario
    @GetMapping("/formUsuario")
    public String formularioUsuario(Model model){
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "pages/usuarioForm";
    }

    //segundo paso ---> enviar datos
    @PostMapping("/registrarUsuario")
    public String guardarUsuario(@Valid @ModelAttribute Usuario usuario){
        if (usuario.getId() != null){
            usuarioService.actualizarUsuario(usuario.getId(), usuario);
        } else {
            usuarioService.guardarUsuario(usuario);
        }
        return "redirect:/usuarios";
    }

    // Actualizar (CORREGIDO)
    @GetMapping("/editarUsuario/{id}")
    public String actualizarUsuario(@PathVariable Long id, Model model){
        Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioById(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
        } else {
            return "redirect:/usuarios";
        }
        return "pages/usuarioForm";
    }

    @PostMapping("/actualizarUsuario/{id}")
    public String actualizarUsuarioDirecto(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuarioService.actualizarUsuario(id, usuario);
        return "redirect:/usuarios";
    }

    //Eliminar
    @DeleteMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}