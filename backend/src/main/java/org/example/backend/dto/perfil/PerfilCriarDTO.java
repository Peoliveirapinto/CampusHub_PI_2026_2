package org.example.backend.dto.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PerfilCriarDTO(
        @NotNull(message = "{perfil.usuario-id.obrigatorio}")
        UUID usuarioId,
        @NotBlank(message = "{perfil.nome.obrigatorio}")
        String nome,
        @NotBlank(message = "{perfil.descricao.obrigatoria}")
        String descricao,
        @NotBlank(message = "{perfil.curso.obrigatorio}")
        String curso,
        String especializacao,
        @NotNull(message = "{perfil.ano-de-ingresso.obrigatorio}")
        Integer anoDeIngresso,
        @NotNull(message = "{perfil.semestre-de-ingresso.obrigatorio}")
        Integer semestreDeIngresso,
        @NotBlank(message = "{perfil.disponibilidade.obrigatoria}")
        String disponibilidade,
        @NotBlank(message = "{perfil.contato.obrigatorio}")
        String contato,
        @NotBlank(message = "{perfil.areas-de-interesse.obrigatorias}")
        String areasDeInteresse
) {}
