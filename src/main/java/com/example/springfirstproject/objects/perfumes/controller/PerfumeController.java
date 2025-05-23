package com.example.springfirstproject.objects.perfumes.controller;

import com.example.springfirstproject.common.config.anotaciones.module.Requieremodule;
import com.example.springfirstproject.objects.brands.service.BrandService;
import com.example.springfirstproject.objects.perfumes.models.Perfumes;
import com.example.springfirstproject.objects.perfumes.service.PerfumeService;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/perfumes")
@RequiredArgsConstructor
@Requieremodule({ 2L })
public class PerfumeController {

    private final UserAuthService userAuthService;
    private final PerfumeService perfumeService;
    private final BrandService brandService;

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

        addCommonAttributes(model);

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
        Page<Perfumes> pageResult = perfumeService.findByFilters(id, name, season, brand, page, size);

        model.addAttribute("perfumes", perfumeService.conversorPerfumeDTO(pageResult.getContent()));

        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());
        model.addAttribute("selectedPerfume", session.getAttribute("selectedPerfume"));
        //model.addAttribute("filters", Map.of("id", id, "name", name, "brand", brand, "season", season));

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("name", name);
        filters.put("brand", brand);
        filters.put("season", season);
        model.addAttribute("filters", filters);

        // model.addAllAttributes(perfumeService.cargarListaInfo());
        perfumeService.cargarListaInfo().forEach(model::addAttribute);
        return "perfumes";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarPerfume(@PathVariable Long id, HttpSession session) {

        perfumeService.findById(id).ifPresent(perfume -> session.setAttribute("selectedPerfume", perfume));
        return "redirect:/perfumes";
    }

    @PostMapping("/guardar")
    public String guardarPerfume(@ModelAttribute Perfumes perfume, HttpSession session) {
        if (perfume.getBrand() != null && perfume.getBrand().getId() != null) {
            Long brandId = perfume.getBrand().getId();
            brandService.findById(brandId).ifPresent(perfume::setBrand);
        }
        perfumeService.save(perfume);

        // Limpiar el seleccionado en sesión para que no reaparezca en el formulario
        session.removeAttribute("selectedPerfume");

        return "redirect:/perfumes";
    }

    @GetMapping("/cancelar")
    public String cancelar(@ModelAttribute Perfumes perfume, HttpSession session) {

        // Limpiar el seleccionado en sesión para que no reaparezca en el formulario
        session.removeAttribute("selectedPerfume");

        return "redirect:/perfumes";
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());

        if (auth.getName().equals("anonymousUser")) {
            return;
        }

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return;
        }
        model.addAttribute("userAuth", userAuth.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);
    }
}
