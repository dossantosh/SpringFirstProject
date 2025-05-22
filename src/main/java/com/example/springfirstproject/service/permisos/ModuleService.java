package com.example.springfirstproject.service.permisos;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Permisos.Modules;
import com.example.springfirstproject.repositories.Permisos.ModuleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public Optional<Modules> findById(Long id) {
        return moduleRepository.findById(id);
    }

    public Set<Modules> findAllById(List<Long> lista) {
        return new HashSet<>(moduleRepository.findAllById(lista));
    }

    public Set<Modules> findAll() {
        return new HashSet<>(moduleRepository.findAll());
    }
}
