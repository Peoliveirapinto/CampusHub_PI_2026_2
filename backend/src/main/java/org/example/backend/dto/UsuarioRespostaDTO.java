package org.example.backend.dto;

import org.example.backend.model.Usuario;

import java.util.UUID;

public record UsuarioRespostaDTO(
        UUID id,
        String email
) {
    public UsuarioRespostaDTO(Usuario usuario){
        this(usuario.getId(), usuario.getEmail());
    }
}
