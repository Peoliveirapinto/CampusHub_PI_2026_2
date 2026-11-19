package org.example.backend.dto.perfil;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerfilCriarMeuDTO(
        @NotBlank(message = "{perfil.nome.obrigatorio}")
        String nome,
        @NotBlank(message = "{perfil.descricao.obrigatoria}")
        String descricao,
        @NotBlank(message = "{perfil.curso.obrigatorio}")
        String curso,
        String especializacao,
        @NotNull(message = "{perfil.ano-de-ingresso.obrigatorio}")
        @Min(value = 1, message = "{perfil.ano-de-ingresso.invalido}")
        Integer anoDeIngresso,
        @NotNull(message = "{perfil.semestre-de-ingresso.obrigatorio}")
        @Min(value = 1, message = "{perfil.semestre-de-ingresso.invalido}")
        @Max(value = 2, message = "{perfil.semestre-de-ingresso.invalido}")
        Integer semestreDeIngresso,
        @NotBlank(message = "{perfil.disponibilidade.obrigatoria}")
        String disponibilidade,
        @NotBlank(message = "{perfil.contato.obrigatorio}")
        String contato,
        @NotBlank(message = "{perfil.areas-de-interesse.obrigatorias}")
        String areasDeInteresse
) {}
