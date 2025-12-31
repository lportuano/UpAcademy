package com.talleres.controller;

import com.talleres.entity.Usuario;
import com.talleres.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioAccesoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/gestionar/{id}")
    public String formularioAcceso(@PathVariable Long id, Model model) {
        usuarioService.buscarEstudianteById(id).ifPresent(u -> {
            model.addAttribute("usuario", u);
        });
        return "pages/gestionAcceso";
    }

    // 2. Procesar la actualización de Password y Rol
    @PostMapping("/actualizar-acceso")
    public String actualizarAcceso(@RequestParam Long id,
                                   @RequestParam String password,
                                   @RequestParam String role) {

        Usuario usuario = usuarioService.buscarEstudianteById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRole(role);

        usuarioService.guardarEstudiante(usuario);
        return "redirect:/academy?exito=credenciales_actualizadas";
    }
}