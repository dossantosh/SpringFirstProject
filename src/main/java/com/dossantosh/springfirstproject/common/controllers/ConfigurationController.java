package com.dossantosh.springfirstproject.common.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequireModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;

import jakarta.servlet.http.HttpSession;

@Controller
@RequireModule({ 1L })
public class ConfigurationController extends GenericController {

    public ConfigurationController(UserContextService userContextService, PermisosUtils permisosUtils) {
        super(userContextService, permisosUtils);
    }

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model, HttpSession session) {

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);
        
        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}