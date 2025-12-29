package com.talleres.controller;

import com.talleres.entity.Estudent;
import com.talleres.service.EstudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/academy")
public class RegistroController {

    @Autowired
    private EstudentService estudentService;

    @GetMapping
    public String formularioTabla (@RequestParam(name = "buscarEstudiante", required = false, defaultValue = "") String buscarEstudiante, Model model){
        List<Estudent> estudents = estudentService.buscarEstudiantePorNombre(buscarEstudiante);
        model.addAttribute("buscarEstudiante", buscarEstudiante);
        model.addAttribute("estudents", estudents);
        return "pages/tablaRegistro";
    }

    @GetMapping("/formulario")
    public String formularioStudent(Model model){
        model.addAttribute("estudent", new Estudent());
        return "pages/formulario";
    }

    @PostMapping("/enviarEstudent")
    public String guardarEstudent(@Valid @ModelAttribute Estudent estudent, Model model) {
        estudentService.guardarEstudiante(estudent);
        model.addAttribute("estudiante", estudent);
        return "pages/registroExitoso";
    }

    @GetMapping("/editarEstudiante/{id}")
    public String actualizarEstudiante(@PathVariable Long id, Model model){
        Optional<Estudent> estudent = estudentService.buscarEstudianteById(id);
        model.addAttribute("estudent", estudent);
        return "pages/formulario";
    }

    @DeleteMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id){
        estudentService.eliminarEstudiante(id);
        return "redirect:/academy";
    }

    @GetMapping("/notas")
    public String verNotas(Model model) {
        return "pages/notas";
    }
}