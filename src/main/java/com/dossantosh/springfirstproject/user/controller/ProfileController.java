package com.dossantosh.springfirstproject.user.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.global.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;
import com.dossantosh.springfirstproject.user.models.User;

import com.dossantosh.springfirstproject.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;

@Controller
@RequireModule({ 1L })
public class ProfileController extends GenericController {

    private final UserService userService;

    private final SessionService sessionService;

    public ProfileController(UserContextService userContextService, PermisosUtils permisosUtils,
            UserService userService, SessionService sessionService) {
        super(userContextService, permisosUtils);
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/user/profile")
    public String showPerfilPanel(Model model, HttpSession session) {

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);

        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "profile");

        model.addAttribute("user", userService.findById(userContextService.getId()));

        model.addAttribute("listaRol", userContextService.getRoles());
        model.addAttribute("listaMod", userContextService.getModules());
        model.addAttribute("listaSub", userContextService.getSubmodules());
        return "user/profile";
    }

    @GetMapping("/user/editar") // /user/editar
    public String mostrarFormularioEdicion(Model model, HttpSession session) {

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);

        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("user", userService.findFullUserById(userContextService.getId()));

        userService.cargarListasFormulario().forEach(model::addAttribute);
        return "user/editar";
    }

    @PostMapping("/user/editar")
    public String procesarFormularioEdicion(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
            return "redirect:/user/editar";
        }

        if(userService.existsByEmail(user.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "El correo electrónico ya está en uso.");
            return "redirect:/user/editar";
        }

        userService.modifyUser(user, userService.findById(user.getId()));

        redirectAttrs.addFlashAttribute("success", "Perfil actualizado correctamente");

        sessionService.refreshAuthentication(userService.userToUserAuth(user));
        return "redirect:/user/profile";
    }
}