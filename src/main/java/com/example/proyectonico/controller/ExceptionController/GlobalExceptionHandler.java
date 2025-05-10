package com.example.proyectonico.controller.ExceptionController;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.proyectonico.service.UserChikitoService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final UserChikitoService userChikitoService;

    //@Autowired
    public GlobalExceptionHandler(UserChikitoService userChikitoService) {
        this.userChikitoService = userChikitoService;
    }

    // Maneja recursos estáticos no encontrados (ej: /no-existe.js)
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleStaticResource404(NoResourceFoundException ex, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getName();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        model.addAttribute("error", "Archivo no encontrado: " + ex.getResourcePath());
        return "error/404";
    }

    @ExceptionHandler({ DataAccessResourceFailureException.class, CannotGetJdbcConnectionException.class })
    public ModelAndView handleDatabaseConnectionFailure(HttpServletRequest request) {
        return new ModelAndView("redirect:/login?error=dbConnectionLost");
    }

    // 404 Maneja rutas de controladores no existentes (ej: /ruta-falsa)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleController404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", "Página no encontrada: " + ex.getRequestURL());
        return "error/404";
    }

    // 500 - Error interno del servidor (ej: NullPointerException)
    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, Model model) {
        model.addAttribute("error", "Error interno del servidor" + ex.getMessage());
        return "error/500";
    }

    // 403 - Acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public String handle403(AccessDeniedException ex, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getName();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        /* 
        String currentUsername = principal.getName(); // Obtiene el usuario autenticado
        User user = userService.findByUsername(currentUsername);

        model.addAttribute("modulos", user.getModules());
        model.addAttribute("submodulos", user.getSubmodules());
        */ 
        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        model.addAttribute("error", "No tienes permisos para acceder a este recurso");
        return "error/403";
    }

    // 400 - Bad Request (ej: @Valid fallido)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle400(MethodArgumentNotValidException ex, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getName();
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        model.addAttribute("error", "Esa url no existe");
        model.addAttribute("message", ex.getMessage());
        return "error/400";
    }
}