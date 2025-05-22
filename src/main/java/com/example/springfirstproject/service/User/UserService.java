package com.example.springfirstproject.service.user;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.user.User;
import com.example.springfirstproject.repositories.user.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public LinkedHashSet<User> findAll() {
        return new LinkedHashSet<>(userRepository.findAll());
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @PersistenceContext
    private EntityManager em;

    public Page<User> findByFilters(Long id, String username, String email, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // --- Construcción de la consulta principal (para resultados paginados) ---
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(userRoot.get("id"), id));
        }
        if (username != null && !username.isBlank()) {
            predicates.add(cb.like(cb.lower(userRoot.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            predicates.add(cb.like(cb.lower(userRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(userRoot.get("id")));

        TypedQuery<User> query = em.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<User> resultList = query.getResultList();

        // --- Construcción de la consulta de conteo (para total de registros) ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        List<Predicate> countPredicates = new ArrayList<>();
        if (id != null) {
            countPredicates.add(cb.equal(countRoot.get("id"), id));
        }
        if (username != null && !username.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        // --- Construcción del Page ---
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(resultList, pageable, total);
    }
}
