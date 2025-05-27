package com.dossantosh.springfirstproject.common.config.annotation.submodule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para marcar métodos o clases que requieren ciertos permisos
 * Puede aplicarse a nivel de método o clase
 * Ejemplo: @RequierePermiso("CREAR_USUARIO")
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiereSubmodule {
    /**
     * Lista de permisos requeridos para acceder al recurso
     * 
     * @return Array de strings con los nombres de los permisos
     */
    long[] value();
}
