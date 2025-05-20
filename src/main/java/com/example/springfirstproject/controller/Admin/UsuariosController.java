package com.example.springfirstproject.controller.Admin;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.service.User.UserChikitoService;
import com.example.springfirstproject.service.User.UserService;

import lombok.AllArgsConstructor;

//@PreAuthorize("hasRole('ADMIN')")  
@AllArgsConstructor
@Controller
@RequiereModulo({ 1L, 2L })
public class UsuariosController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/usuarios")
    public String showUsuarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        model.addAttribute("chikito", userCh);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        SequencedSet<User> setUsuarios = new LinkedHashSet<>();
        for (User usuario : userService.findAll()) {
            setUsuarios.add(usuario);
        }
        model.addAttribute("usuarios", setUsuarios);

        return "usuarios";
    }
}
