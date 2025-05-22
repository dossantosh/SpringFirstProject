package com.example.springfirstproject.service.permisos;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.permisos.Submodules;
import com.example.springfirstproject.repositories.Permisos.SubmoduleRepository;

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

    public Submodules save(Submodules submodulo) {
        return submoduleRepository.save(submodulo);
    }

}
