package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginDTO(
        @NotBlank(message = "{usuario.email.obrigatorio}")
        String email,

        @NotBlank(message = "{usuario.senha.obrigatoria}")
        String senha
) {}
