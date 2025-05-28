package com.dossantosh.springfirstproject.news;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NewsService {
    
    private final NewsRepository newsRepository;

    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Noticia con ID " + id + " no encontrada"));
    }

    public Set<News> findAllById(List<Long> lista) {
        return new HashSet<>(newsRepository.findAllById(lista));
    }

    public Set<News> findAll() {
        return new HashSet<>(newsRepository.findAll());
    }
}
