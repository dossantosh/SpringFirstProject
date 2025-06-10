package com.dossantosh.springfirstproject.common.security.custom.annotations.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiereModule {
    /**
     * Lista de permisos requeridos para acceder al recurso
     * 
     * @return Array de Longs con los nombres de los permisos
     */

    long[] value();
}