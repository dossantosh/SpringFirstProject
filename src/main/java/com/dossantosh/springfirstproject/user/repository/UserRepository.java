package com.dossantosh.springfirstproject.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserAuthProjection;
import com.dossantosh.springfirstproject.user.models.User;

import com.dossantosh.springfirstproject.user.utils.projections.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsById(@NonNull Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Log in
    @Query(value = """
            SELECT
                u.id_user AS id,
                u.username AS username,
                u.password AS password,
                u.enabled AS enabled,
                (
                    SELECT array_agg(r.name)
                    FROM roles r
                    JOIN users_roles ur ON r.id_role = ur.id_role
                    WHERE ur.id_user = u.id_user
                ) AS roles,
                (
                    SELECT array_agg(m.id_module)
                    FROM modules m
                    JOIN users_modules um ON m.id_module = um.id_module
                    WHERE um.id_user = u.id_user
                ) AS modules,
                (
                    SELECT array_agg(s.id_submodule)
                    FROM submodules s
                    JOIN users_submodules us ON s.id_submodule = us.id_submodule
                    WHERE us.id_user = u.id_user
                ) AS submodules,
                p.user_id AS preferences_userId,
                p.email_notifications AS preferences_emailNotifications,
                p.sms_notifications AS preferences_smsNotifications,
                p.tema AS preferences_tema,
                p.idioma AS preferences_idioma
            FROM users u
            LEFT JOIN preferences p ON p.user_id = u.id_user
            WHERE u.username = :username
                                """, nativeQuery = true)
    Optional<UserAuthProjection> findUserAuthByUsername(String username);

    // View
    @Query(value = """
                SELECT u.id_user AS id,
                       u.username AS username,
                       u.email AS email,
                       u.enabled AS enabled,
                       EXISTS (
                           SELECT 1
                           FROM users_roles ur
                           WHERE ur.id_user = u.id_user
                             AND ur.id_role = 2
                       ) AS isAdmin
                FROM users u
                WHERE (:id IS NULL OR u.id_user = :id)
                  AND (:username IS NULL OR LOWER(u.username) LIKE CONCAT(:username, '%'))
                  AND (:email IS NULL OR LOWER(u.email) LIKE CONCAT(:email, '%'))
                  AND (
                      (:direction = 'NEXT' AND (:lastId IS NULL OR u.id_user > :lastId))
                      OR
                      (:direction = 'PREVIOUS' AND (:lastId IS NULL OR u.id_user < :lastId))
                  )
                ORDER BY
                  CASE WHEN :direction = 'NEXT' THEN u.id_user END ASC,
                  CASE WHEN :direction = 'PREVIOUS' THEN u.id_user END DESC
                LIMIT :limit
            """, nativeQuery = true)
    List<UserDTO> findByFiltersKeysetWithDirection(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("email") String email,
            @Param("lastId") Long lastId,
            @Param("limit") int limit,
            @Param("direction") String direction);

    @EntityGraph(attributePaths = { "roles", "modules", "submodules"})
    Optional<User> findFullUserById(Long id);
}