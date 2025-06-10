package com.dossantosh.springfirstproject.common.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.security.custom.PermisosUtils;
import com.dossantosh.springfirstproject.common.security.custom.annotations.module.RequiereModule;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiereModule({ 1L })
public class ConfigurationController extends GenericController {

    public ConfigurationController(PermisosUtils permisosUtils) {
        super(permisosUtils);
    }

    @GetMapping("/common/configuration")
    public String showAdminPanel(Model model, HttpSession session) {

        Set<Long> readAll = new HashSet<>();
        Set<Long> writeAll = new HashSet<>();

        Set<Long> readUsers = new HashSet<>();
        Set<Long> writeUsers = new HashSet<>();

        Set<Long> readPerfumes = new HashSet<>();
        Set<Long> writePerfumes = new HashSet<>();

        readAll.add(1L);
        writeAll.add(2L);
        readUsers.add(3L);
        writeUsers.add(4L);
        readPerfumes.add(5L);
        writePerfumes.add(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

        model.addAttribute("activeNavLink", "configuration");

        return "common/configuration";
    }
}