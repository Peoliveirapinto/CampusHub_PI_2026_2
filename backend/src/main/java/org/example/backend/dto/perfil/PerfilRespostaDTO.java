package org.example.backend.dto.perfil;

import org.example.backend.dto.usuario.UsuarioRespostaDTO;
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
        String contato,
        String areasDeInteresse
) {}
