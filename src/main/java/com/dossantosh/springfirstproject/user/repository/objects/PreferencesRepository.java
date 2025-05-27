package com.dossantosh.springfirstproject.user.repository.objects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.user.models.objects.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    Preferences findByUserId(Long userId);
}