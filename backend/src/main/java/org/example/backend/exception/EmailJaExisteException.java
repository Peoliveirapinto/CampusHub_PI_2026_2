package org.example.backend.exception;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String messageKey) {
        super(messageKey);
    }
}
