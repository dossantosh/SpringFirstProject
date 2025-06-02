package com.dossantosh.springfirstproject.common;

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

    public ConfigurationController(UserAuthService userAuthService) {
        super(userAuthService);
    }

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(2L);
        escrituraMod.add(2L);
        lecturaSub.add(2L);
        escrituraSub.add(2L);
        
        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}