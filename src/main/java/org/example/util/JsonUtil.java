package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public final class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static void sendJsonResponse(HttpServletResponse response, int status, Object data) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), data);
    }

    public static void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), Map.of("status", status, "message", message));
    }

    public static void sendValidationErrorResponse(HttpServletResponse resp, int status, Map<String, String> errors) throws IOException {
        String combinedValidationMessage = errors.values().stream()
                .collect(Collectors.joining("; "));

        Map<String, Object> errorBody = Map.of(
                "status", status,
                "message", combinedValidationMessage
        );

        sendJsonResponse(resp, status, errorBody);
    }
}
