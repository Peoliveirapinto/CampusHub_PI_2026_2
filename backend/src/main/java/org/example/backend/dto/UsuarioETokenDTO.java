package org.example.backend.dto;

import org.example.backend.model.Usuario;

public record UsuarioETokenDTO(
        Usuario usuario,
        String token
) {
}
