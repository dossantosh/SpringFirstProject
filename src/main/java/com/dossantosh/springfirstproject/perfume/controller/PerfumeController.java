package com.dossantosh.springfirstproject.perfume.controller;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.service.BrandService;
import com.dossantosh.springfirstproject.perfume.service.PerfumeService;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/objects/perfume")
@RequiereModule({ 2L })
public class PerfumeController extends GenericController {

    private final PerfumeLockManager perfumeLockManager;

    private final PerfumeService perfumeService;

    private final BrandService brandService;

    private static boolean isBloqued = true;

    public PerfumeController(UserAuthService userAuthService, PermisosUtils permisosUtils,
            PerfumeService perfumeService,
            BrandService brandService,
            PerfumeLockManager perfumeLockManager) {
        super(userAuthService, permisosUtils);
        this.perfumeService = perfumeService;
        this.brandService = brandService;
        this.perfumeLockManager = perfumeLockManager;

        isBloqued = false;
    }

    @GetMapping
    public String mostrarPerfumes(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String season,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Model model,
            HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "perfumes");

        // convertimos en null a las vacias
        if (name != null && name.isBlank()) {
            name = null;
        }
        if (brand != null && brand.isBlank()) {
            brand = null;
        }
        if (season != null && season.isBlank()) {
            season = null;
        }
        // Obtenemos la lista de perfumes
        Page<Perfumes> pageResult = perfumeService.findByFilters(id, name, season, brand, page, size, "id", "ASC");

        model.addAttribute("perfumes", perfumeService.conversorPerfumeDTO(pageResult.getContent()));

        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());
        model.addAttribute("selectedPerfume", session.getAttribute("selectedPerfume"));

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("name", name);
        filters.put("brand", brand);
        filters.put("season", season);
        model.addAttribute("filters", filters);

        // model.addAllAttributes(perfumeService.cargarListaInfo());
        perfumeService.cargarListaInfo().forEach(model::addAttribute);

        model.addAttribute("isLockedByAnother", isBloqued);

        return "objects/perfume";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarPerfume(@PathVariable Long id, RedirectAttributes redirectAttrs, HttpSession session) {

        String name = (String) session.getAttribute("username");

        if (perfumeLockManager.isLockedByAnother(id, name)) {
            isBloqued = true;
            redirectAttrs.addFlashAttribute("isLockedByAnother", isBloqued);

            Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
            if (anterior != null && !anterior.getId().equals(id)) {
                perfumeLockManager.releaseLock(anterior.getId(), name);
            }

            session.setAttribute("selectedPerfume", perfumeService.findById(id));

            return "redirect:/objects/perfume";
        }
        isBloqued = false;
        redirectAttrs.addFlashAttribute("isLockedByAnother", isBloqued);

        if (perfumeLockManager.lockedBy(id) != null) {
            return "redirect:/objects/perfume";
        }

        Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
        if (anterior != null && !anterior.getId().equals(id)) {
            perfumeLockManager.releaseLock(anterior.getId(), name);
        }

        session.removeAttribute("selectedPerfume");

        if (perfumeLockManager.tryLock(id, name)) {
            session.setAttribute("selectedPerfume", perfumeService.findById(id));
        }

        return "redirect:/objects/perfume";
    }

    @PostMapping("/guardar")
    public String guardarPerfume(@Valid @ModelAttribute Perfumes perfume,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            HttpSession session) {
        try {

            if (result.hasErrors()) {
                redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
                return "redirect:/objects/perfume";
            }

            if (perfume.getBrand() != null && perfumeService.existsById(perfume.getId())) {
                Long brandId = perfume.getBrand().getId();
                perfume.setBrand(brandService.findById(brandId));
            }

            perfumeService.save(perfume);

            session.removeAttribute("selectedPerfume");

            String name = (String) session.getAttribute("username");

            perfumeLockManager.releaseLock(perfume.getId(), name);

            return "redirect:/objects/perfume";

        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/objects/perfume";
        }
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, HttpSession session) {

        String name = (String) session.getAttribute("username");

        perfumeLockManager.releaseLock(id, name);

        session.removeAttribute("selectedPerfume");

        return "redirect:/objects/perfume";
    }

    @PostMapping("/objects/perfume/liberar")
    @ResponseBody
    public ResponseEntity<Void> liberarPerfume(
            @RequestParam(required = false) Long id,
            HttpServletRequest request) {

        // Get authentication from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            System.out.println("No authentication found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = auth.getName();

        if (id != null) {
            perfumeLockManager.releaseLock(id, username);
            System.out.println("Released lock for ID: " + id + " by user: " + username);

            // Clear session attribute if it matches the released ID
            HttpSession session = request.getSession(false);
            if (session != null) {
                Perfumes selected = (Perfumes) session.getAttribute("selectedPerfume");
                if (selected != null && selected.getId().equals(id)) {
                    session.removeAttribute("selectedPerfume");
                }
            }

            return ResponseEntity.ok().build();
        }

        System.out.println("No ID provided for release");
        return ResponseEntity.badRequest().build();
    }

}