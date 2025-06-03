package com.dossantosh.springfirstproject.common.security.custom.login;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SessionInvalidationService {

    private final JdbcTemplate jdbcTemplate;

    public SessionInvalidationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void invalidateSessionsByUsername(String username) {
        // Obtener SESSION_ID y PRIMARY_ID juntos
        String sql = "SELECT SESSION_ID, PRIMARY_ID FROM SPRING_SESSION WHERE PRINCIPAL_NAME = ?";

        // Obtener lista de pares (SESSION_ID, PRIMARY_ID)
        List<Map<String, Object>> sessions = jdbcTemplate.queryForList(sql, username);

        for (Map<String, Object> session : sessions) {
            String sessionId = (String) session.get("SESSION_ID");
            String primaryId = (String) session.get("PRIMARY_ID");

            // Borrar atributos de la sesión usando PRIMARY_ID
            jdbcTemplate.update("DELETE FROM SPRING_SESSION_ATTRIBUTES WHERE SESSION_PRIMARY_ID = ?", primaryId);

            // Borrar la sesión principal
            jdbcTemplate.update("DELETE FROM SPRING_SESSION WHERE SESSION_ID = ?", sessionId);
        }
    }
}