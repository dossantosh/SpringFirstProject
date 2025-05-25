package com.example.springfirstproject.objects.perfumes.brands.service;

import java.util.LinkedHashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.objects.perfumes.brands.models.Brands;
import com.example.springfirstproject.objects.perfumes.brands.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public Brands save(Brands brand) {
        return brandRepository.save(brand);
    }

    public Optional<Brands> findById(Long id) {
        return brandRepository.findById(id);
    }

    public Optional<Brands> findByName(String name) {
        return brandRepository.findByName(name);
    }

    public LinkedHashSet<Brands> findAll() {
        return new LinkedHashSet<>(brandRepository.findAll());
    }

    public long count(){
        return brandRepository.count();
    }
}
