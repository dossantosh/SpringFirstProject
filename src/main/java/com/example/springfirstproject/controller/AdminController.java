package com.example.springfirstproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.service.UserService;

import lombok.Data;

//@PreAuthorize("hasRole('ADMIN')")
@Data
@Controller
@RequiereModulo({1L})
public class AdminController {
    @Autowired
    private final UserService userService;

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        return "admin";
    }
}
