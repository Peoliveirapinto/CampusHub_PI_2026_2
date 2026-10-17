package org.example.backend.dto.auth;

import org.example.backend.model.Usuario;

public record UsuarioETokenDTO(
        Usuario usuario,
        String token
) {
}
