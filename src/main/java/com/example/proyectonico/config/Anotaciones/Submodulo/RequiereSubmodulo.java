package com.example.proyectonico.config.Anotaciones.Submodulo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para marcar métodos o clases que requieren ciertos permisos
 * Puede aplicarse a nivel de método o clase
 * Ejemplo: @RequierePermiso("CREAR_USUARIO")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiereSubmodulo {
    /**
     * Lista de permisos requeridos para acceder al recurso
     * @return Array de strings con los nombres de los permisos
     */
    String[] value();
}
