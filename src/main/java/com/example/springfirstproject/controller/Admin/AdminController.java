package com.example.springfirstproject.controller.Admin;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.service.User.UserChikitoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequiereModulo({ 1L, 2L })
public class AdminController {
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());
        Optional<UserChikito> userCh = userChikitoService.findByUsername(auth.getName());

        model.addAttribute("chikito", userCh.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        return "admin";
    }
}
