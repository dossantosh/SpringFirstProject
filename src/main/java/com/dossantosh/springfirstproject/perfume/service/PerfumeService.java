package com.dossantosh.springfirstproject.perfume.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.perfume.dto.PerfumeDTO;
import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.repository.PerfumeRepository;
import com.dossantosh.springfirstproject.perfume.utils.PerfumeSpecifications;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    private final BrandService brandService;

    private final TypesService tipoService;

    @PersistenceContext
    private EntityManager em;

    public Perfumes save(Perfumes perfumes) {
        return perfumeRepository.save(perfumes);
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

    public long count() {
        return perfumeRepository.count();
    }

    public Page<Perfumes> findByFilters(Long id, String name, String season, String brandName, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Perfumes> cq = cb.createQuery(Perfumes.class);
        Root<Perfumes> perfume = cq.from(Perfumes.class);
        Join<Object, Object> brand = perfume.join("brand", JoinType.LEFT);

        Predicate predicate = cb.conjunction(); // TRUE por defecto

        if (id != null) {
            predicate = cb.and(predicate, cb.equal(perfume.get("id"), id));
        }
        if (name != null && !name.isBlank()) {
            predicate = cb.and(predicate, cb.like(cb.lower(perfume.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (season != null && !season.isBlank()) {
            predicate = cb.and(predicate, cb.equal(cb.lower(perfume.get("season")), season.toLowerCase()));
        }
        if (brandName != null && !brandName.isBlank()) {
            predicate = cb.and(predicate, cb.like(cb.lower(brand.get("name")), "%" + brandName.toLowerCase() + "%"));
        }

        cq.where(predicate);
        cq.orderBy(cb.asc(perfume.get("id")));

        TypedQuery<Perfumes> query = em.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        // Para el total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Perfumes> countRoot = countQuery.from(Perfumes.class);
        Join<Object, Object> countBrand = countRoot.join("brand", JoinType.LEFT);
        countQuery.select(cb.count(countRoot));
        List<Predicate> countPredicates = new ArrayList<>();

        if (id != null)
            countPredicates.add(cb.equal(countRoot.get("id"), id));
        if (name != null && !name.isBlank())
            countPredicates.add(cb.like(cb.lower(countRoot.get("name")), "%" + name.toLowerCase() + "%"));
        if (season != null && !season.isBlank())
            countPredicates.add(cb.equal(cb.lower(countRoot.get("season")), season.toLowerCase()));
        if (brandName != null && !brandName.isBlank())
            countPredicates.add(cb.like(cb.lower(countBrand.get("name")), "%" + brandName.toLowerCase() + "%"));

        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        List<Perfumes> resultList = query.getResultList();
        Pageable pageable = PageRequest.of(page, size);

        return new PageImpl<>(resultList, pageable, total);
    }

    public Page<Perfumes> findPerfumeByFilters(Long id, String name, String season, String brandName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
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
