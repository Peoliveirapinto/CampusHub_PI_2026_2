package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SenhaService {
    private final PasswordEncoder passwordEncoder;

    public boolean ehValida(String senha){
        if(senha == null || senha.isBlank()){
            return false;
        }

        // Aqui a espaço para melhoria na validação da senha
        // Caso os requisitos para validação da senha sejam alterados, altere também a seguinte mensagem:
        // usuario.senha.invalida no arquivo backend/src/main/resources/messages.properties
        return senha.length() >= 8;
    }

    public String hash(String senhaOriginal){
        return passwordEncoder.encode(senhaOriginal);
    }

    public boolean matchesTheHash(String senhaLimpa, String senhaHasheada){
        return passwordEncoder.matches(senhaLimpa, senhaHasheada);
    }

    public void garantirQueASenhaEstaCerta(String senhaLimpa, String senhaHasheada){
        if(!this.matchesTheHash(senhaLimpa, senhaHasheada)){
            throw new CredenciaisInvalidasException("auth.credenciais-invalidas");
        }
    }
}
