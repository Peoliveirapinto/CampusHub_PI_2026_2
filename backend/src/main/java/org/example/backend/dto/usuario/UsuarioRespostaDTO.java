package org.example.backend.dto.usuario;

import org.example.backend.model.enums.Role;

import java.util.UUID;

public record UsuarioRespostaDTO(
        UUID id,
        String email,
        Role role
) {}
