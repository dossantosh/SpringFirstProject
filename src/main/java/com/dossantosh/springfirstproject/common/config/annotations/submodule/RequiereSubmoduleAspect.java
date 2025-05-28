package com.dossantosh.springfirstproject.common.config.annotations.submodule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class RequiereSubmoduleAspect {

    private final UserRepository userRepository;

    @Around("@annotation(com.dossantosh.springfirstproject.common.config.annotations.submodule.RequiereSubmodule) "
          + "|| @within(com.dossantosh.springfirstproject.common.config.annotations.submodule.RequiereSubmodule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        RequiereSubmodule ann = ms.getMethod().getAnnotation(RequiereSubmodule.class);
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereSubmodule.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        // Ahora buscamos por ID dentro de cada Module
        for (long subId : required) {
            boolean has = user.getSubmodules()
                              .stream()
                              .anyMatch(m -> Long.valueOf(subId).equals(m.getId()));
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
