package com.example.springfirstproject.controller.objects;

import com.example.springfirstproject.config.Anotaciones.modulo.RequiereModulo;
import com.example.springfirstproject.models.objects.Brands;
import com.example.springfirstproject.models.objects.PerfumeView;
import com.example.springfirstproject.models.objects.Perfumes;
import com.example.springfirstproject.models.objects.Tipos;
import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.service.objects.BrandService;
import com.example.springfirstproject.service.objects.PerfumeService;
import com.example.springfirstproject.service.objects.TipoService;
import com.example.springfirstproject.service.user.UserAuthService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/perfumes")
@RequiredArgsConstructor
@RequiereModulo({ 2L })
public class PerfumeController {

    private final UserAuthService userAuthService;
    private final PerfumeService perfumeService;
    private final BrandService brandService;
    private final TipoService tipoService;

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
        model.addAttribute("modulosNecesarios", lista);
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

        LinkedHashSet<PerfumeView> perfumeViews = new LinkedHashSet<>(pageResult.getContent().stream().map(p -> {
            PerfumeView dto = new PerfumeView();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setSeason(p.getSeason());
            dto.setDescription(p.getDescription());
            dto.setFecha(p.getFecha());
            dto.setTipo(p.getTipo().getName());
            dto.setBrandName(p.getBrand() != null ? p.getBrand().getName() : "Sin marca");
            return dto;
        }).collect(Collectors.toCollection(LinkedHashSet::new)));

        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());

        LinkedHashSet<Brands> marcas = new LinkedHashSet<>(brandService.findAll());
        LinkedHashSet<Tipos> tipos = new LinkedHashSet<>(tipoService.findAll());

        Perfumes seleccionado = (Perfumes) session.getAttribute("selectedPerfume");

        model.addAttribute("perfumes", perfumeViews);
        model.addAttribute("brands", marcas);
        model.addAttribute("tipos", tipos);
        model.addAttribute("selectedPerfume", seleccionado);

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("name", name);
        filters.put("brand", brand);
        filters.put("season", season);
        model.addAttribute("filters", filters);

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
}
