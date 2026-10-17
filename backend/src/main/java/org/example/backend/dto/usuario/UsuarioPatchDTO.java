package org.example.backend.dto.usuario;

import jakarta.validation.constraints.Email;
import org.example.backend.model.enums.Role;

public record UsuarioPatchDTO(
        @Email(message = "{usuario.email.invalido}")
        String email,
        Role role
) {}
