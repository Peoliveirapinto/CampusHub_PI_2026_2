package org.example.backend.dto.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PerfilCriarDTO(
        @NotNull
        UUID usuarioId,
        @NotBlank
        String nome,
        @NotBlank
        String descricao,
        @NotBlank
        String curso,
        String especializacao,
        @NotNull
        Integer anoDeIngresso,
        @NotNull
        Integer semestreDeIngresso,
        @NotBlank
        String disponibilidade,
        @NotBlank
        String contato
) {}
