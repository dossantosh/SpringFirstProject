package com.dossantosh.springfirstproject.perfume.controller;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.service.BrandService;
import com.dossantosh.springfirstproject.perfume.service.PerfumeService;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public PerfumeController(UserAuthService userAuthService, PermisosUtils permisosUtils,
            PerfumeService perfumeService,
            BrandService brandService,
            PerfumeLockManager perfumeLockManager) {
        super(userAuthService, permisosUtils);
        this.perfumeService = perfumeService;
        this.brandService = brandService;
        this.perfumeLockManager = perfumeLockManager;
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
        return "objects/perfume";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarPerfume(@PathVariable Long id, RedirectAttributes redirectAttrs, HttpSession session) {

        String name = (String) session.getAttribute("username");

        if (perfumeLockManager.isLockedByAnother(id, name)) {

            redirectAttrs.addFlashAttribute("isLockedByAnother", true);

            Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
            if (anterior != null && !anterior.getId().equals(id)) {
                perfumeLockManager.releaseLock(anterior.getId(), name);
            }

            session.setAttribute("selectedPerfume", perfumeService.findById(id));

            return "redirect:/objects/perfume";
        }
        redirectAttrs.addFlashAttribute("isLockedByAnother", false);

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
            perfumeLockManager.refreshLock(id, name);
        }

        return "redirect:/objects/perfume";
    }

    @PostMapping("/guardar")
    public String guardarPerfume(@ModelAttribute Perfumes perfume,
            RedirectAttributes redirectAttrs,
            HttpSession session) {
        try {
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
}