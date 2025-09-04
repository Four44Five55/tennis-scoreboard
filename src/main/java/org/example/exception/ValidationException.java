package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Map;

//400
public class ValidationException extends ApiException {
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(HttpServletResponse.SC_BAD_REQUEST, "validation_error", message, fieldErrors, null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getFieldErrors() {
        var d = getDetails();
        return d == null ? Collections.emptyMap() : (Map<String, String>) d;
    }
}
