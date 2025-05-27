package com.dossantosh.springfirstproject.objects.news.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.objects.news.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
}

