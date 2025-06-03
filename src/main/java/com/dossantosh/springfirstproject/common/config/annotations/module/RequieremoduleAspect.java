package com.dossantosh.springfirstproject.common.config.annotations.module;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class RequiereModuleAspect {

    private final UserAuthRepository userAuthRepository;

    @Around("@annotation(com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule) "
          + "|| @within(com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {
        //agarra información del método que se quiere ejecutar
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        //extrae, en este caso, los modulos necesarios para ejecutar el método
        RequiereModule ann = ms.getMethod().getAnnotation(RequiereModule.class);
        //si está vacío, busca la anotación a nivel de clase
        if (ann == null) {
            ann = pjp.getTarget().getClass().getAnnotation(RequiereModule.class);
        }
        long[] required = ann.value();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAuth user = userAuthRepository.findByUsername(username)
            .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));

        if(Boolean.FALSE.equals(user.getEnabled())){
            throw new AccessDeniedException("No tiene permiso para este módulo");
        }

        // Ahora buscamos por ID dentro de cada Modulo
        for (long modId : required) {
            boolean has = user.getModules()
                              .stream()
                              .anyMatch(m -> Long.valueOf(modId).equals(m));
            if (has) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("No tiene permiso para este módulo");
    }
}
