package com.example.springfirstproject.objects.perfumes.tipo.service;

import java.util.LinkedHashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.objects.perfumes.tipo.model.Tipos;
import com.example.springfirstproject.objects.perfumes.tipo.repository.TipoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TipoService {

    private final TipoRepository tipoRepository;

    public Optional<Tipos> findByName(String nombre) {
        return tipoRepository.findByName(nombre);
    }

    public long count() {
        return tipoRepository.count();
    }

    public Tipos save(Tipos tipos) {
        return tipoRepository.save(tipos);
    }

    public LinkedHashSet<Tipos> findAll(){
        return new LinkedHashSet<>(tipoRepository.findAll());
    }

    public Optional<Tipos> findById(Long id){
        return tipoRepository.findById(id);
    }
}
