package com.dossantosh.springfirstproject.common.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.ui.Model;

import com.dossantosh.springfirstproject.user.models.UserAuth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenericController {


    private final PermisosUtils permisosUtils;

    protected void addPrincipalAttributes(Model model, HttpSession session,
            Set<Long> lecturaMod, Set<Long> escrituraMod, Set<Long> lecturaSub, Set<Long> escrituraSub) {

        HashMap<String, Set<Long>> mod = new HashMap<>();
        HashMap<String, Set<Long>> sub = new HashMap<>();

        HashMap<String, HashMap<String, Set<Long>>> permisos = new HashMap<>();

        mod.put("lectura", lecturaMod);
        mod.put("escritura", escrituraMod);

        sub.put("lectura", lecturaSub);
        sub.put("escritura", escrituraSub);

        permisos.put("modulos", mod);
        permisos.put("submodulos", sub);

        UserAuth userAuth = (UserAuth) session.getAttribute("userAuth");

        model.addAttribute("userAuth", userAuth);

        for (Map.Entry<String, HashMap<String, Set<Long>>> modSub : permisos.entrySet()) {
            String nombrePermiso = modSub.getKey();
            HashMap<String, Set<Long>> tipos;

            switch (nombrePermiso) {
                case "modulos":
                    tipos = modSub.getValue();
                    for (Map.Entry<String, Set<Long>> tipo : tipos.entrySet()) {
                        String tipoNombre = tipo.getKey();
                        Set<Long> valores;
                        switch (tipoNombre) {
                            case "lectura":
                                valores = tipo.getValue();

                                model.addAttribute("lecturaMod", valores);
                                model.addAttribute("puedeVerMod",
                                        permisosUtils.contieneAlgunModulo(userAuth.getModules(), valores));
                                break;
                            default:
                                break;
                        }
                    }
                    break;

                case "submodulos":
                    tipos = modSub.getValue();
                    for (Map.Entry<String, Set<Long>> tipo : tipos.entrySet()) {
                        String tipoNombre = tipo.getKey();
                        Set<Long> valores;
                        switch (tipoNombre) {
                            case "lectura":
                                valores = tipo.getValue();

                                model.addAttribute("lecturaSub", valores);
                                model.addAttribute("puedeVerMod",
                                        permisosUtils.contieneAlgunModulo(userAuth.getModules(), valores));
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