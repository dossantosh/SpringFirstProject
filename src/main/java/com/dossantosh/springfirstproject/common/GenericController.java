package com.dossantosh.springfirstproject.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenericController {

    protected final UserAuthService userAuthService;

    protected void addPrincipalAttributes(Authentication user, Model model,
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

        model.addAttribute("username", user.getName());

        UserAuth userAuth = userAuthService.findByUsername(user.getName());

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