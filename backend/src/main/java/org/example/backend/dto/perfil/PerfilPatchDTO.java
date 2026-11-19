package org.example.backend.dto.perfil;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PerfilPatchDTO(
        String nome,
        String descricao,
        String curso,
        String especializacao,
        @Min(value = 1, message = "{perfil.ano-de-ingresso.invalido}")
        Integer anoDeIngresso,
        @Min(value = 1, message = "{perfil.semestre-de-ingresso.invalido}")
        @Max(value = 2, message = "{perfil.semestre-de-ingresso.invalido}")
        Integer semestreDeIngresso,
        String disponibilidade,
        String contato,
        String areasDeInteresse
) {}
