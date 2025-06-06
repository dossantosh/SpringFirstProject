package com.dossantosh.springfirstproject.user.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.custom.login.CustomAuthenticationSuccessHandler;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;

import com.dossantosh.springfirstproject.user.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequiereModule({ 2L })
public class ProfileController extends GenericController {

    private final UserService userService;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public ProfileController( PermisosUtils permisosUtils, UserService userService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        super(permisosUtils);
        this.userService = userService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @GetMapping("/user/profile")
    public String showPerfilPanel(Model model, HttpSession session) {

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "profile");

        UserAuth userAuth = (UserAuth)model.getAttribute("userAuth");

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

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        UserAuth userAuth = (UserAuth) model.getAttribute("userAuth");

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
            HttpServletResponse response) throws IOException, ServletException {

        userService.guardarUsuario(user, userService.findById(user.getId()));

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
            return "redirect:/objects/perfume";
        }

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");

        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, null);
        return "redirect:/user/profile";
    }
}