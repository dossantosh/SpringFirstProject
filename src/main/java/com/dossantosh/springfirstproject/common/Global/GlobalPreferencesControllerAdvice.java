package com.dossantosh.springfirstproject.common.global;

import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.objects.Preferences;

import com.dossantosh.springfirstproject.user.service.objects.PreferencesService;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalPreferencesControllerAdvice {

    private final PreferencesService preferencesService;

    @ModelAttribute("preferences")
    public Preferences getUserPreferences(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario no está autenticado, devuelve preferences por defecto
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            Preferences defaultPreferences = new Preferences();
            defaultPreferences.setTema("auto");
            defaultPreferences.setIdioma("es");
            defaultPreferences.setEmailNotifications(false);
            defaultPreferences.setSmsNotifications(false);
            return defaultPreferences;
        }

        // Cargar preferences del usuario autenticado
        UserAuth userAuth = (UserAuth) auth.getPrincipal();

        Preferences preferences = userAuth.getPreferences();

        // Si no existen preferences, crearlas
        if (preferences == null) {
            preferences = new Preferences();
            preferences.setUserId(userAuth.getId());
            preferences.setTema("auto");
            preferences.setIdioma("es");
            preferences.setEmailNotifications(false);
            preferences.setSmsNotifications(false);
            preferencesService.guardarPreferencias(preferences);
        }

        return preferences;
    }
}
