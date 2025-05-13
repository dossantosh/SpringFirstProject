package com.example.springfirstproject.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.Noticias;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.service.UserChikitoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequiereModulo({ 2L })
public class DashboardController {
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        model.addAttribute("chikito", userCh);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        SequencedSet< Noticias > setNoticias = new LinkedHashSet<>();  

        Noticias salida = new Noticias(1L, "The Dawn of Man: disponible en...", "Ya se puede comprar the dawn of man en las plataformas oficiales...", "13/05/2025", "imagen");
        Noticias fechaSalida = new Noticias(1L, "Fecha de salida: The Dawn of Man", "La fecha de salida ya está confirmada para el 13/05/2025. Ya podéis reservar en las plataformas...", "10/04/2025", "imagen");
        Noticias prueba = new Noticias(1L, "Esto es una prueba que...", "Primera prueba del sistema de noticias", "03/04/2025", "imagen");

        setNoticias.add(salida);
        setNoticias.add(fechaSalida);
        setNoticias.add(prueba);

        model.addAttribute("noticias", setNoticias);
        return "dashboard";
    }
}
