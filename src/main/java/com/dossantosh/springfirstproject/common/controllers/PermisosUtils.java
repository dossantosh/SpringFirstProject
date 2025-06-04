package com.dossantosh.springfirstproject.common.controllers;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("permisosUtils")
public class PermisosUtils {

    public boolean contieneRol(Collection<?> rolesUsuario, Object rolARevisar) {
        return rolesUsuario != null && rolesUsuario.contains(rolARevisar);
    }

    public boolean contieneAlgunRol(Collection<?> rolesUsuario, Collection<?> rolesRequeridos) {
        if (rolesUsuario == null || rolesRequeridos == null)
            return false;
        for (Object rol : rolesRequeridos) {
            if (rolesUsuario.contains(rol)) {
                return true;
            }
        }
        return false;
    }

    public boolean contieneModulo(Collection<?> modulosUsuario, Object moduloARevisar) {
        return modulosUsuario != null && modulosUsuario.contains(moduloARevisar);
    }

    public boolean contieneAlgunModulo(Collection<?> modulosUsuario, Collection<?> modulosRequeridos) {
        if (modulosUsuario == null || modulosRequeridos == null)
            return false;
        for (Object mod : modulosRequeridos) {
            if (modulosUsuario.contains(mod)) {
                return true;
            }
        }
        return false;
    }

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
}
