package com.dossantosh.springfirstproject.common.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
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

        List<Long> readAll = List.of(1L);
        List<Long> writeAll = List.of(2L);

        List<Long> readUsers = List.of(3L);
        List<Long> writeUsers = List.of(4L);

        List<Long> readPerfumes = List.of(5L);
        List<Long> writePerfumes = List.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}