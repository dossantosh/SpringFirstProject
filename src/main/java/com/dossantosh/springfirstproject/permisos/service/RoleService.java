package com.dossantosh.springfirstproject.permisos.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.permisos.model.Roles;
import com.dossantosh.springfirstproject.permisos.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Roles findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol con ID " + id + " no encontrado"));
    }

    public Set<Roles> findAllById(Set<Long> listaId) {
        return new HashSet<>(roleRepository.findAllById(listaId));

    }

    public Set<Roles> findAll() {
        return new HashSet<>(roleRepository.findAll());

    }

    public boolean existById(Long id) {
        return roleRepository.existsById(id);
    }

}