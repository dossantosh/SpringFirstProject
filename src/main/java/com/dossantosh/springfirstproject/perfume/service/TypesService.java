package com.dossantosh.springfirstproject.perfume.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.perfume.models.Types;
import com.dossantosh.springfirstproject.perfume.repository.TypesRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TypesService {

    private final TypesRepository typeRepository;

    public Types findById(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo con ID " + id + " no encontrado"));
    }

    public Types findByName(String name) {
        return typeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Tipo con nombre " + name + " no encontrado"));
    }

    public long count() {
        return typeRepository.count();
    }

    public Types save(Types tipos) {
        return typeRepository.save(tipos);
    }

    public List<Types> findAll() {
        return new ArrayList<>(typeRepository.findAll());
    }
}
