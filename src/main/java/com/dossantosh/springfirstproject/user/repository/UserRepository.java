package com.dossantosh.springfirstproject.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuthProjection;
import com.dossantosh.springfirstproject.user.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

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
                    ) AS submodules
                FROM users u
                WHERE u.username = :username
            """, nativeQuery = true)
    UserAuthProjection findUserAuthByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsById(@NonNull Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}