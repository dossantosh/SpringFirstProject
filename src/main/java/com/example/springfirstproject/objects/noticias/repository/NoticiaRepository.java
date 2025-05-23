package com.example.springfirstproject.objects.noticias.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.noticias.model.Noticias;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticias, Long> {
    
}

