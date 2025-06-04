package com.dossantosh.springfirstproject.common.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

@Controller
@RequiereModule({ 2L })
public class ConfigurationController extends GenericController {

    public ConfigurationController(UserAuthService userAuthService, PermisosUtils permisosUtils) {
        super(userAuthService, permisosUtils);
    }

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);
        
        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}