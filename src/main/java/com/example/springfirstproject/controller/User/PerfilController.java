package com.example.springfirstproject.controller.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.Permisos.Modules;
import com.example.springfirstproject.models.Permisos.Roles;
import com.example.springfirstproject.models.Permisos.Submodules;
import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.service.User.UserChikitoService;
import com.example.springfirstproject.service.User.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequiereModulo({ 2L })
public class PerfilController {

    private final UserService userService;

    private final UserChikitoService userChikitoService;

    @GetMapping("/perfil")
    public String showPerfilPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserChikito> userCh = userChikitoService.findByUsername(auth.getName());
        if (!userCh.isPresent()) {
            return null;
        }
        model.addAttribute("chikito", userCh.get());

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
