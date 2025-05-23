package com.example.springfirstproject.user.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.common.config.anotaciones.module.Requieremodule;
import com.example.springfirstproject.permisos.modules.model.Modules;
import com.example.springfirstproject.permisos.roles.model.Roles;
import com.example.springfirstproject.permisos.submodules.model.Submodules;
import com.example.springfirstproject.user.models.User;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;
import com.example.springfirstproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Requieremodule({ 2L })
public class PerfilController {

    private final UserService userService;

    private final UserAuthService userAuthService;

    @GetMapping("/perfil")
    public String showPerfilPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        model.addAttribute("userAuth", userAuth.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        Optional<User> user = userService.findByUsername(auth.getName());

        if(!user.isPresent()){
            return null;
        }

        User userExistente = user.get();
        model.addAttribute("user", userExistente);

        StringBuilder sbRol = new StringBuilder();
        for (Roles rol : userExistente.getRoles()) {
            sbRol.append(rol.getName() + " \n");
        }
        StringBuilder sbMod = new StringBuilder();
        for (Modules mod : userExistente.getModules()) {
            sbMod.append(mod.getName() + " \n");
        }
        StringBuilder sbSub = new StringBuilder();
        for (Submodules sub : userExistente.getSubmodules()) {
            sbSub.append(sub.getName() + " \n");
        }

        model.addAttribute("listaRol", sbRol);
        model.addAttribute("listaMod", sbMod);
        model.addAttribute("listaSub", sbSub);
        return "perfil";
    }
}
