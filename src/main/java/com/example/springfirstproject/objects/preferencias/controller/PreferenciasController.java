package com.example.springfirstproject.objects.preferencias.controller;

import com.example.springfirstproject.common.config.annotation.module.Requieremodule;
import com.example.springfirstproject.objects.preferencias.models.Preferencias;
import com.example.springfirstproject.objects.preferencias.service.PreferenciasService;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Requieremodule({ 2L })
@Controller
public class PreferenciasController {

    private final UserAuthService userAuthService;

    private final PreferenciasService preferenciasService;
    
    private final LocaleResolver localeResolver;

    @GetMapping("configuracion/preferencias")
    public String mostrarPreferencias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        model.addAttribute("userAuth", userAuth.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        Preferencias preferencias = preferenciasService.obtenerPreferencias(userAuth.get().getId());
        model.addAttribute("preferencias", preferencias != null ? preferencias : new Preferencias());
        return "configuracion/preferencias";
    }

    @PostMapping("configuracion/preferencias")
    public String guardarPreferencias(@ModelAttribute Preferencias preferencias,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        // Obtén usuario y asigna
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        preferencias.setUserId(userAuth.get().getId());

        try {
            preferenciasService.guardarPreferencias(preferencias);
            // Aplica inmediatamente el locale elegido
            localeResolver.setLocale(request, response, Locale.forLanguageTag(preferencias.getIdioma()));
            redirectAttributes.addFlashAttribute("successMessage", "Preferencias guardadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al guardar las preferencias.");
        }

        return "redirect:/configuracion";
    }
}
