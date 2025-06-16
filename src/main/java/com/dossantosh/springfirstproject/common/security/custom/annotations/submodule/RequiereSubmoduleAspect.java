package com.dossantosh.springfirstproject.common.security.custom.annotations.submodule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class RequiereSubmoduleAspect {

    private final UserContextService userContextService;

    @Around("@annotation(com.dossantosh.springfirstproject.common.security.custom.annotations.submodule.RequiereSubmodule) "
            + "|| @within(com.dossantosh.springfirstproject.common.security.custom.annotations.submodule.RequiereSubmodule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        RequiereSubmodule ann = ms.getMethod().getAnnotation(RequiereSubmodule.class);
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereSubmodule.class);
        }
        long[] required = ann.value();

        // Obtener HttpSession desde el contexto HTTP actual
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            throw new IllegalStateException("No hay contexto HTTP disponible");
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpSession session = servletRequestAttributes.getRequest().getSession(false);

        if (session == null) {
            throw new AccessDeniedException("No hay sesión activa");
        }

        if (Boolean.FALSE.equals(userContextService.getEnabled())) {
            throw new AccessDeniedException("No tiene permiso para este módulo");
        }

        // Ahora buscamos por ID dentro de cada Module
        for (long modId : required) {
            boolean has = userContextService.getModules()
                    .stream()
                    .anyMatch(m -> Long.valueOf(modId).equals(m));
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
