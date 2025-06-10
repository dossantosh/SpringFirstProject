package com.dossantosh.springfirstproject.user.controller;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.custom.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.security.custom.login.SessionService;

import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.objects.Preferences;
import com.dossantosh.springfirstproject.user.service.objects.PreferencesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/objects/preferences")
@RequiereModule({ 1L })
@Controller
public class PreferencesController extends GenericController {

    private final PreferencesService preferencesService;

    private final LocaleResolver localeResolver;

    private final SessionService sessionService;

    public PreferencesController(PermisosUtils permisosUtils, PreferencesService preferencesService,
            LocaleResolver localeResolver, SessionService sessionService) {
        super(permisosUtils);
        this.preferencesService = preferencesService;
        this.localeResolver = localeResolver;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String mostrarPreferencias(Model model, HttpSession session) {

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<Long> readAll = new HashSet<>();
        Set<Long> writeAll = new HashSet<>();

        Set<Long> readUsers = new HashSet<>();
        Set<Long> writeUsers = new HashSet<>();

        Set<Long> readPerfumes = new HashSet<>();
        Set<Long> writePerfumes = new HashSet<>();

        readAll.add(1L);
        writeAll.add(2L);
        readUsers.add(3L);
        writeUsers.add(4L);
        readPerfumes.add(5L);
        writePerfumes.add(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        Preferences preferences = preferencesService.findByUserId(userAuth.getId());
        model.addAttribute("preferences", preferences != null ? preferences : new Preferences());
        return "objects/preferences";
    }

    @PostMapping
    public String guardarPreferencias(@ModelAttribute Preferences preferences,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        preferences.setUserId(userAuth.getId());

        try {
            preferencesService.guardarPreferencias(preferences);
            // Aplica inmediatamente el locale elegido
            localeResolver.setLocale(request, response, Locale.forLanguageTag(preferences.getIdioma()));
            redirectAttributes.addFlashAttribute("successMessage", "Preferencias guardadas correctamente.");

            userAuth.setPreferences(preferences);
            sessionService.refreshAuthentication(userAuth);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al guardar las preferences.");
        }

        return "redirect:/common/configuration";
    }
}
