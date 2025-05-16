package com.example.springfirstproject.controller;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.Preferencias;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.service.PreferenciasService;
import com.example.springfirstproject.service.UserChikitoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Locale;
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

@AllArgsConstructor
@RequiereModulo({ 2L })
@Controller
public class PreferenciasController {

    private final UserChikitoService userChikitoService;
    private final PreferenciasService preferenciasService;
    private final LocaleResolver localeResolver;

    @GetMapping("configuracion/preferencias")
    public String mostrarPreferencias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        model.addAttribute("chikito", userCh);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        Preferencias preferencias = preferenciasService.obtenerPreferencias(userCh.getId());
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
        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        preferencias.setUserId(userCh.getId());

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
