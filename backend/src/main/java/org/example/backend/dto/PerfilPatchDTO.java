package org.example.backend.dto;

public record PerfilPatchDTO(
        String nome,
        String descricao,
        String curso,
        String especializacao,
        Integer anoDeIngresso,
        Integer semestreDeIngresso,
        String disponibilidade,
        String contato
) {}
