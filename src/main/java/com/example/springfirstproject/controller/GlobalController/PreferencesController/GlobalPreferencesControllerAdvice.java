package com.example.springfirstproject.controller.GlobalController.PreferencesController;

import com.example.springfirstproject.models.Preferencias;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.service.PreferenciasService;
import com.example.springfirstproject.service.UserChikitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalPreferencesControllerAdvice {

    @Autowired
    private UserChikitoService userChikitoService;

    @Autowired
    private PreferenciasService preferenciasService;

    @ModelAttribute("preferencias")
    public Preferencias getUserPreferences() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario no está autenticado, devuelve preferencias por defecto
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            Preferencias defaultPreferences = new Preferencias();
            defaultPreferences.setTema("auto");
            defaultPreferences.setIdioma("es");
            defaultPreferences.setNotificacionesEmail(false);
            defaultPreferences.setNotificacionesSMS(false);
            return defaultPreferences;
        }

        // Cargar preferencias del usuario autenticado
        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        Preferencias preferencias = preferenciasService.obtenerPreferencias(userCh.getId());

        // Si no existen preferencias, crearlas
        if (preferencias == null) {
            preferencias = new Preferencias();
            preferencias.setUserId(userCh.getId());
            preferencias.setTema("auto");
            preferencias.setIdioma("es");
            preferencias.setNotificacionesEmail(false);
            preferencias.setNotificacionesSMS(false);
            preferenciasService.guardarPreferencias(preferencias);
        }

        return preferencias;
    }
}
