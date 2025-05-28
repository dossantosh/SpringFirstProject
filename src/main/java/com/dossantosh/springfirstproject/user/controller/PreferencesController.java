package com.dossantosh.springfirstproject.user.controller;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.objects.Preferences;
import com.dossantosh.springfirstproject.user.service.UserAuthService;
import com.dossantosh.springfirstproject.user.service.objects.PreferencesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@RequestMapping("/objects/preferences")
@RequiredArgsConstructor
@RequiereModule({ 2L })
@Controller
public class PreferencesController {

    private final UserAuthService userAuthService;

    private final PreferencesService preferencesService;
    
    private final LocaleResolver localeResolver;

    @GetMapping
    public String mostrarPreferencias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        model.addAttribute("userAuth", userAuth);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        Preferences preferences = preferencesService.obtenerPreferencias(userAuth.getId());
        model.addAttribute("preferences", preferences != null ? preferences : new Preferences());
        return "objects/preferences";
    }

    @PostMapping
    public String guardarPreferencias(@ModelAttribute Preferences preferences,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        // Obtén usuario y asigna
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        preferences.setUserId(userAuth.getId());

        try {
            preferencesService.guardarPreferencias(preferences);
            // Aplica inmediatamente el locale elegido
            localeResolver.setLocale(request, response, Locale.forLanguageTag(preferences.getIdioma()));
            redirectAttributes.addFlashAttribute("successMessage", "Preferencias guardadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al guardar las preferences.");
        }

        return "redirect:/common/configuration";
    }
}
