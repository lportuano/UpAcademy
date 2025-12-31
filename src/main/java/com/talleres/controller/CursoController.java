package com.talleres.controller;

import com.talleres.entity.Horario;
import com.talleres.service.HorariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private HorariosService horarioService;

    private void cargarHorariosPorNivel(String nivel, Model model) {
        List<Horario> horariosNivel = horarioService.listarTodos().stream()
                .filter(h -> h.getNivel().equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
        model.addAttribute("horarios", horariosNivel);
        model.addAttribute("nivel", nivel);
    }

    @GetMapping("/a1")
    public String cursoA1(Model model) {
        cargarHorariosPorNivel("A1", model);
        return "pages/cursos/nivelA1";
    }

    @GetMapping("/a2")
    public String cursoA2(Model model) {
        cargarHorariosPorNivel("A2", model);
        return "pages/cursos/nivelA2";
    }

    @GetMapping("/b1")
    public String cursoB1(Model model) {
        cargarHorariosPorNivel("B1", model);
        return "pages/cursos/nivelB1";
    }

    @GetMapping("/b2")
    public String cursoB2(Model model) {
        cargarHorariosPorNivel("B2", model);
        return "pages/cursos/nivelB2";
    }

    @GetMapping("/c1")
    public String cursoC1(Model model) {
        cargarHorariosPorNivel("C1", model);
        return "pages/cursos/nivelC1";
    }

    @GetMapping("/c2")
    public String cursoC2(Model model) {
        cargarHorariosPorNivel("C2", model);
        return "pages/cursos/nivelC2";
    }
}