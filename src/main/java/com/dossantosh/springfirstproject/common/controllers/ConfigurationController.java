package com.dossantosh.springfirstproject.common.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;

import jakarta.servlet.http.HttpSession;


@Controller
@RequiereModule({ 2L })
public class ConfigurationController extends GenericController {

    public ConfigurationController( PermisosUtils permisosUtils) {
        super(permisosUtils);
    }

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model, HttpSession session) {

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);
        
        addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}