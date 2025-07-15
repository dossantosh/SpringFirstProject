package com.dossantosh.springfirstproject.perfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.utils.PerfumeProjection;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes, Long>, JpaSpecificationExecutor<Perfumes> {
  Optional<Perfumes> findByName(String name);

  boolean existsById(Long id);

  boolean existsByName(String name);

  @Query(value = """
      SELECT p.id AS id,
             p.name AS name,
             b.name AS brandName,
             p.season AS season
      FROM perfumes p
      LEFT JOIN brands b ON p.brand_id = b.id
      WHERE (:id IS NULL OR p.id = :id)
        AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT(:name, '%')))
        AND (:brandName IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT(:brandName, '%')))
        AND (:season IS NULL OR LOWER(p.season) LIKE LOWER(CONCAT(:season, '%')))
        AND (
            (:direction = 'NEXT' AND (:lastId IS NULL OR p.id > :lastId))
            OR
            (:direction = 'PREVIOUS' AND (:lastId IS NULL OR p.id < :lastId))
        )
      ORDER BY
        CASE WHEN :direction = 'NEXT' THEN p.id END ASC,
        CASE WHEN :direction = 'PREVIOUS' THEN p.id END DESC
      LIMIT :limit
      """, nativeQuery = true)
  List<PerfumeProjection> findByFiltersKeysetWithDirection(
      @Param("id") Long id,
      @Param("name") String name,
      @Param("brandName") String brandName,
      @Param("season") String season,
      @Param("lastId") Long lastId,
      @Param("limit") int limit,
      @Param("direction") String direction);
}
