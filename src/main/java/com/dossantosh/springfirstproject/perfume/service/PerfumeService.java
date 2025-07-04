package com.dossantosh.springfirstproject.perfume.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.common.global.events.Audit.AuditService;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.repository.PerfumeRepository;
import com.dossantosh.springfirstproject.perfume.utils.PerfumeDTO;
import com.dossantosh.springfirstproject.perfume.utils.PerfumeSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    private final BrandService brandService;

    private final TypesService tipoService;

    private final AuditService auditService;

    @PersistenceContext
    private EntityManager em;

    public Perfumes save(Perfumes perfumes) {
        try {
            return perfumeRepository.save(perfumes);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException(
                    "El perfume fue modificado por otro usuario. Por favor, recarga la página.");
        }
    }

    public Set<Perfumes> findAll() {
        return new LinkedHashSet<>(perfumeRepository.findAll());
    }

    public Perfumes findById(Long id) {
        return perfumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfume con ID " + id + " no encontrado"));
    }

    public Perfumes findByName(String name) {
        return perfumeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Perfume con nombre " + name + " no encontrado"));
    }

    public boolean existsById(Long id) {
        return perfumeRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return perfumeRepository.existsByName(name);
    }

    public void createPerfume(Perfumes newPerfume) {

        save(newPerfume);

        auditService.logCustomEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "PERFUME_CREATE",
                Map.of("id", newPerfume.getId(),
                        "name", newPerfume.getName(),
                        "season", newPerfume.getSeason(),
                        "description", newPerfume.getDescription(),
                        "fecha", newPerfume.getFecha(),
                        "tipo", newPerfume.getTipo().getName(),
                        "brandName", newPerfume.getBrand() != null ? newPerfume.getBrand().getName() : "Sin marca"));
    }

    public void modifyPerfumes(Perfumes updatedPerfume) {

        save(updatedPerfume);

        auditService.logCustomEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "PERFUME_MODIFY",
                Map.of("id", updatedPerfume.getId(),
                        "name", updatedPerfume.getName(),
                        "season", updatedPerfume.getSeason(),
                        "description", updatedPerfume.getDescription(),
                        "fecha", updatedPerfume.getFecha(),
                        "tipo", updatedPerfume.getTipo().getName(),
                        "brandName", updatedPerfume.getBrand() != null ? updatedPerfume.getBrand().getName() : "Sin marca"));
    }

    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("Perfume con ID " + id + " no encontrado");
        }
        perfumeRepository.deleteById(id);

        Perfumes perfume = findById(id);
        auditService.logCustomEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "PERFUME_DELETE",
                Map.of("id", perfume.getId(),
                        "name", perfume.getName(),
                        "season", perfume.getSeason(),
                        "description", perfume.getDescription(),
                        "fecha", perfume.getFecha(),
                        "tipo", perfume.getTipo().getName(),
                        "brandName", perfume.getBrand() != null ? perfume.getBrand().getName() : "Sin marca"));
    }

    public long count() {
        return perfumeRepository.count();
    }

    public Page<Perfumes> findByFilters(Long id, String name, String season, String brandName, int page, int size,
            String sortBy, String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Specification<Perfumes> spec = PerfumeSpecifications.filter(id, name, season, brandName);

        return perfumeRepository.findAll(spec, pageable);
    }

    public Set<PerfumeDTO> conversorPerfumeDTO(Collection<Perfumes> perfumes) {
        return perfumes.stream().map(p -> {
            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setSeason(p.getSeason());
            dto.setDescription(p.getDescription());
            dto.setFecha(p.getFecha());
            dto.setTipo(p.getTipo().getName());
            dto.setBrandName(p.getBrand() != null ? p.getBrand().getName() : "Sin marca");
            return dto;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<String, Set<Object>> cargarListaInfo() {
        Map<String, Set<Object>> map = new HashMap<>();
        map.put("brands", new LinkedHashSet<>(brandService.findAll()));
        map.put("tipos", new LinkedHashSet<>(tipoService.findAll()));
        return map;
    }
}
