package com.talleres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String incio (){
        return "index";
    }

    @GetMapping("/formulario")
    public String registroFormulario (){
        return "/pages/formulario";
    }
}
