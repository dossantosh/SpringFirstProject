package com.dossantosh.springfirstproject.news;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Noticia con ID " + id + " no encontrada"));
    }

    public List<News> findAllById(List<Long> lista) {
        return new ArrayList<>(newsRepository.findAllById(lista));
    }

    public List<News> findAll() {
        return new ArrayList<>(newsRepository.findAll());
    }
}
