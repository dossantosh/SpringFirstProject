package com.dossantosh.springfirstproject.common.security.custom;

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
}
