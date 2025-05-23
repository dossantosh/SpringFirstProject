package com.example.springfirstproject.common.config.anotaciones.module;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.springfirstproject.user.models.User;
import com.example.springfirstproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class RequieremoduleAspect {

    private final UserRepository userRepository;

    @Around("@annotation(com.example.proyectonico.config.Anotaciones.module.Requieremodule) "
          + "|| @within(com.example.proyectonico.config.Anotaciones.module.Requieremodule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        //agarra información del método que se quiere ejecutar
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        //extrae, en este caso, los modules necesarios para ejecutarse
        Requieremodule ann = ms.getMethod().getAnnotation(Requieremodule.class);
        //si está vacío, busca la anotación a nivel de clase
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(Requieremodule.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        // Ahora buscamos por ID dentro de cada Module
        for (long modId : required) {
            boolean has = user.getModules()
                              .stream()
                              .anyMatch(m -> m.getId() == modId);
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
