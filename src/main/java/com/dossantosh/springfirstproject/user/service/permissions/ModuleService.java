package com.dossantosh.springfirstproject.user.service.permissions;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.repository.permissions.ModuleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public Modules findById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo con ID " + id + " no encontrado"));
    }

    public List<Modules> findAllById(List<Long> lista) {
        return new ArrayList<>(moduleRepository.findAllById(lista));
    }

    public List<Modules> findAll() {
        return new ArrayList<>(moduleRepository.findAll());
    }

    public boolean existById(Long id) {
        return moduleRepository.existsById(id);
    }
}
