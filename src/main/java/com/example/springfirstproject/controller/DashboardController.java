package com.example.springfirstproject.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.service.UserChikitoService;
import com.example.springfirstproject.service.UserService;

import lombok.Data;

//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@Data
@Controller
public class DashboardController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getName();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        /* 
        String currentUsername = principal.getName(); // Obtiene el usuario autenticado
        User user = userService.findByUsername(currentUsername);

        model.addAttribute("modulos", user.getModules());
        model.addAttribute("submodulos", user.getSubmodules());
        */ 
        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        return "dashboard";
    }
}
