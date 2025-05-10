package com.example.proyectonico.config.Anotaciones.Modulo;

import com.example.proyectonico.models.User;
import com.example.proyectonico.repositories.UserRepository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequiereModuloAspect {

    @Autowired
    private UserRepository userRepository;

    @Around("@annotation(com.example.proyectonico.config.Anotaciones.Modulo.RequiereModulo) "
          + "|| @within(com.example.proyectonico.config.Anotaciones.Modulo.RequiereModulo)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        RequiereModulo ann = ms.getMethod().getAnnotation(RequiereModulo.class);
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereModulo.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        // Ahora buscamos por ID dentro de cada Module
        for (long modId : required) {
            boolean has = user.getModules()
                              .stream()
                              .anyMatch(m -> m.getIdModule() == modId);
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
