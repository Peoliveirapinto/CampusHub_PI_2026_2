package org.example.backend.exception;

public class TokenPayloadInvalidoException extends RuntimeException {
    public TokenPayloadInvalidoException(String message) {
        super(message);
    }
}
