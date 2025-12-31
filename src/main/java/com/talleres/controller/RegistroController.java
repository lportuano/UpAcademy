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
@RequestMapping("/academy")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String formularioTabla(@RequestParam(name = "buscarEstudiante", required = false, defaultValue = "") String buscarEstudiante, Model model){
        List<Usuario> usuarios = usuarioService.buscarEstudiantePorNombre(buscarEstudiante);
        model.addAttribute("buscarEstudiante", buscarEstudiante);
        model.addAttribute("usuarios", usuarios);
        return "pages/tablaRegistro";
    }

    @GetMapping("/formulario")
    public String formularioStudent(Model model){
        model.addAttribute("usuario", new Usuario());
        return "pages/formEstudent";
    }

    @PostMapping("/enviarEstudent")
    public String guardarEstudent(@Valid @ModelAttribute Usuario usuario, Model model) {
        usuarioService.guardarEstudiante(usuario);
        model.addAttribute("usuario", usuario);
        return "pages/registroExitoso";
    }

    @GetMapping("/formDocente")
    public String formularioDocente(Model model){
        model.addAttribute("usuario", new Usuario());
        return "pages/formDocente";
    }

    @PostMapping("/enviarDocente")
    public String guardarDocente(@ModelAttribute Usuario docente, Model model) {
        docente.setNivelIngles("DOCENTE");
        usuarioService.guardarEstudiante(docente);
        model.addAttribute("usuario", docente);
        return "pages/registroExitoso";
    }

    @GetMapping("/editarEstudiante/{id}")
    public String actualizarEstudiante(@PathVariable Long id, Model model){
        Optional<Usuario> estudent = usuarioService.buscarEstudianteById(id);
        estudent.ifPresent(value -> model.addAttribute("usuario", value));
        return "pages/formEstudent";
    }

    @GetMapping("/editarDocente/{id}")
    public String mostrarFormularioEditarDocente(@PathVariable Long id, Model model) {
        usuarioService.buscarEstudianteById(id).ifPresent(docente -> {
            model.addAttribute("usuario", docente);
        });
        return "pages/formDocente";
    }

    @PostMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id){
        usuarioService.eliminarEstudiante(id);
        return "redirect:/academy";
    }
}