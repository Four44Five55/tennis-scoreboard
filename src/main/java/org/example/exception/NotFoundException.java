package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

//404
public final class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, "not_found", message);
    }

    public NotFoundException(String resource, String key, Object value) {
        super(HttpServletResponse.SC_NOT_FOUND, "not_found",
                String.format("%s with %s=%s not found", capitalize(resource), key, value),
                Map.of("resource", resource, key, String.valueOf(value)), null);
    }

    private static String capitalize(String s) {
        return (s == null || s.isEmpty()) ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}