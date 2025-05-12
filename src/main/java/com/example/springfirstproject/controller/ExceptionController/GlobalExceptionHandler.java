package com.example.springfirstproject.controller.ExceptionController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.springfirstproject.service.UserChikitoService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final UserChikitoService userChikitoService;

    @Autowired
    public GlobalExceptionHandler(UserChikitoService userChikitoService) {
        this.userChikitoService = userChikitoService;
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));
        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleStaticResource404(NoResourceFoundException ex, Model model) {
        addCommonAttributes(model);
        model.addAttribute("error", "Archivo no encontrado");
        model.addAttribute("message", ex.getResourcePath());
        return "error/404";
    }

    @ExceptionHandler({ DataAccessResourceFailureException.class, CannotGetJdbcConnectionException.class })
    public ModelAndView handleDatabaseConnectionFailure(HttpServletRequest request) {
        return new ModelAndView("redirect:/login?error=dbConnectionLost");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle400(MethodArgumentNotValidException ex, Model model) {
        addCommonAttributes(model);
        model.addAttribute("error", "Solicitud inválida");
        model.addAttribute("message", ex.getMessage());
        return "error/400";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handle403(AccessDeniedException ex, Model model) {
        addCommonAttributes(model);
        model.addAttribute("error", "No tienes permisos para acceder a este recurso");
        model.addAttribute("message", ex.getMessage());
        return "error/403";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleController404(NoHandlerFoundException ex, Model model) {
        addCommonAttributes(model);
        model.addAttribute("error", "Página no encontrada");
        model.addAttribute("message", ex.getRequestURL());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, Model model) {
        addCommonAttributes(model);
        model.addAttribute("error", "Error interno del servidor");
        model.addAttribute("message", ex.getMessage());
        return "error/500";
    }
}
