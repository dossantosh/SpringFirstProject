package com.dossantosh.springfirstproject.common.controllers;

import java.util.HashMap;
import java.util.Map;
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

        HashMap<String, Set<Long>> all = new HashMap<>();
        HashMap<String, Set<Long>> users = new HashMap<>();
        HashMap<String, Set<Long>> perfumes = new HashMap<>();

        HashMap<String, HashMap<String, Set<Long>>> permisos = new HashMap<>();

        all.put("read", readAll);
        all.put("write", writeAll);

        users.put("read", readUsers);
        users.put("write", writeUsers);

        perfumes.put("read", readPerfumes);
        perfumes.put("write", writePerfumes);

        permisos.put("all", all);
        permisos.put("users", users);
        permisos.put("perfumes", perfumes);

        UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("userAuth", userAuth);

        for (Map.Entry<String, HashMap<String, Set<Long>>> type : permisos.entrySet()) {
            String nameType = type.getKey();
            HashMap<String, Set<Long>> tipos;

            switch (nameType) {
                case "all":
                    tipos = type.getValue();
                    for (Map.Entry<String, Set<Long>> tipo : tipos.entrySet()) {
                        String tipoNombre = tipo.getKey();
                        Set<Long> valores;
                        switch (tipoNombre) {
                            case "read":
                                valores = tipo.getValue();

                                model.addAttribute("readAll",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            case "write":
                                valores = tipo.getValue();

                                model.addAttribute("writeAll",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            default:
                                break;
                        }
                    }
                    break;

                case "users":
                    tipos = type.getValue();
                    for (Map.Entry<String, Set<Long>> tipo : tipos.entrySet()) {
                        String tipoNombre = tipo.getKey();
                        Set<Long> valores;
                        switch (tipoNombre) {
                            case "read":
                                valores = tipo.getValue();

                                model.addAttribute("readUsers",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            case "write":
                                valores = tipo.getValue();

                                model.addAttribute("writeUsers",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            default:
                                break;
                        }
                    }
                    break;

                case "perfumes":
                    tipos = type.getValue();
                    for (Map.Entry<String, Set<Long>> tipo : tipos.entrySet()) {
                        String tipoNombre = tipo.getKey();
                        Set<Long> valores;
                        switch (tipoNombre) {
                            case "read":
                                valores = tipo.getValue();

                                model.addAttribute("readPerfumes",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            case "write":
                                valores = tipo.getValue();

                                model.addAttribute("writePerfumes",
                                        permisosUtils.contieneAlgunSubmodulo(userAuth.getSubmodules(), valores));
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}