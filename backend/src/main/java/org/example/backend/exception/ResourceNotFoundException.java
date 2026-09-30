package org.example.backend.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Object[] args;

    public ResourceNotFoundException(String messageKey, Object... args) {
        super(messageKey); // messageKey é a chave (ex: "usuario.nao-encontrado")
        this.args = args;
    }
}
