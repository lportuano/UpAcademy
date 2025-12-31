package com.talleres.controller;

import com.talleres.entity.Horario; // Asegúrate de crear esta Entidad
import com.talleres.service.HorariosService;
import com.talleres.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorariosService horarioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.listarTodos());
        model.addAttribute("docentes", usuarioService.listarDocentes());
        return "pages/horarios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Horario horario) {
        horarioService.guardar(horario);
        return "redirect:/horarios";
    }

    @GetMapping("/editar/{id}")
    public String editarHorario(@PathVariable Long id, Model model) {
        Horario horario = horarioService.buscarPorId(id);
        model.addAttribute("horario", horario);
        model.addAttribute("docentes", usuarioService.listarDocentes());
        return "pages/editar-horario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return "redirect:/horarios";
    }
}