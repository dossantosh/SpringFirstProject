package com.example.springfirstproject.permisos.submodules.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.permisos.submodules.model.Submodules;
import com.example.springfirstproject.permisos.submodules.repository.SubmoduleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubmoduleService {

    private final SubmoduleRepository submoduleRepository;

    public Optional<Submodules> findById(Long id) {
        return submoduleRepository.findById(id);
    }

    public Set<Submodules> findAllById(Set<Long> listaId) {
        return new HashSet<>(submoduleRepository.findAllById(listaId));
    }

    public Set<Submodules> findAll() {
        return new HashSet<>(submoduleRepository.findAll());
    }

    public Submodules save(Submodules submodule) {
        return submoduleRepository.save(submodule);
    }

}
