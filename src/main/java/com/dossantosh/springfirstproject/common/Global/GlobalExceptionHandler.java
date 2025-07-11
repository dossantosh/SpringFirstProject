package com.dossantosh.springfirstproject.common.global;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.dossantosh.springfirstproject.common.global.events.Audit.AuditService;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

        private final AuditService auditService;

        private final UserContextService userContextService;

        @ExceptionHandler({ DataAccessResourceFailureException.class, CannotGetJdbcConnectionException.class })
        public ModelAndView handleDatabaseConnectionFailure(HttpServletRequest request) {
                return new ModelAndView("redirect:/login?error=dbConnectionLost");
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public String handle400(MethodArgumentNotValidException ex, HttpServletRequest request, Object handler,
                        Model model) {
                model.addAttribute("error", "Solicitud inválida");
                model.addAttribute("message", ex.getMessage());

                Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.toMap(
                                                fieldError -> fieldError.getField(),
                                                fieldError -> fieldError.getDefaultMessage()));

                auditService.logCustomEvent(
                                userContextService.getUsername() != null
                                                ? userContextService.getUsername()
                                                : "anonymous",
                                "MethodArgumentNotValidException",
                                Map.of(
                                                "errors", fieldErrors,
                                                "url", request.getRequestURI() != null
                                                                ? request.getRequestURI()
                                                                : "N/A",
                                                "method", request.getMethod() != null
                                                                ? request.getMethod()
                                                                : "N/A",
                                                "handler", handler.getClass().getSimpleName() != null
                                                                ? handler.getClass().getSimpleName()
                                                                : "N/A"));

                return "error/400";
        }

        @ExceptionHandler(AccessDeniedException.class)
        public String handle403(AccessDeniedException ex, HttpServletRequest request, Object handler, Model model) {
                model.addAttribute("error", "No tienes permisos para acceder a este recurso");
                model.addAttribute("message", ex.getMessage());

                String error = ex.getCause() != null ? ex.getCause().toString() : ex.getMessage();

                auditService.logCustomEvent(
                                userContextService.getUsername() != null
                                                ? userContextService.getUsername()
                                                : "anonymous",
                                "AccessDeniedException 403",
                                Map.of(
                                                "errors", error,
                                                "url", request.getRequestURI() != null
                                                                ? request.getRequestURI()
                                                                : "N/A",
                                                "method", request.getMethod() != null
                                                                ? request.getMethod()
                                                                : "N/A",
                                                "handler", handler.getClass().getSimpleName() != null
                                                                ? handler.getClass().getSimpleName()
                                                                : "N/A"));

                return "error/403";
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public String handleStaticResource404(NoResourceFoundException ex, HttpServletRequest request, Object handler,
                        Model model) {
                model.addAttribute("error", "Archivo no encontrado");
                model.addAttribute("message", ex.getResourcePath());

                String error = ex.getCause() != null ? ex.getCause().toString() : ex.getMessage();

                auditService.logCustomEvent(
                                userContextService.getUsername() != null
                                                ? userContextService.getUsername()
                                                : "anonymous",
                                "NoResourceFoundException 404",
                                Map.of(
                                                "errors", error,
                                                "url", request.getRequestURI() != null
                                                                ? request.getRequestURI()
                                                                : "N/A",
                                                "method", request.getMethod() != null
                                                                ? request.getMethod()
                                                                : "N/A",
                                                "handler", handler.getClass().getSimpleName() != null
                                                                ? handler.getClass().getSimpleName()
                                                                : "N/A"));

                return "error/404";
        }

        @ExceptionHandler(Exception.class)
        public String handleGeneralException(Exception ex, HttpServletRequest request, Object handler, Model model) {
                model.addAttribute("error", "Error inesperado");
                model.addAttribute("message", ex.getMessage());

                String error = ex.getCause() != null ? ex.getCause().toString() : ex.getMessage();

                auditService.logCustomEvent(
                                userContextService.getUsername() != null
                                                ? userContextService.getUsername()
                                                : "anonymous",
                                "Exception 500",
                                Map.of(
                                                "errors", error != null ? error : "N/A",
                                                "url", request.getRequestURI(),
                                                "method", request.getMethod() != null
                                                                ? request.getMethod()
                                                                : "N/A",
                                                "handler", handler.getClass().getSimpleName() != null
                                                                ? handler.getClass().getSimpleName()
                                                                : "N/A"));

                return "error/500";
        }

}
