package org.example.backend.dto.pareamento;

import org.example.backend.dto.perfil.PerfilRespostaDTO;

public record PareamentoRespostaDTO(
        PerfilRespostaDTO perfil,
        int pontuacaoMatch
) {}
