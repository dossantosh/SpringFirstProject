package com.dossantosh.springfirstproject.common.controllers;

import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.dossantosh.springfirstproject.common.security.custom.PermisosUtils;
import com.dossantosh.springfirstproject.user.models.UserAuth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenericController {

    private final PermisosUtils permisosUtils;

    protected void addPrincipalAttributes(Model model,
            Set<Long> readAll, Set<Long> writeAll, Set<Long> readUsers, Set<Long> writeUsers, Set<Long> readPerfumes,
            Set<Long> writePerfumes) {

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("userAuth", userAuth);

        if (readAll != null) {
            model.addAttribute("readAll",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), readAll));
        }
        if (writeAll != null) {
            model.addAttribute("writeAll",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), writeAll));
        }
        if (readUsers != null) {
            model.addAttribute("readUsers",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), readUsers));
        }
        if (writeUsers != null) {
            model.addAttribute("writeUsers",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), writeUsers));
        }
        if (readPerfumes != null) {
            model.addAttribute("readPerfumes",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), readPerfumes));
        }
        if (writePerfumes != null) {
            model.addAttribute("writePerfumes",
                    permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), writePerfumes));
        }
    }
}