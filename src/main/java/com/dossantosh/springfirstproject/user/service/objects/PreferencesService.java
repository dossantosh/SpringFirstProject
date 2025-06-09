package com.dossantosh.springfirstproject.user.service.objects;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.objects.Preferences;
import com.dossantosh.springfirstproject.user.repository.objects.PreferencesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PreferencesService {

    private final PreferencesRepository preferencesRepository;

    public Preferences findByUserId(Long userId) {
        // Intentamos obtener las preferences del usuario de la base de datos
        Preferences preferences = preferencesRepository.findByUserId(userId);
        
        // Si no existen preferences, se crea una por defecto
        if (preferences == null) {
            preferences = new Preferences();
            preferences.setUserId(userId);
            preferences.setTema("auto");  // Valor por defecto
            preferences.setIdioma("es");  // Valor por defecto
            preferences.setEmailNotifications(true);  // Valor por defecto
            preferences.setSmsNotifications(false);  // Valor por defecto
            
            // Guardamos las preferences por primera vez
            preferencesRepository.save(preferences);
        }

        return preferences;
    }

    public void guardarPreferencias(Preferences nuevasPreferencias) {
        preferencesRepository.save(nuevasPreferencias);
    }
}