package com.dossantosh.springfirstproject.common.config.annotations.module;

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
public class RequieremoduleAspect {

    private final UserRepository userRepository;

    @Around("@annotation(com.dossantosh.springfirstproject.common.config.annotations.module.Requieremodule) "
          + "|| @within(com.dossantosh.springfirstproject.common.config.annotations.module.Requieremodule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        //agarra información del método que se quiere ejecutar
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        //extrae, en este caso, los modules necesarios para ejecutarse
        RequiereModule ann = ms.getMethod().getAnnotation(RequiereModule.class);
        //si está vacío, busca la anotación a nivel de clase
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereModule.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        // Ahora buscamos por ID dentro de cada Module
        for (long modId : required) {
            boolean has = user.getModules()
                              .stream()
                              .anyMatch(m -> Long.valueOf(modId).equals(m.getId()));
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
