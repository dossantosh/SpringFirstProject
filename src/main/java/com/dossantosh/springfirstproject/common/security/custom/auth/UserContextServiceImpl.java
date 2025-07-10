package com.dossantosh.springfirstproject.common.security.custom.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContextServiceImpl implements UserContextService {

    @Override
    public Long getId() {
        return getUserAuth().getId();
    }

    @Override
    public String getUsername() {
        return getUserAuth().getUsername();
    }

    @Override
    public boolean getEnabled() {
        return getUserAuth().getEnabled();
    }

    @Override
    public List<String> getRoles() {
        return getUserAuth().getRoles();
    }

    @Override
    public List<Long> getModules() {
        return getUserAuth().getModules();
    }

    @Override
    public List<Long> getSubmodules() {
        return getUserAuth().getSubmodules();
    }

    private UserAuth getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserAuth userAuth) {
            return userAuth;
        }
        throw new IllegalStateException("Usuario no autenticado o no válido.");
    }
}
