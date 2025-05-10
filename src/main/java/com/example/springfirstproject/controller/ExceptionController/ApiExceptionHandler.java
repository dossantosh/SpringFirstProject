package com.example.springfirstproject.controller.ExceptionController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice(annotations = org.springframework.web.bind.annotation.RestController.class)
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                                : "Error de validación"));

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos de entrada",
                errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(fieldName, message);
        });

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error de validación en los datos",
                errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN,
                "Acceso denegado: No tienes permisos suficientes",
                null);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Error de integridad de datos";
        Map<String, String> details = new HashMap<>();
    
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            org.hibernate.exception.ConstraintViolationException constraintEx = 
                (org.hibernate.exception.ConstraintViolationException) ex.getCause();
            
            String constraintName = constraintEx.getConstraintName();
            details.put("constraint", constraintName);
            
            // Extraer información del mensaje de error
            String errorMsg = constraintEx.getSQLException().getMessage();
            extractTableAndColumnInfo(errorMsg, details);
            
            // Mapeo de constraints conocidos
            if (constraintName != null) {
                if (constraintName.startsWith("UK_") || constraintName.startsWith("IX_") || "23505".equals(constraintEx.getSQLState())) {
                    errorMessage = "Violación de unicidad: El valor ya existe";
                    details.put("type", "DUPLICATE_ENTRY");
                } else if (constraintName.startsWith("FK_") || "23503".equals(constraintEx.getSQLState())) {
                    errorMessage = "Violación de clave foránea: Referencia inválida";
                    details.put("type", "FOREIGN_KEY_VIOLATION");
                }
            }
        } 
        else if (ex.getCause() instanceof SQLException) {
            SQLException sqlEx = (SQLException) ex.getCause();
            details.put("sqlState", sqlEx.getSQLState());
            
            // Extraer información del mensaje de error
            extractTableAndColumnInfo(sqlEx.getMessage(), details);
            
            if ("23505".equals(sqlEx.getSQLState())) {
                errorMessage = "Violación de unicidad: El valor ya existe";
                details.put("type", "DUPLICATE_ENTRY");
            } else if ("23503".equals(sqlEx.getSQLState())) {
                errorMessage = "Violación de clave foránea: Referencia inválida";
                details.put("type", "FOREIGN_KEY_VIOLATION");
            }
        }
    
        // Personalización para constraint específico
        if ("ix709_4".equalsIgnoreCase(details.get("constraint"))) {
            errorMessage = "El valor para '" + details.getOrDefault("column", "el campo") + "' ya existe";
        }
    
        ApiError error = new ApiError(
            HttpStatus.CONFLICT,
            errorMessage,
            details
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    private void extractTableAndColumnInfo(String errorMsg, Map<String, String> details) {
        if (errorMsg == null) return;
        
        // Patrones comunes en diferentes bases de datos
        // PostgreSQL
        if (errorMsg.contains("violates unique constraint")) {
            // Ejemplo: "duplicate key value violates unique constraint "ix709_4" Detail: Key (email)=(test@test.com) already exists."
            details.put("constraintType", "UNIQUE");
            
            // Extraer columna
            int keyStart = errorMsg.indexOf("Key (");
            if (keyStart > 0) {
                int keyEnd = errorMsg.indexOf(")", keyStart);
                if (keyEnd > 0) {
                    String column = errorMsg.substring(keyStart + 5, keyEnd);
                    details.put("column", column);
                }
            }
        }
        else if (errorMsg.contains("violates foreign key constraint")) {
            // Ejemplo: "insert or update on table "orders" violates foreign key constraint "fk_customer" Detail: Key (customer_id)=(123) is not present in table "customers"."
            details.put("constraintType", "FOREIGN_KEY");
            
            // Extraer tabla y columna
            int tableStart = errorMsg.indexOf("table \"");
            if (tableStart > 0) {
                int tableEnd = errorMsg.indexOf("\"", tableStart + 7);
                if (tableEnd > 0) {
                    String table = errorMsg.substring(tableStart + 7, tableEnd);
                    details.put("table", table);
                }
            }
            
            int keyStart = errorMsg.indexOf("Key (");
            if (keyStart > 0) {
                int keyEnd = errorMsg.indexOf(")", keyStart);
                if (keyEnd > 0) {
                    String column = errorMsg.substring(keyStart + 5, keyEnd);
                    details.put("column", column);
                }
            }
        }
        
        // MySQL
        else if (errorMsg.contains("Duplicate entry") && errorMsg.contains("for key")) {
            // Ejemplo: "Duplicate entry 'test@test.com' for key 'users.ix709_4'"
            details.put("constraintType", "UNIQUE");
            
            String[] parts = errorMsg.split("'");
            if (parts.length >= 2) {
                details.put("duplicateValue", parts[1]);
            }
            
            int keyStart = errorMsg.indexOf("for key '");
            if (keyStart > 0) {
                int keyEnd = errorMsg.indexOf("'", keyStart + 9);
                if (keyEnd > 0) {
                    String key = errorMsg.substring(keyStart + 9, keyEnd);
                    String[] keyParts = key.split("\\.");
                    if (keyParts.length == 2) {
                        details.put("table", keyParts[0]);
                        details.put("constraint", keyParts[1]);
                    }
                }
            }
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                Map.of("error", ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}