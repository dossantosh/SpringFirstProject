package com.example.springfirstproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.models.User;
import com.example.springfirstproject.service.UserService;

import lombok.Data;

//@PreAuthorize("hasRole('ADMIN')")  
@Data
@Controller
@RequiereModulo({ 1L })
public class UsuariosController {

    private final UserService userService;

    public UsuariosController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/usuarios")
    public String showUsuarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //User user = userService.findByUsername(auth.getName());

        model.addAttribute("usuarios", userService.findAll());
        model.addAttribute("username", auth.getName());
        

        return "usuarios";
    }
}
