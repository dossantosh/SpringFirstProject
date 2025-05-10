package com.example.proyectonico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.proyectonico.service.UserService;
import com.example.proyectonico.config.Anotaciones.Modulo.RequiereModulo;

import lombok.Data;

//@PreAuthorize("hasRole('ADMIN')")  
@Data
@Controller
@RequiereModulo({1L})
public class UsuariosController {
    @Autowired
    private final UserService userService;

    @GetMapping("/usuarios")
    public String showUsuarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("usuarios",userService.findAll());

        auth.getName();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        return "usuarios";
    }
}
