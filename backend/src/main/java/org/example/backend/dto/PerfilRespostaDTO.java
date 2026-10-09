package org.example.backend.dto;

import org.example.backend.model.Perfil;

public record PerfilRespostaDTO(
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
