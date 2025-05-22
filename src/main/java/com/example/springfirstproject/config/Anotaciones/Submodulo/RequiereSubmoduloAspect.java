package com.example.springfirstproject.config.Anotaciones.Submodulo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.repositories.User.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class RequiereSubmoduloAspect {

    private final UserRepository userRepository;

    @Around("@annotation(com.example.proyectonico.config.Anotaciones.Submodulo.RequiereSubmodulo) "
          + "|| @within(com.example.proyectonico.config.Anotaciones.Submodulo.RequiereSubmodulo)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        RequiereSubmodulo ann = ms.getMethod().getAnnotation(RequiereSubmodulo.class);
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereSubmodulo.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        // Ahora buscamos por ID dentro de cada Module
        for (long subId : required) {
            boolean has = user.getSubmodules()
                              .stream()
                              .anyMatch(m -> m.getId() == subId);
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
