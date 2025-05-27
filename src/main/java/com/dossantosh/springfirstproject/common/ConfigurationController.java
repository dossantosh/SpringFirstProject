package com.dossantosh.springfirstproject.common;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotation.module.Requieremodule;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Requieremodule({ 2L })
public class ConfigurationController {

    private final UserAuthService userAuthService;

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        model.addAttribute("userAuth", userAuth);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        return "common/configuration";
    }
}