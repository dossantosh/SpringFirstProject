package com.dossantosh.springfirstproject.pref;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    Preferences findByUserId(Long userId);
}