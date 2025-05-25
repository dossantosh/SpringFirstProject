package com.example.springfirstproject.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.springfirstproject.common.config.annotation.module.Requieremodule;
import com.example.springfirstproject.user.models.User;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;
import com.example.springfirstproject.user.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Requieremodule({ 2L })
public class UsuariosController {

    private final UserAuthService userAuthService;
    private final UserService userService;

    @GetMapping
    public String mostrarUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Model model,
            HttpSession session) {

        addCommonAttributes(model);

        // Convertir cadenas vacías a null
        if (username != null && username.isBlank()) {
            username = null;
        }
        if (email != null && email.isBlank()) {
            email = null;
        }

        // Obtener página de usuarios filtrados
        Page<User> pageResult = userService.findByFilters(id, username, email, page, size);

        if (pageResult == null) {
            return "principal";
        }

        model.addAttribute("users", userService.convertirUsuariosAUserAuth(pageResult.getContent()));
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());
        model.addAttribute("selectedUser", session.getAttribute("selectedUser"));

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("username", username);
        filters.put("email", email);
        model.addAttribute("filters", filters);

        // model.addAllAttributes(userService.cargarListasFormulario());
        userService.cargarListasFormulario().forEach(model::addAttribute);
        return "usuarios";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarUsuario(@PathVariable Long id, HttpSession session) {
        userService.findById(id)
                .ifPresent(user -> session.setAttribute("selectedUser", user));
        return "redirect:/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute User user, HttpSession session) {

        userService.guardarUsuarioConPermisos(user, userService.findById(user.getId()).get());
        session.removeAttribute("selectedUser");
        return "redirect:/usuarios";
    }

    @GetMapping("/cancelar")
    public String cancelarEdicion(HttpSession session) {
        // Limpiar el seleccionado en sesión
        session.removeAttribute("selectedUser");
        return "redirect:/usuarios";
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());
        if (auth.getName().equals("anonymousUser")) {
            return;
        }

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return;
        }
        model.addAttribute("userAuth", userAuth.get());

        // Ejemplo de módulo necesario (ajustar según lógica de permisos)
        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);
    }
}
