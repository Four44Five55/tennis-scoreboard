package org.example.validation;

import org.example.exception.ValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Errors {
    private final Map<String, String> errors = new LinkedHashMap<>();

    public void add(String field, String message) {
        if (message != null && !message.isBlank()) errors.put(field, message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void throwIfAny() {
        if (!errors.isEmpty()) {
            throw new ValidationException("Ошибка валидации", Map.copyOf(errors));
        }
    }
}