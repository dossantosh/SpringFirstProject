package com.example.springfirstproject.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springfirstproject.models.User;
import com.example.springfirstproject.service.ModuleService;
import com.example.springfirstproject.service.SubmoduleService;
import com.example.springfirstproject.service.UserService;

import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping("/perfil")
public class EditarController {

    private final UserService userService;
    private final ModuleService moduleService;
    private final SubmoduleService submoduleService;

    // Constructor‑injection (ya no hace falta @Autowired aquí)
    public EditarController(UserService userService,
                            ModuleService moduleService,
                            SubmoduleService submoduleService) {
        this.userService = userService;
        this.moduleService = moduleService;
        this.submoduleService = submoduleService;
    }

    @GetMapping("/editar") // /perfil/editar
    public String mostrarFormularioEdicion(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("todosLosModulos", moduleService.listarTodosModulos());
        model.addAttribute("todosLosSubmodulos", submoduleService.listarTodosSubmodulos());
        return "perfil/editar";
    }

    @PostMapping("/editar") // /perfil/editar
    public String procesarFormularioEdicion(
            @ModelAttribute("user") User userActualizado,
            @RequestParam(value = "modulosSeleccionados", required = false) Set<Long> modulosIds,
            @RequestParam(value = "submodulosSeleccionados", required = false) Set<Long> submodulosIds,
            RedirectAttributes flash
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userExistente = userService.findByUsername(username);

        userExistente.setUsername(userActualizado.getUsername());

        // Manejo de nulos
        Set<Long> modIds = (modulosIds != null ? modulosIds : Collections.emptySet());
        Set<Long> subIds = (submodulosIds != null ? submodulosIds : Collections.emptySet());

        // Asumo que saveUser asigna roles, módulos y submódulos internamente
        userService.saveUser(userExistente, /*roles*/ Set.of(2L), modIds, subIds);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
