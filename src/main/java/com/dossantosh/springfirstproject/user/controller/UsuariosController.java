package com.dossantosh.springfirstproject.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequiereModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.service.UserService;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user/users")
@RequiereModule({ 2L })
public class UsuariosController extends GenericController {

    private final UserService userService;

    private final SessionService sessionService;

    private final Set<Long> writeUsers;

    public UsuariosController(UserContextService userContextService, PermisosUtils permisosUtils,
            UserService userService,
            SessionService sessionService) {
        super(userContextService, permisosUtils);
        this.userService = userService;
        this.sessionService = sessionService;

        Set<Long> writeUsersTemp = new HashSet<>();
        writeUsersTemp.add(4L);

        this.writeUsers = writeUsersTemp;
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

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);

        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "users");

        // Convertir cadenas vacías a null
        if (username != null && username.isBlank()) {
            username = null;
        }
        if (email != null && email.isBlank()) {
            email = null;
        }

        // Obtener página de usuarios filtrados
        Page<User> pageResult;

        if (permisosUtils.isAdmin(SecurityContextHolder.getContext().getAuthentication())) {

            pageResult = userService.findByFiltersAdmin(id, username, email, page, size, "id", "ASC");

        } else {

            pageResult = userService.findByFiltersUser(id, username, email, page, size, "id", "ASC");
        }

        if (pageResult == null || pageResult.isEmpty() && page > 0) {
            return "redirect:/user/users";
        }

        model.addAttribute("users", userService.convertirUsuariosADTO(pageResult.getContent()));

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

        userService.modifyUser(user, userService.findById(user.getId()));

        List<String> primaryIdList = sessionService.findPrimaryIdsByPrincipalName(user.getUsername());

        if (!primaryIdList.isEmpty()) {

            sessionService.updateSecurityContextForUser(userService.userToUserAuth(user));
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

    @DeleteMapping("/delete/{id}")
    public String borrarUsuario(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        if (!permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writeUsers)) {
            redirectAttrs.addFlashAttribute("error", "No tienes permisos para borrar usuarios.");
            return "redirect:/user/users";
        }

        if (!userService.existsById(id)) {
            redirectAttrs.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/user/users";
        }

        userService.deleteById(id);

        session.setAttribute("selectedUser", null);

        

        return "redirect:/user/users";
    }
}