package com.talleres.controller;

import com.talleres.entity.Estudent;
import com.talleres.service.EstudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/academy")
public class RegistroController {

    @Autowired
    private EstudentService estudentService;

    //LEER
    @GetMapping
    public String formularioTabla (@RequestParam(name = "buscarEstudiante", required = false, defaultValue = "")
                                   String buscarEstudiante, Model model){
        List<Estudent> estudents = estudentService.buscarEstudiantePorNombre(buscarEstudiante);
        model.addAttribute("buscarEstudiante", buscarEstudiante);
        model.addAttribute("estudents", estudents);
        return "pages/tablaRegistro";
    }

    //GUARDAR
    @GetMapping("/formulario")
    public String formularioStudent(Model model){
        Estudent estudent = new Estudent();
        model.addAttribute("estudent", new Estudent());
        return "pages/formulario";
    }

    // Procesar formulario (POST)
    @PostMapping("/enviarEstudent")
    public String guardarEstudent(@Valid @ModelAttribute("estudent") Estudent estudent) {
        estudentService.guardarEstudiante(estudent);
        return "redirect:/academy";
    }

    //ACTUALIZAR
    @GetMapping("/editarEstudiante/{id}")
    public String actualizarEstudiante(@PathVariable Long id, Model model){
        Optional<Estudent> estudent = estudentService.buscarEstudianteById(id);
        model.addAttribute("estudent", estudent);
        return "pages/formulario";
    }

    //ELIMINAR
    @DeleteMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id){
        estudentService.eliminarEstudiante(id);
        return "redirect:/academy";
    }
}
