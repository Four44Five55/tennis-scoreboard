package org.example.exception;

public class DuplicateEntityException extends EntityException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
