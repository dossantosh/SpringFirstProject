package com.dossantosh.springfirstproject.objects.news.controller;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotation.module.Requieremodule;
import com.dossantosh.springfirstproject.objects.news.model.News;
import com.dossantosh.springfirstproject.objects.preferences.service.PreferencesService;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Requieremodule({ 2L })
public class NewsController {

    private final UserAuthService userAuthService;

    private final PreferencesService preferencesService;

    @GetMapping("/objects/news")
    public String showPrincipal(Model model, News news) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        model.addAttribute("userAuth", userAuth);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        LinkedHashSet<News> setNoticias = new LinkedHashSet<>();

        News salida;
        News fechaSalida;
        News prueba;

        if (preferencesService.obtenerPreferencias(userAuth.getId()).getIdioma().equals("es")) {
            salida = new News(1L, "The Dawn of Man: disponible en...",
                    "Ya se puede comprar the dawn of man en las plataformas oficiales...", "13/05/2025", "image");
            fechaSalida = new News(1L, "Fecha de salida: The Dawn of Man",
                    "La fecha de salida ya está confirmada para el 13/05/2025. Ya podéis reservar en...", "10/04/2025",
                    "image");
            prueba = new News(1L, "Esto es una prueba que...",
                    "Primera prueba del sistema de news y si o is tiene que tener, al menos, dos lineas",
                    "03/04/2025", "image");
        } else {
            salida = new News(1L, "The Dawn of Man: available...",
                    "The Dawn of Man is now available for purchase on official platforms..", "13/05/2025", "image");
            fechaSalida = new News(1L, "Departure date: The Dawn of Man",
                    "The departure date is now confirmed for May 13, 2025. You can now book at...", "10/04/2025",
                    "image");
            prueba = new News(1L, "This is a test that...",
                    "First test of the news system and it has to have at least two lines.", "03/04/2025", "image");
        }

        setNoticias.add(salida);
        setNoticias.add(fechaSalida);
        setNoticias.add(prueba);

        model.addAttribute("news", setNoticias);
        return "objects/news";
    }
}
