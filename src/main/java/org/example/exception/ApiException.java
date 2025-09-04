package org.example.exception;

import java.util.Map;

public abstract class ApiException extends RuntimeException {
    private final int status;
    private final String code;
    private final Map<String, ?> details;

    protected ApiException(int status, String code, String message) {
        this(status, code, message, null, null);
    }

    protected ApiException(int status, String code, String message, Throwable cause) {
        this(status, code, message, null, cause);
    }

    protected ApiException(int status, String code, String message,
                           Map<String, ?> details, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.details = details;
    }

    public int getStatus() { return status; }
    public String getCode() { return code; }
    public Map<String, ?> getDetails() { return details; }
}
