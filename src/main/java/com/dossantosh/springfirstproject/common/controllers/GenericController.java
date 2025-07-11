package com.dossantosh.springfirstproject.common.controllers;

import java.util.Set;

import org.springframework.ui.Model;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenericController {

    protected final UserContextService userContextService;

    protected final PermisosUtils permisosUtils;

    protected void addPrincipalAttributes(Model model,
            Set<Long> readAll, Set<Long> writeAll, Set<Long> readUsers, Set<Long> writeUsers,
            Set<Long> readPerfumes,
            Set<Long> writePerfumes) {

        model.addAttribute("userAuth", userContextService);

        if (readAll != null) {
            model.addAttribute("readAll",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), readAll));
        }
        if (writeAll != null) {
            model.addAttribute("writeAll",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writeAll));
        }
        if (readUsers != null) {
            model.addAttribute("readUsers",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), readUsers));
        }
        if (writeUsers != null) {
            model.addAttribute("writeUsers",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writeUsers));
        }
        if (readPerfumes != null) {
            model.addAttribute("readPerfumes",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), readPerfumes));
        }
        if (writePerfumes != null) {
            model.addAttribute("writePerfumes",
                    permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writePerfumes));
        }
    }
}