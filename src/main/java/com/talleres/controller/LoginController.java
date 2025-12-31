package com.talleres.controller;

import com.talleres.entity.Usuario;
import com.talleres.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/postLogin")
    public String dirigirPorRol(Authentication authenticator) {
        if (authenticator == null) return "redirect:/login";

        User usuarioSpring = (User) authenticator.getPrincipal();

        String role = usuarioSpring.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        switch (role) {
            case "ROLE_ADMIN":
                return "redirect:/admin";

            case "ROLE_DOCENTE":
                return "redirect:/horarios";

            case "ROLE_ESTUDIANTE":
                Optional<Usuario> usuarioBD = usuarioService.buscarEstudianteByCedula(usuarioSpring.getUsername());

                if (usuarioBD.isPresent()) {
                    String nivel = usuarioBD.get().getNivelIngles();

                    if (nivel != null && !nivel.isEmpty()) {
                        return "redirect:/cursos/" + nivel.toLowerCase();
                    }
                }
                return "redirect:/index";

            default:
                return "redirect:/index";
        }
    }
}