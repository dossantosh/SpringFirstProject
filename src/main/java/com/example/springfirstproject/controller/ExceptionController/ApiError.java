package com.example.springfirstproject.controller.ExceptionController;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Map;
/*
 * Objeto que se enviara cuando se comenta un error dentro de un RestController
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> details;

    public ApiError(HttpStatus status, String message, Map<String, String> details) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    
}