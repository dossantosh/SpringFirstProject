package com.dossantosh.springfirstproject.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.global.page.Direction;
import com.dossantosh.springfirstproject.common.global.page.KeysetPage;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.service.UserService;
import com.dossantosh.springfirstproject.user.utils.projections.UserDTO;

import java.util.*;

@Controller
@RequestMapping("/user/users")
@RequireModule({ 2L })
public class UsuariosController extends GenericController {

    private final UserService userService;

    private final SessionService sessionService;

    public UsuariosController(UserContextService userContextService, PermisosUtils permisosUtils,
            UserService userService,
            SessionService sessionService) {
        super(userContextService, permisosUtils);
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String mostrarUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "NEXT") String direction,
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

        if (lastId == null || lastId < 0) {
            lastId = 0L; // Asegurarse de que lastId sea un valor válido
        }

        Direction dir = Direction.valueOf(direction.toUpperCase());

        KeysetPage<UserDTO> pageResult = userService.findUsersKeyset(id, username, email, lastId, size, dir);

        if (pageResult == null || pageResult.isEmpty() && lastId > 0) {
            return "redirect:/user/users";
        }

        model.addAttribute("users", pageResult.getContent());
        model.addAttribute("nextId", pageResult.getNextId()); // Para botón "Siguiente"
        model.addAttribute("previousId", pageResult.getPreviousId()); // Opcional: para "Anterior"
        model.addAttribute("hasNext", pageResult.isHasNext());
        model.addAttribute("direction", direction);

        model.addAttribute("selectedUser", session.getAttribute("selectedUser"));

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("username", username);
        filters.put("email", email);
        model.addAttribute("filters", filters);

        // Cargar listas solo si hay usuario seleccionado
  
            userService.cargarListasFormulario().forEach(model::addAttribute);

        return "user/users";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarUsuario(@PathVariable Long id, HttpSession session) {
        if (!userService.existsById(id)) {
            return "redirect:/user/users";
        }

        session.setAttribute("selectedUser", userService.findFullUserById(id));
        return "redirect:/user/users";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute User user, HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {

        userService.modifyUser(user, userService.findFullUserById(user.getId()));

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

    @GetMapping("/delete/{id}")
    public String borrarUsuario(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        List<Long> writeUsersPermission = List.of(4L);

        if (!permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writeUsersPermission)) {
            redirectAttrs.addFlashAttribute("error", "No tienes permisos para borrar usuarios.");
            return "redirect:/user/users";
        }

        if (!userService.existsById(id)) {
            redirectAttrs.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/user/users";
        }

        session.setAttribute("selectedUser", null);

        User user = userService.findFullUserById(id);

        List<String> primaryIdList = sessionService.findPrimaryIdsByPrincipalName(user.getUsername());

        if (!primaryIdList.isEmpty()) {

            sessionService.invalidateSessionsById(id);
        }

        userService.deleteById(id);

        return "redirect:/user/users";
    }
}