package com.dossantosh.springfirstproject.common.security.custom.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.pref.models.Preferences;

import java.util.LinkedHashSet;
import java.util.Optional;

@Service
public final class UserContextServiceImpl implements UserContextService {

    @Override
    public Long getId() {
        return getUserAuth().map(UserAuth::getId).orElse(null);
    }

    @Override
    public String getUsername() {
        return getUserAuth().map(UserAuth::getUsername).orElse(null);
    }

    @Override
    public boolean getEnabled() {
        return getUserAuth().map(UserAuth::getEnabled).orElse(Boolean.FALSE);
    }

    @Override
    public Preferences getPreferences() {
        return getUserAuth()
                .map(UserAuth::getPreferences)
                .orElseGet(Preferences::new); // devuelve instancia vacía en vez de null
    }

    @Override
    public LinkedHashSet<String> getRoles() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getRoles).orElse(new LinkedHashSet<>()));
    }

    @Override
    public LinkedHashSet<Long> getModules() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getModules).orElse(new LinkedHashSet<>()));
    }

    @Override
    public LinkedHashSet<Long> getSubmodules() {
        return new LinkedHashSet<>(
                getUserAuth().map(UserAuth::getSubmodules).orElse(new LinkedHashSet<>()));
    }

    @Override
    public boolean isAdmin() {
        return getUserAuth()
                .map(UserAuth::getRoles)
                .orElse(new LinkedHashSet<>())
                .contains("ROLE_ADMIN");
    }

    @Override
    public UserAuth getCurrentUserAuth() {
        return getUserAuth().orElse(null);
    }

    private static Optional<UserAuth> getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserAuth userAuth) {
            return Optional.of(userAuth);
        }
        return Optional.empty();
    }
}
