package com.talleres.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/academy")
public class CompraController {

    @GetMapping("/comprar")
    public String mostrarPaginaCompra() {
        return "pages/comprarCurso";
    }

    @PostMapping("/confirmarSeleccion")
    public String confirmarSeleccion(@RequestParam String curso,
                                     @RequestParam String precio,
                                     Authentication auth,
                                     Model model) {


        String nombreUsuario = (auth != null) ? auth.getName() : "Invitado";

        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("nombreCurso", curso);
        model.addAttribute("precioCurso", precio);

        return "pages/detalleCompra";
    }

    @PostMapping("/finalizarCompra")
    public String finalizarCompra(@RequestParam("curso") String nombreCurso,
                                  Authentication auth) {


        System.out.println("Compra procesada: " + nombreCurso + " para el usuario: " + auth.getName());

        return "redirect:/academy/notas?compraExitosa=true";
    }
}