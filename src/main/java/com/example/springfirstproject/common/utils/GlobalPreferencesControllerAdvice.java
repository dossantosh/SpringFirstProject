package com.example.springfirstproject.common.utils;

import com.example.springfirstproject.objects.preferencias.models.Preferencias;
import com.example.springfirstproject.objects.preferencias.service.PreferenciasService;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalPreferencesControllerAdvice {

    private final UserAuthService userAuthService;

    private final PreferenciasService preferenciasService;

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
        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if(!userAuth.isPresent()){
            return null;
        }
        Preferencias preferencias = preferenciasService.obtenerPreferencias(userAuth.get().getId());

        // Si no existen preferencias, crearlas
        if (preferencias == null) {
            preferencias = new Preferencias();
            preferencias.setUserId(userAuth.get().getId());
            preferencias.setTema("auto");
            preferencias.setIdioma("es");
            preferencias.setNotificacionesEmail(false);
            preferencias.setNotificacionesSMS(false);
            preferenciasService.guardarPreferencias(preferencias);
        }

        return preferencias;
    }
}
