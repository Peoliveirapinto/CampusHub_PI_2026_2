package org.example.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.backend.validation.SenhaValida;

public record UsuarioCadastroDTO(
        @NotBlank(message = "{usuario.email.obrigatorio}")
        @Email(message = "{usuario.email.invalido}")
        String email,

        @NotBlank(message = "{usuario.senha.obrigatoria}")
        @SenhaValida
        String senha
) {}
