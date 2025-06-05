package com.dossantosh.springfirstproject.common.global;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ DataAccessResourceFailureException.class, CannotGetJdbcConnectionException.class })
    public ModelAndView handleDatabaseConnectionFailure(HttpServletRequest request) {
        return new ModelAndView("redirect:/login?error=dbConnectionLost");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle400(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("error", "Solicitud inválida");
        model.addAttribute("message", ex.getMessage());
        return "error/400";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handle403(AccessDeniedException ex, Model model) {
        model.addAttribute("error", "No tienes permisos para acceder a este recurso");
        model.addAttribute("message", ex.getMessage());
        return "error/403";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleStaticResource404(NoResourceFoundException ex, Model model) {
        model.addAttribute("error", "Archivo no encontrado");
        model.addAttribute("message", ex.getResourcePath());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "Error inesperado");
        model.addAttribute("message", ex.getMessage());
        return "error/500";
    }

}
