package com.example.springfirstproject.service.objects;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.SequencedSet;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.objects.Brands;
import com.example.springfirstproject.repositories.objects.BrandRepository;

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

    public Optional<Brands> findByUsername(String name) {
        return brandRepository.findByName(name);
    }

    public SequencedSet<Brands> findAll() {
        return new LinkedHashSet<>(brandRepository.findAll());
    }

    public long count(){
        return brandRepository.count();
    }
}
