package com.dossantosh.springfirstproject.objects.preferences.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.objects.preferences.models.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    Preferences findByUserId(Long userId);
}