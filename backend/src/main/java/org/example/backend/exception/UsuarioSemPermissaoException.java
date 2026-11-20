package org.example.backend.exception;

public class UsuarioSemPermissaoException extends RuntimeException {
    public UsuarioSemPermissaoException(String message) {
        super(message);
    }
}
