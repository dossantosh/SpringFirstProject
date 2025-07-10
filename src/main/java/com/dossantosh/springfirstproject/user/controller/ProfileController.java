package com.dossantosh.springfirstproject.user.controller;

import java.io.IOException;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuth;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.others.SessionService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
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

        List<Long> readAll = List.of(1L);
        List<Long> writeAll = List.of(2L);

        List<Long> readUsers = List.of(3L);
        List<Long> writeUsers = List.of(4L);

        List<Long> readPerfumes = List.of(5L);
        List<Long> writePerfumes = List.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "profile");

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findById(userAuth.getId());

        model.addAttribute("user", user);

        StringBuilder sbRol = new StringBuilder();
        for (Roles rol : user.getRoles()) {
            sbRol.append(rol.getName() + " \n");
        }
        StringBuilder sbMod = new StringBuilder();
        for (Modules mod : user.getModules()) {
            sbMod.append(mod.getName() + " \n");
        }
        StringBuilder sbSub = new StringBuilder();
        for (Submodules sub : user.getSubmodules()) {
            sbSub.append(sub.getName() + " \n");
        }

        model.addAttribute("listaRol", sbRol);
        model.addAttribute("listaMod", sbMod);
        model.addAttribute("listaSub", sbSub);
        return "user/profile";
    }

    @GetMapping("/user/editar") // /user/editar
    public String mostrarFormularioEdicion(Model model, HttpSession session) {

        List<Long> readAll = List.of(1L);
        List<Long> writeAll = List.of(2L);

        List<Long> readUsers = List.of(3L);
        List<Long> writeUsers = List.of(4L);

        List<Long> readPerfumes = List.of(5L);
        List<Long> writePerfumes = List.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("user", userService.findById(userAuth.getId()));

        userService.cargarListasFormulario().forEach(model::addAttribute);
        return "user/editar";
    }

    @PostMapping("/user/editar")
    public String procesarFormularioEdicion(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            RedirectAttributes flash,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        userService.modifyUser(user, userService.findById(user.getId()));

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
            return "redirect:/objects/perfume";
        }

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");

        sessionService.refreshAuthentication(userService.userToUserAuth(user));
        return "redirect:/user/profile";
    }
}