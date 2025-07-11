package com.dossantosh.springfirstproject.common.security.custom.auth;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.pref.models.Preferences;

import java.util.LinkedHashSet;

@Service
public class UserContextServiceImpl implements UserContextService {

    @Override
    public Long getId() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) ? userAuth.getId() : null;
    }

    @Override
    public String getUsername() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) ? userAuth.getUsername() : null;
    }

    @Override
    public boolean getEnabled() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) && Boolean.TRUE.equals(userAuth.getEnabled());
    }

    @Override
    public Preferences getPreferences() {
        UserAuth userAuth = getUserAuth();
        if (userAuth == null) {
            return null; // o new Preferences() si quieres valor por defecto
        }
        Preferences preferences = userAuth.getPreferences();
        if (preferences == null) {
            return null;
        }
        return preferences;
    }

    @Override
    public LinkedHashSet<String> getRoles() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) ? userAuth.getRoles() : new LinkedHashSet<>();
    }

    @Override
    public LinkedHashSet<Long> getModules() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) ? userAuth.getModules() : new LinkedHashSet<>();
    }

    @Override
    public LinkedHashSet<Long> getSubmodules() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) ? userAuth.getSubmodules() : new LinkedHashSet<>();
    }

    @Override
    public boolean isAdmin() {
        UserAuth userAuth = getUserAuth();
        return (userAuth != null) && userAuth.getRoles().contains("ROLE_ADMIN");
    }

    @Override
    public UserAuth getCurrentUserAuth() {
        return getUserAuth();
    }

    private UserAuth getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserAuth userAuth) {
            return userAuth;
        }
        return null;
    }
}
