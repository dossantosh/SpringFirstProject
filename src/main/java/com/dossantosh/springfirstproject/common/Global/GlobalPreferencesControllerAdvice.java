package com.dossantosh.springfirstproject.common.global;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.pref.PreferencesService;
import com.dossantosh.springfirstproject.pref.models.Preferences;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalPreferencesControllerAdvice {

    private final PreferencesService preferencesService;

    private final UserContextService userContextService;

    @ModelAttribute("preferences")
    public Preferences getUserPreferences(HttpSession session) {

        // Si el usuario no está autenticado, devuelve preferences por defecto
        if (userContextService.getId() == null) {

            Preferences defaultPreferences = new Preferences();
            defaultPreferences.setTema("auto");
            defaultPreferences.setIdioma("es");
            defaultPreferences.setEmailNotifications(false);
            defaultPreferences.setSmsNotifications(false);
            return defaultPreferences;
        }

        Preferences preferences = userContextService.getPreferences();

        // Si no existen preferences, crearlas
        if (preferences == null) {
            preferences = new Preferences();
            preferences.setUserId(userContextService.getId());
            preferences.setTema("auto");
            preferences.setIdioma("es");
            preferences.setEmailNotifications(false);
            preferences.setSmsNotifications(false);
            preferencesService.guardarPreferencias(preferences);
        }

        return preferences;
    }
}
