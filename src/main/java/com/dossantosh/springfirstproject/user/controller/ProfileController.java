package com.dossantosh.springfirstproject.user.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotation.module.Requieremodule;
import com.dossantosh.springfirstproject.permisos.model.Modules;
import com.dossantosh.springfirstproject.permisos.model.Roles;
import com.dossantosh.springfirstproject.permisos.model.Submodules;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;
import com.dossantosh.springfirstproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Requieremodule({ 2L })
public class ProfileController {

    private final UserService userService;

    private final UserAuthService userAuthService;

    @GetMapping("/user/profile")
    public String showPerfilPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        model.addAttribute("userAuth", userAuth);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        User user = userService.findByUsername(auth.getName());

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
}
