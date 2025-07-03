package com.dossantosh.springfirstproject.common.security.others;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("permisosUtils")
public class PermisosUtils {

    public boolean contieneSubmodulo(Collection<?> submodulosUsuario, Object submoduloARevisar) {
        return submodulosUsuario != null && submodulosUsuario.contains(submoduloARevisar);
    }

    public boolean contieneAlgunSubmodulo(Collection<?> submodulosUsuario, Collection<?> submodulosRequeridos) {
        if (submodulosUsuario == null || submodulosRequeridos == null)
            return false;
        for (Object sub : submodulosRequeridos) {
            if (submodulosUsuario.contains(sub)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(Authentication auth) {

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
