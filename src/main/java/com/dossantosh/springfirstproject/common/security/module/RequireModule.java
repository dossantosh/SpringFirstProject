package com.dossantosh.springfirstproject.common.security.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireModule {
    /**
     * Lista de permisos requeridos para acceder al recurso
     * 
     * @return Array de Longs con los ids de los permisos
     */

    long[] value();
}