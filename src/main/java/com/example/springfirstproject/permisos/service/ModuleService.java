package com.example.springfirstproject.permisos.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.permisos.model.Modules;
import com.example.springfirstproject.permisos.repository.ModuleRepository;

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
