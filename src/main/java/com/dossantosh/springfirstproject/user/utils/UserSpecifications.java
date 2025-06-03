package com.dossantosh.springfirstproject.user.utils;

import org.springframework.data.jpa.domain.Specification;

import com.dossantosh.springfirstproject.user.models.User;

import jakarta.persistence.criteria.Predicate;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserSpecifications {

    public static Specification<User> filter(Long id, String username, String email) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (username != null && !username.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.trim().toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}