package com.dossantosh.springfirstproject.perfume.controller;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequiereModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.perfume.models.Brands;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.service.BrandService;
import com.dossantosh.springfirstproject.perfume.service.PerfumeService;
import com.dossantosh.springfirstproject.perfume.utils.lock.PerfumeLockManager;

import java.util.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/objects/perfume")
@RequiereModule({ 3L })
public class PerfumeController extends GenericController {

    private final PerfumeLockManager perfumeLockManager;

    private final PerfumeService perfumeService;

    private final BrandService brandService;

    private final Set<Long> writePerfume;

    public PerfumeController(UserContextService userContextService, PermisosUtils permisosUtils,
            PerfumeLockManager perfumeLockManager,
            PerfumeService perfumeService, BrandService brandService) {
        super(userContextService, permisosUtils);
        this.perfumeLockManager = perfumeLockManager;
        this.perfumeService = perfumeService;
        this.brandService = brandService;

        Set<Long> writePerfumeTemp = new HashSet<>();
        writePerfumeTemp.add(6L);

        this.writePerfume = writePerfumeTemp;
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
            HttpSession session) {

        Boolean isLockedByAnother = (Boolean) session.getAttribute("isLockedByAnother");

        session.setAttribute("isLockedByAnother", isLockedByAnother);

        Set<Long> readAll = Set.of(1L);
        Set<Long> writeAll = Set.of(2L);

        Set<Long> readUsers = Set.of(3L);
        Set<Long> writeUsers = Set.of(4L);

        Set<Long> readPerfumes = Set.of(5L);
        Set<Long> writePerfumes = Set.of(6L);

        addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

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
        model.addAttribute("isLockedByAnother", session.getAttribute("isLockedByAnother"));

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("name", name);
        filters.put("brand", brand);
        filters.put("season", season);
        model.addAttribute("filters", filters);

        perfumeService.cargarListaInfo().forEach(model::addAttribute);

        model.addAttribute("newPerfume", new Perfumes());

        model.addAttribute("newBrand", new Brands());

        return "objects/perfume";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarPerfume(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        if (!permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writePerfume)) {

            session.setAttribute("selectedPerfume", perfumeService.findById(id));

            return "redirect:/objects/perfume";
        }

        if (perfumeLockManager.isLockedByAnother(id, userContextService.getUsername())) {
            session.setAttribute("isLockedByAnother", true);

            Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
            if (anterior != null && !anterior.getId().equals(id)) {
                perfumeLockManager.releaseLock(anterior.getId(), userContextService.getUsername());
            }

            session.setAttribute("selectedPerfume", perfumeService.findById(id));

            return "redirect:/objects/perfume";
        }

        session.setAttribute("isLockedByAnother", null);

        if (perfumeLockManager.lockedBy(id) != null) {
            perfumeLockManager.refreshLock(id, userContextService.getUsername());
            return "redirect:/objects/perfume";
        }

        Perfumes anterior = (Perfumes) session.getAttribute("selectedPerfume");
        if (anterior != null && !anterior.getId().equals(id)) {
            perfumeLockManager.releaseLock(anterior.getId(), userContextService.getUsername());
        }

        session.setAttribute("selectedPerfume", null);

        if (perfumeLockManager.tryLock(id, userContextService.getUsername())) {
            session.setAttribute("selectedPerfume", perfumeService.findById(id));
        }

        return "redirect:/objects/perfume";
    }

    @PutMapping("/guardar")
    public String guardarPerfume(@Valid @ModelAttribute Perfumes perfume,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttrs) {
        try {

            if (result.hasErrors()) {
                redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
                return "redirect:/objects/perfume";
            }

            if (!perfumeService.existsById(perfume.getId())) {
                // Si el perfume no existe, no se puede guardar
                redirectAttrs.addFlashAttribute("error", "No se puede modificar un perfume que no existe.");
                return "redirect:/objects/perfume";
            }

            if (perfume.getBrand() == null || perfume.getBrand().getId() == null) {
                // Si no se ha seleccionado una marca, no se puede guardar
                redirectAttrs.addFlashAttribute("error", "Ha surgido un error con la marca del perfume.");
                return "redirect:/objects/perfume";
            }

            Long brandId = perfume.getBrand().getId();
            perfume.setBrand(brandService.findById(brandId));

            perfumeService.save(perfume);

            session.setAttribute("selectedPerfume", null);

            session.setAttribute("isLockedByAnother", null);

            perfumeLockManager.releaseLock(perfume.getId(),
                    userContextService.getUsername());

            return "redirect:/objects/perfume";

        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/objects/perfume";
        }
    }

    @PostMapping("/brand/create")
    public String guardarMarca(@Valid @ModelAttribute Brands newBrand,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttrs) {
        try {

            if (result.hasErrors()) {
                redirectAttrs.addFlashAttribute("errorMarca", "Revisa los campos del formulario.");
                return "redirect:/objects/perfume";
            }

            brandService.save(newBrand);
            return "redirect:/objects/perfume";

        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("errorMarca", e.getMessage());
            return "redirect:/objects/perfume";
        }
    }


    @PostMapping("/create")
    public String crearPerfume(@Valid @ModelAttribute Perfumes perfume,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttrs) {
        try {

            if (result.hasErrors()) {
                redirectAttrs.addFlashAttribute("error", "Revisa los campos del formulario.");
                return "redirect:/objects/perfume";
            }

            if (perfume.getBrand() == null || perfume.getBrand().getId() == null) {
                redirectAttrs.addFlashAttribute("error", "No se puede crear un perfume sin una marca.");
                return "redirect:/objects/perfume";
            }

            if (perfumeService.existsByName(perfume.getName())) {
                redirectAttrs.addFlashAttribute("error", "Ya existe un perfume con ese nombre.");
                return "redirect:/objects/perfume";
            }
            perfumeService.save(perfume);

            return "redirect:/objects/perfume";

        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/objects/perfume";
        }
    }

    @DeleteMapping("/borrar/{id}")
    public String borrarPerfume(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        if (!permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writePerfume)) {
            redirectAttrs.addFlashAttribute("error", "No tienes permisos para borrar perfumes.");
            return "redirect:/objects/perfume";
        }

        if (!perfumeService.existsById(id)) {
            redirectAttrs.addFlashAttribute("error", "El perfume no existe.");
            return "redirect:/objects/perfume";
        }

        perfumeLockManager.releaseLock(id, userContextService.getUsername());

        perfumeService.deleteById(id);

        session.setAttribute("selectedPerfume", null);

        session.setAttribute("isLockedByAnother", null);

        return "redirect:/objects/perfume";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {

        if (permisosUtils.contieneAlgunSubmodulo(userContextService.getSubmodules(), writePerfume)) {

            perfumeLockManager.releaseLock(id, userContextService.getUsername());

        }

        session.setAttribute("selectedPerfume", null);

        session.setAttribute("isLockedByAnother", null);

        return "redirect:/objects/perfume";
    }

    @PostMapping(value = "/refreshLock/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> refreshLock(@PathVariable Long id, HttpSession session) {

        perfumeLockManager.refreshLock(id, userContextService.getUsername());
        return ResponseEntity.ok().build();
    }
}