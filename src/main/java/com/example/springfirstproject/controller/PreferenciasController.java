package com.example.springfirstproject.controller;

import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;
import com.example.springfirstproject.models.Preferencias;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.service.PreferenciasService;
import com.example.springfirstproject.service.UserChikitoService;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@RequiereModulo({ 2L })
@ControllerAdvice
@Controller
public class PreferenciasController {
    @Autowired
    private final UserChikitoService userChikitoService;

    @Autowired
    private PreferenciasService preferenciasService;

    @GetMapping("/preferencias")
    public String mostrarPreferencias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        model.addAttribute("chikito", userCh);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        Preferencias preferencias = preferenciasService.obtenerPreferencias(userCh.getId());
        model.addAttribute("preferencias", preferencias != null ? preferencias : new Preferencias());

        return "preferencias";
    }

    @PostMapping("/preferencias")
    public String guardarPreferencias(@ModelAttribute Preferencias preferencias, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserChikito userCh = userChikitoService.findByUsername(auth.getName());
        preferencias.setUserId(userCh.getId());

        try {
            preferenciasService.guardarPreferencias(preferencias);
            redirectAttributes.addFlashAttribute("successMessage", "Preferencias guardadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al guardar las preferencias.");
            e.printStackTrace();
        }

        return "redirect:/configuracion";
    }
}
