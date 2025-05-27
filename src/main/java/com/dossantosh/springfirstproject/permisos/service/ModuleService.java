package com.dossantosh.springfirstproject.permisos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.permisos.model.Modules;
import com.dossantosh.springfirstproject.permisos.repository.ModuleRepository;

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

    public Set<Modules> findAllById(List<Long> lista) {
        return new HashSet<>(moduleRepository.findAllById(lista));
    }

    public Set<Modules> findAll() {
        return new HashSet<>(moduleRepository.findAll());
    }

    public boolean existById(Long id){
        return moduleRepository.existsById(id);
    }
}
