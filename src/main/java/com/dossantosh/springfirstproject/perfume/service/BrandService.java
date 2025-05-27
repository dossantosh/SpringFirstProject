package com.dossantosh.springfirstproject.perfume.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.perfume.models.Brands;
import com.dossantosh.springfirstproject.perfume.repository.BrandRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public Brands save(Brands brand) {
        return brandRepository.save(brand);
    }

    public Brands findById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Marca con ID " + id + " no encontrada"));
    }

    public Brands findByName(String name) {
        return brandRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Marca con nombre " + name + " no encontrada"));
    }

    public Set<Brands> findAll() {
        return new LinkedHashSet<>(brandRepository.findAll());
    }

    public boolean existsById(Long id){
        return brandRepository.existsById(id);
    }

    public long count(){
        return brandRepository.count();
    }
}
