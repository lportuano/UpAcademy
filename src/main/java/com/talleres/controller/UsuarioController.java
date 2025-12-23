package com.talleres.controller;

import com.talleres.entity.Usuario;
import com.talleres.roles.Rol;
import com.talleres.service.UsuarioService;
import com.talleres.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LEER - Listar todos los usuarios
    @GetMapping
    public String listaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.mostrarUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", Rol.values());
        return "pages/usuarioLista";
    }

    // FORMULARIO - Nuevo usuario
    @GetMapping("/formUsuario")
    public String formularioUsuario(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "pages/usuarioForm";
    }

    // GUARDAR - Nuevo usuario
    @PostMapping("/registrarUsuario")
    public String guardarUsuario(@Valid @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
            return "redirect:/usuarios";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/formUsuario";
        }
    }

    // EDITAR - Mostrar formulario de edición
    @GetMapping("/editarUsuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("roles", Rol.values());
            return "pages/usuarioForm";
        }
        return "redirect:/usuarios";
    }

    // ACTUALIZAR - Procesar actualización
    @PostMapping("/actualizarUsuario/{id}")
    public String actualizarUsuario(@PathVariable Long id, @Valid @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizarUsuario(id, usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // ELIMINAR - Eliminar usuario
    @DeleteMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
        }
        return "redirect:/usuarios";
    }
}