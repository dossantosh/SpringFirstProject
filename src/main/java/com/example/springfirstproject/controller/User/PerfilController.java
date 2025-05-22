package com.example.springfirstproject.controller.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.modulo.RequiereModulo;
import com.example.springfirstproject.models.permisos.Modules;
import com.example.springfirstproject.models.permisos.Roles;
import com.example.springfirstproject.models.permisos.Submodules;
import com.example.springfirstproject.models.user.User;
import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.service.user.UserAuthService;
import com.example.springfirstproject.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequiereModulo({ 2L })
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
        model.addAttribute("modulosNecesarios", lista);

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
