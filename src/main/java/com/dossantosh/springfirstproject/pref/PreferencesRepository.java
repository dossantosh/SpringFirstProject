package com.dossantosh.springfirstproject.pref;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.pref.models.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    Preferences findByUserId(Long userId);
}