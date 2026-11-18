package org.example.backend.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import org.example.backend.validation.SenhaValida;

public record AlterarSenhaDTO(
       @NotBlank(message = "{usuario.senha-atual.obrigatoria}")
       String senhaAtual,
       @NotBlank(message = "{usuario.senha-nova.obrigatoria}")
       @SenhaValida
       String novaSenha,
       @NotBlank(message = "{usuario.senha-nova-confirmacao.obrigatoria}")
       @SenhaValida
       String confirmacaoNovaSenha
) {}
