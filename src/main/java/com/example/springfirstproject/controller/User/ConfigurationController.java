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
import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.service.user.UserAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequiereModulo({ 2L })
public class ConfigurationController {

    private final UserAuthService userAuthService;

    @GetMapping("/configuracion")
    public String showAdminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        model.addAttribute("userAuth", userAuth.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        return "configuracion";
    }
}