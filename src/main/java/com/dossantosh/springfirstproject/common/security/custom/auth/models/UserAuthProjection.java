package com.dossantosh.springfirstproject.common.security.custom.auth.models;

import java.util.List;

import com.dossantosh.springfirstproject.pref.models.PreferencesProjection;

public interface UserAuthProjection {
    Long getId();

    String getUsername();

    String getPassword();

    Boolean getEnabled();

    PreferencesProjection getPreferences();

    List<String> getRoles();

    List<Long> getModules();

    List<Long> getSubmodules();
}