package com.example.springfirstproject.controller;

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

// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@Data
@Controller
public class PerfilController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/perfil")
    public String showPerfilPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);
        return "perfil";
    }
}
