package com.dossantosh.springfirstproject.perfume.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PerfumeSpecifications {

    public static Specification<Perfumes> filter(Long id, String name, String season, String brandName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%"));
            }
            if (season != null && !season.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("season")), "%" + season.toLowerCase() + "%"));
            }
            if (brandName != null && !brandName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("brandName")), "%" + brandName.toLowerCase() + "%"));
            }

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
