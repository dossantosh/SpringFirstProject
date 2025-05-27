package com.dossantosh.springfirstproject.user.service.permissions;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.repository.permissions.SubmoduleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubmoduleService {

    private final SubmoduleRepository submoduleRepository;

    public Submodules findById(Long id) {
        return submoduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo con ID " + id + " no encontrado"));
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

    public boolean existById(Long id) {
        return submoduleRepository.existsById(id);
    }

}
