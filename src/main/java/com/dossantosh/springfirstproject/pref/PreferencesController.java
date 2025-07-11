package com.dossantosh.springfirstproject.pref;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuth;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;
import com.dossantosh.springfirstproject.pref.models.Preferences;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Locale;
import java.util.Set;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/objects/preferences")
@RequireModule({ 1L })
@Controller
public class PreferencesController extends GenericController {

    private final PreferencesService preferencesService;

    private final LocaleResolver localeResolver;

    private final SessionService sessionService;

    public PreferencesController(UserContextService userContextService, PermisosUtils permisosUtils,
            PreferencesService preferencesService,
            LocaleResolver localeResolver, SessionService sessionService) {
        super(userContextService, permisosUtils);
        this.preferencesService = preferencesService;
        this.localeResolver = localeResolver;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String mostrarPreferencias(Model model, HttpSession session) {

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);

        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        Preferences preferences = userContextService.getPreferences();
        model.addAttribute("preferences", preferences != null ? preferences : new Preferences());
        return "objects/preferences";
    }

    @PostMapping
    public String guardarPreferencias(@ModelAttribute Preferences preferences,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        preferences.setUserId(userContextService.getId());

        try {
            preferencesService.guardarPreferencias(preferences);
            // Aplica inmediatamente el locale elegido
            localeResolver.setLocale(request, response, Locale.forLanguageTag(preferences.getIdioma()));
            redirectAttributes.addFlashAttribute("successMessage", "Preferencias guardadas correctamente.");

            UserAuth currentUserAuth = userContextService.getCurrentUserAuth();

            UserAuth updatedUserAuth = new UserAuth(
                    currentUserAuth.getId(),
                    currentUserAuth.getUsername(),
                    currentUserAuth.getPassword(),
                    currentUserAuth.getEnabled(),
                    preferences, // nuevas preferencias aquí
                    currentUserAuth.getRoles(),
                    currentUserAuth.getModules(),
                    currentUserAuth.getSubmodules());

            sessionService.refreshAuthentication(updatedUserAuth);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al guardar las preferences.");
        }

        return "redirect:/common/configuration";
    }
}
