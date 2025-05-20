package com.example.springfirstproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.User.Preferencias;
import com.example.springfirstproject.repositories.User.PreferenciasRepository;

@Service
public class PreferenciasService {

    @Autowired
    private PreferenciasRepository preferenciasRepository;

    public Preferencias obtenerPreferencias(Long userId) {
        // Intentamos obtener las preferencias del usuario de la base de datos
        Preferencias preferencias = preferenciasRepository.findByUserId(userId);
        
        // Si no existen preferencias, se crea una por defecto
        if (preferencias == null) {
            preferencias = new Preferencias();
            preferencias.setUserId(userId);
            preferencias.setTema("auto");  // Valor por defecto
            preferencias.setIdioma("es");  // Valor por defecto
            preferencias.setNotificacionesEmail(true);  // Valor por defecto
            preferencias.setNotificacionesSMS(false);  // Valor por defecto
            
            // Guardamos las preferencias por primera vez
            preferenciasRepository.save(preferencias);
        }

        return preferencias;
    }

    public void guardarPreferencias(Preferencias nuevasPreferencias) {
        preferenciasRepository.save(nuevasPreferencias);
    }
}