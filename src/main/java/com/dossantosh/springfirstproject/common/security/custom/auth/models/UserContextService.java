package com.dossantosh.springfirstproject.common.security.custom.auth.models;

import java.util.LinkedHashSet;

import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuth;
import com.dossantosh.springfirstproject.pref.models.Preferences;

public interface UserContextService {
    Long getId();

    String getUsername();

    boolean getEnabled();

    Preferences getPreferences();

    LinkedHashSet<String> getRoles();

    LinkedHashSet<Long> getModules();

    LinkedHashSet<Long> getSubmodules();

    boolean isAdmin();

    UserAuth getCurrentUserAuth();
}