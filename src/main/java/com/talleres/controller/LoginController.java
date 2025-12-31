package com.talleres.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/postLogin")
    public String dirigirPorRol(Authentication authenticator) {
        if (authenticator == null) return "redirect:/login";

        User usuario = (User) authenticator.getPrincipal();
        String role = usuario.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_DOCENTE")) {
            return "redirect:/academy";
        } else {
            return "redirect:/index";
        }
    }
}