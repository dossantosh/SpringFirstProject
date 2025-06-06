package com.dossantosh.springfirstproject.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.custom.login.SessionService;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserService;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user/users")
@RequiereModule({ 1L })
public class UsuariosController extends GenericController {

    private final UserService userService;

    private final SessionService sessionService;

    public UsuariosController(PermisosUtils permisosUtils, UserService userService,
            SessionService sessionService) {
        super(permisosUtils);
        this.userService = userService;
        this.sessionService = sessionService;
        ;
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

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "users");

        // Convertir cadenas vacías a null
        if (username != null && username.isBlank()) {
            username = null;
        }
        if (email != null && email.isBlank()) {
            email = null;
        }

        // Obtener página de usuarios filtrados
        Page<User> pageResult = userService.findByFilters(id, username, email, page, size, "id", "ASC");

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
    public String guardarUsuario(@ModelAttribute User user, HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        userService.guardarUsuario(user, userService.findById(user.getId()));

        List<String> primaryIdList = sessionService.findPrimaryIdsByPrincipalName(user.getUsername());

        if (!primaryIdList.isEmpty()) {
            String primaryId = primaryIdList.get(0);

            UserAuth userAuth = userService.userToUserAuth(user);

            sessionService.addUserAuthAttributeToSession(primaryId, "userAuth", userAuth);
        }

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