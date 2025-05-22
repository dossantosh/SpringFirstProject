package com.example.springfirstproject.service.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.objects.Perfumes;
import com.example.springfirstproject.repositories.objects.PerfumeRepository;

import jakarta.persistence.EntityManager;
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

    @PersistenceContext
    private EntityManager em;

    public Perfumes save(Perfumes perfumes) {
        return perfumeRepository.save(perfumes);
    }

    public Optional<Perfumes> findById(Long id) {
        return perfumeRepository.findById(id);
    }

    public Optional<Perfumes> findByName(String name) {
        return perfumeRepository.findByName(name);
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
}
