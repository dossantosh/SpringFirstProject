package com.dossantosh.springfirstproject.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dossantosh.springfirstproject.common.GenericController;
import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.service.UserAuthService;
import com.dossantosh.springfirstproject.user.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/user/users")
@RequiereModule({ 2L })
public class UsuariosController extends GenericController {

    private final UserService userService;

    public UsuariosController(UserAuthService userAuthService, UserService userService) {
        super(userAuthService);
        this.userService = userService;
    }

    @GetMapping
    public String mostrarUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model,
            HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(2L);
        escrituraMod.add(2L);
        lecturaSub.add(2L);
        escrituraSub.add(2L);
        
        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "users");

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
            return "objects/news";
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
        return "user/users";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarUsuario(@PathVariable Long id, HttpSession session) {
        if (!userService.existsById(id)) {
            return "redirect:/user/users";
        }

        session.setAttribute("selectedUser", userService.findById(id));
        return "redirect:/user/users";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute User user, HttpSession session) {

        userService.guardarUsuarioConPermisos(user, userService.findById(user.getId()));
        session.removeAttribute("selectedUser");
        return "redirect:/user/users";
    }

    @GetMapping("/cancelar")
    public String cancelarEdicion(HttpSession session) {
        // Limpiar el seleccionado en sesión
        session.removeAttribute("selectedUser");
        return "redirect:/user/users";
    }
}
