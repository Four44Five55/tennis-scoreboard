package org.example.exception;

import jakarta.servlet.http.HttpServletResponse;

//500
public final class DataAccessException extends ApiException {
    public DataAccessException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "db_error", message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "db_error", message, cause);
    }
}
