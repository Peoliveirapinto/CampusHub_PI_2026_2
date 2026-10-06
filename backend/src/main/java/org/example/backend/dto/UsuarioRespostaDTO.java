package org.example.backend.dto;

import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;

import java.util.UUID;

public record UsuarioRespostaDTO(
        UUID id,
        String email,
        Role role
) {
    public UsuarioRespostaDTO(Usuario usuario){
        this(usuario.getId(), usuario.getEmail(), usuario.getRole());
    }
}
