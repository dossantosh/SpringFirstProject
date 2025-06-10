package com.dossantosh.springfirstproject.common.security.custom.login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserService;

@Service
public class SessionService {

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    public SessionService(JdbcTemplate jdbcTemplate, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    public void invalidateSessionsById(Long id) {
        // Obtener SESSION_ID y PRIMARY_ID juntos
        String sql = "SELECT SESSION_ID, PRIMARY_ID FROM SPRING_SESSION WHERE PRINCIPAL_NAME = ?";

        // Obtener lista de pares (SESSION_ID, PRIMARY_ID)
        List<Map<String, Object>> sessions = jdbcTemplate.queryForList(sql, userService.findById(id).getUsername());

        for (Map<String, Object> session : sessions) {
            String sessionId = (String) session.get("SESSION_ID");
            String primaryId = (String) session.get("PRIMARY_ID");

            // Borrar atributos de la sesión usando PRIMARY_ID
            jdbcTemplate.update("DELETE FROM SPRING_SESSION_ATTRIBUTES WHERE SESSION_PRIMARY_ID = ?", primaryId);

            // Borrar la sesión principal
            jdbcTemplate.update("DELETE FROM SPRING_SESSION WHERE SESSION_ID = ?", sessionId);
        }
    }

    // Search for name
    public List<String> findPrimaryIdsByPrincipalName(String principalName) {
        String sql = "SELECT PRIMARY_ID FROM SPRING_SESSION WHERE PRINCIPAL_NAME = ?";
        return jdbcTemplate.queryForList(sql, String.class, principalName);
    }

    // add userAuth to an differente user (same as customauthsucceshandler but to
    // another user)
    public void addUserAuthAttributeToSession(String primaryId, String attributeName, UserAuth userAuth)
            throws IOException {
        // Serializar el objeto UserAuth a byte[]
        byte[] serializedUserAuth = serialize(userAuth);

        // Reemplazar en SPRING_SESSION_ATTRIBUTES
        String updateSql = "UPDATE SPRING_SESSION_ATTRIBUTES SET ATTRIBUTE_BYTES = ? WHERE SESSION_PRIMARY_ID = ? AND ATTRIBUTE_NAME = ?";
        int updated = jdbcTemplate.update(updateSql, serializedUserAuth, primaryId, attributeName);

        if (updated == 0) {
            String insertSql = "INSERT INTO SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID, ATTRIBUTE_NAME, ATTRIBUTE_BYTES) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, primaryId, attributeName, serializedUserAuth);
        }
    }

    public void updateSecurityContextForUser(UserAuth newUserAuth) throws IOException {
        String username = newUserAuth.getUsername();
        List<String> primaryIds = findPrimaryIdsByPrincipalName(username);

        if (primaryIds.isEmpty()) {
            System.out.println("No active sessions found for user: " + username);
            return;
        }

        // Crear el nuevo Authentication con los datos actualizados
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                newUserAuth,
                null,
                newUserAuth.getAuthorities());

        // Crear nuevo SecurityContext con el auth
        SecurityContext newContext = new SecurityContextImpl(newAuth);
        byte[] serializedContext = serialize(newContext);

        // Actualizar o insertar en cada sesión
        for (String primaryId : primaryIds) {
            int updated = jdbcTemplate.update(
                    "UPDATE SPRING_SESSION_ATTRIBUTES SET ATTRIBUTE_BYTES = ? WHERE SESSION_PRIMARY_ID = ? AND ATTRIBUTE_NAME = ?",
                    serializedContext, primaryId, "SPRING_SECURITY_CONTEXT");

            if (updated == 0) {
                jdbcTemplate.update(
                        "INSERT INTO SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID, ATTRIBUTE_NAME, ATTRIBUTE_BYTES) VALUES (?, ?, ?)",
                        primaryId, "SPRING_SECURITY_CONTEXT", serializedContext);
            }
        }
    }

    public void refreshAuthentication(UserAuth nuevoUserAuth) throws IOException {

        if(nuevoUserAuth == null){
            return;
        }
        
        Authentication authActual = SecurityContextHolder.getContext().getAuthentication();

        Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
                nuevoUserAuth,
                authActual.getCredentials(),
                authActual.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(nuevaAuth);

        updateSecurityContextForUser(nuevoUserAuth);
    }

    // "encode"
    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }
}