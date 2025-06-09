package com.dossantosh.springfirstproject.perfume.controller;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.service.BrandService;
import com.dossantosh.springfirstproject.perfume.service.PerfumeService;
import com.dossantosh.springfirstproject.perfume.utils.PerfumeLockManager;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    public PerfumeController(PermisosUtils permisosUtils,
                             PerfumeService perfumeService,
                             BrandService brandService,
                             PerfumeLockManager perfumeLockManager) {
        super(permisosUtils);
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
            @RequestParam(defaultValue = "10") int size,
            Model model,
            HttpSession session
    ) {

        Boolean isLockedByAnother = (Boolean) model.getAttribute("isLockedByAnother");

        if (isLockedByAnother == null) {
            isLockedByAnother = false;
        }

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();
        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "perfumes");

        if (name != null && name.isBlank()) {
            name = null;
        }
        if (brand != null && brand.isBlank()) {
            brand = null;
        }
        if (season != null && season.isBlank()) {
            season = null;
        }

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

        perfumeService.cargarListaInfo().forEach(model::addAttribute);

        model.addAttribute("isLockedByAnother", isLockedByAnother);

        return "objects/perfume";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarPerfume(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (perfumeLockManager.isLockedByAnother(id, userName)) {
            redirectAttrs.addFlashAttribute("isLockedByAnother", true);

            Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
            if (anterior != null && !anterior.getId().equals(id)) {
                perfumeLockManager.releaseLock(anterior.getId(), userName);
            }

            session.setAttribute("selectedPerfume", perfumeService.findById(id));

            return "redirect:/objects/perfume";
        }

        redirectAttrs.addFlashAttribute("isLockedByAnother", false);

        if (perfumeLockManager.lockedBy(id) != null) {
            perfumeLockManager.refreshLock(id, userName);
            return "redirect:/objects/perfume";
        }

        Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
        if (anterior != null && !anterior.getId().equals(id)) {
            perfumeLockManager.releaseLock(anterior.getId(), userName);
        }

        session.setAttribute("selectedPerfume", null);

        if (perfumeLockManager.tryLock(id, userName)) {
            session.setAttribute("selectedPerfume", perfumeService.findById(id));
        }

        return "redirect:/objects/perfume";
    }

    @PostMapping("/guardar")
    public String guardarPerfume(@Valid @ModelAttribute Perfumes perfume,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttrs) {
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

            session.setAttribute("selectedPerfume", null);

            perfumeLockManager.releaseLock(perfume.getId(), SecurityContextHolder.getContext().getAuthentication().getName());
            
            return "redirect:/objects/perfume";

        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/objects/perfume";
        }
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        perfumeLockManager.releaseLock(id, SecurityContextHolder.getContext().getAuthentication().getName());

        session.setAttribute("selectedPerfume", null);

        return "redirect:/objects/perfume";
    }

    @PostMapping(value = "/refreshLock/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> refreshLock(@PathVariable Long id, HttpSession session) {

        perfumeLockManager.refreshLock(id, SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok().build();
    }
}