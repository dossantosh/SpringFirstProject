package com.example.springfirstproject.service.User;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Noticias;
import com.example.springfirstproject.repositories.NoticiaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticiaService {
    
    private final NoticiaRepository noticiaRepository;

    public Optional<Noticias> findById(Long id) {
        return noticiaRepository.findById(id);
    }

    public Set<Noticias> findAllById(List<Long> lista) {
        return new HashSet<>(noticiaRepository.findAllById(lista));
    }

    public Set<Noticias> findAll() {
        return new HashSet<>(noticiaRepository.findAll());
    }
}
