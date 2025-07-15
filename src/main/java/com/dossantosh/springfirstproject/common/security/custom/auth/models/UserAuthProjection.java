package com.dossantosh.springfirstproject.common.security.custom.auth.models;

import java.util.List;

public interface UserAuthProjection {
    Long getId();

    String getUsername();

    String getPassword();

    Boolean getEnabled();
    

    Long getPreferencesUserId();

    Boolean getPreferencesEmailNotifications();

    Boolean getPreferencesSmsNotifications();

    String getPreferencesTema();

    String getPreferencesIdioma();


    List<String> getRoles();

    List<Long> getModules();

    List<Long> getSubmodules();
}