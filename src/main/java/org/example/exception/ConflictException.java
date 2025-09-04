package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

//409
public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(HttpServletResponse.SC_CONFLICT, "conflict", message);
    }
}
