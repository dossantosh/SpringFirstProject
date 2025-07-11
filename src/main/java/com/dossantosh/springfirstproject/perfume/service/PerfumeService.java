package com.dossantosh.springfirstproject.perfume.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dossantosh.springfirstproject.common.global.events.Audit.AuditService;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;
import com.dossantosh.springfirstproject.perfume.models.Brands;
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

    private final UserContextService userContextService;

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

    public List<Perfumes> findAll() {
        return new ArrayList<>(perfumeRepository.findAll());
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
                userContextService.getUsername(),
                "PERFUME_CREATE",
                Map.of("id", newPerfume.getId() != null ? newPerfume.getId() : "Sin ID",
                        "name", newPerfume.getName() != null ? newPerfume.getName() : "Sin nombre",
                        "season", newPerfume.getSeason() != null ? newPerfume.getSeason() : "Sin temporada",
                        "description",
                        newPerfume.getDescription() != null ? newPerfume.getDescription() : "Sin descripción",
                        "fecha", newPerfume.getFecha() != null ? newPerfume.getFecha().toString() : "Sin fecha",
                        "tipo",
                        newPerfume.getTipo() != null && newPerfume.getTipo().getName() != null
                                ? newPerfume.getTipo().getName()
                                : "Sin tipo",
                        "brandName", newPerfume.getBrand() != null ? newPerfume.getBrand().getName() : "Sin marca"));
    }

    public void modifyPerfumes(Perfumes updatedPerfume) {

        save(updatedPerfume);

        if (userContextService.getUsername().isBlank() || userContextService.getUsername() == null
                || userContextService.getUsername().isEmpty()) {
            throw new IllegalStateException("DIOOOOOOOOOOOOOOOOOOOOOOOOOOS");
        }

        auditService.logCustomEvent(
                userContextService.getUsername(),
                "PERFUME_MODIFY",
                Map.of("id", updatedPerfume.getId() != null ? updatedPerfume.getId() : "Sin ID",
                        "name", updatedPerfume.getName() != null ? updatedPerfume.getName() : "Sin nombre",
                        "season", updatedPerfume.getSeason() != null ? updatedPerfume.getSeason() : "Sin temporada",
                        "description",
                        updatedPerfume.getDescription() != null ? updatedPerfume.getDescription() : "Sin descripción",
                        "fecha", updatedPerfume.getFecha() != null ? updatedPerfume.getFecha().toString() : "Sin fecha",
                        "tipo",
                        updatedPerfume.getTipo().getName() != null ? updatedPerfume.getTipo().getName() : "Sin tipo",
                        "brandName",
                        updatedPerfume.getBrand() != null ? updatedPerfume.getBrand().getName() : "Sin marca"));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("Perfume con ID " + id + " no encontrado");
        }
        Perfumes perfume = findById(id);

        auditService.logCustomEvent(
                userContextService.getUsername(),
                "PERFUME_DELETE",
                Map.of("id", perfume.getId() != null ? perfume.getId() : "Sin ID",
                        "name", perfume.getName() != null ? perfume.getName() : "Sin nombre",
                        "season", perfume.getSeason() != null ? perfume.getSeason() : "Sin temporada",
                        "description", perfume.getDescription() != null ? perfume.getDescription() : "Sin descripción",
                        "fecha", perfume.getFecha() != null ? perfume.getFecha().toString() : "Sin fecha",
                        "tipo", perfume.getTipo().getName() != null ? perfume.getTipo().getName() : "Sin tipo",
                        "brandName", perfume.getBrand() != null ? perfume.getBrand().getName() : "Sin marca"));

        // Quitar el perfume de la lista de la marca para limpiar la relación
        Brands brand = perfume.getBrand();
        if (brand != null) {
            brand.getPerfumes().remove(perfume);
            perfume.setBrand(null);
        }
        
        perfumeRepository.delete(perfume);
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

    public List<PerfumeDTO> conversorPerfumeDTO(Collection<Perfumes> perfumes) {
        return perfumes.stream().map(p -> {
            PerfumeDTO dto = new PerfumeDTO();
            dto.setId(p.getId() != null ? p.getId() : -1L);
            dto.setName(p.getName() != null ? p.getName() : "Sin nombre");
            dto.setSeason(p.getSeason() != null ? p.getSeason() : "Sin temporada");
            dto.setDescription(p.getDescription() != null ? p.getDescription() : "Sin descripción");
            dto.setFecha(p.getFecha());
            dto.setTipo(p.getTipo().getName() != null ? p.getTipo().getName() : "Sin tipo");
            dto.setBrandName(p.getBrand() != null ? p.getBrand().getName() : "Sin marca");
            return dto;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Map<String, List<Object>> cargarListaInfo() {
        Map<String, List<Object>> map = new HashMap<>();
        map.put("brands", new ArrayList<>(brandService.findAll()));
        map.put("tipos", new ArrayList<>(tipoService.findAll()));
        return map;
    }
}
