package org.example.backend.exception;

public class JaExisteException extends RuntimeException {
    public JaExisteException(String messageKey) {
        super(messageKey);
    }
}
