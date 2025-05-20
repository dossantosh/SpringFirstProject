package com.example.springfirstproject.controller.User;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.springfirstproject.service.User.UserChikitoService;
import com.example.springfirstproject.service.User.UserService;

import lombok.AllArgsConstructor;

// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@AllArgsConstructor
@Controller
@RequiereModulo({ 2L })
public class PerfilController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/perfil")
    public String showPerfilPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

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
        return "perfil";
    }
}
