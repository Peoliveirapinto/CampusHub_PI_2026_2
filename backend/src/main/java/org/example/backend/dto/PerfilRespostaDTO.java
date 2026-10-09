package org.example.backend.dto;

import org.example.backend.model.Perfil;

import java.util.UUID;

public record PerfilRespostaDTO(
        UUID id,
        UsuarioRespostaDTO usuario,
        String nome,
        String descricao,
        String curso,
        String especializacao,
        int anoDeIngresso,
        int semestreDeIngresso,
        String disponibilidade,
        String contato
) {
    public PerfilRespostaDTO(Perfil perfil){
        this(
            perfil.getId(),
            new UsuarioRespostaDTO(perfil.getUsuario()),
            perfil.getNome(),
            perfil.getDescricao(),
            perfil.getCurso(),
            perfil.getEspecializacao(),
            perfil.getAnoDeIngresso(),
            perfil.getSemestreDeIngresso(),
            perfil.getDisponibilidade(),
            perfil.getContato()
        );
    }
}
