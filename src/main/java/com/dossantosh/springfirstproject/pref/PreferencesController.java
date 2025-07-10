package com.dossantosh.springfirstproject.pref;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuth;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Locale;
import java.util.List;

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

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Long> readAll = List.of(1L);
        List<Long> writeAll = List.of(2L);

        List<Long> readUsers = List.of(3L);
        List<Long> writeUsers = List.of(4L);

        List<Long> readPerfumes = List.of(5L);
        List<Long> writePerfumes = List.of(6L);

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
