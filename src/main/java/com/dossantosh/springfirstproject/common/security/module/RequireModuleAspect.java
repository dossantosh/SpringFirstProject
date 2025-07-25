package com.dossantosh.springfirstproject.common.security.module;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RequireModuleAspect {

    private final UserContextService userContextService;

    @Around("@annotation(com.dossantosh.springfirstproject.common.security.module.RequireModule) "
            + "|| @within(com.dossantosh.springfirstproject.common.security.module.RequireModule)")
    public Object checkModules(ProceedingJoinPoint pjp) throws Throwable {

        // agarra información del método que se quiere ejecutar
        MethodSignature ms = (MethodSignature) pjp.getSignature();

        // extrae, en este caso, los modulos necesarios para ejecutar el método
        RequireModule ann = AnnotationUtils.findAnnotation(ms.getMethod(), RequireModule.class);
        //RequiereModule ann = ms.getMethod().getAnnotation(RequiereModule.class);
        
        // si está vacío, busca la anotación a nivel de clase
        if (ann == null) {
            ann = AnnotationUtils.findAnnotation(pjp.getTarget().getClass(), RequireModule.class);
            //ann = pjp.getTarget().getClass().getAnnotation(RequiereModule.class);
        }
        if (ann == null) {
            return pjp.proceed();
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

        // Ahora buscamos por ID dentro de cada Modulo

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
