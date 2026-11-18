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

        return senha.length() >= 8; // Aqui a espaço para melhoria na validação da senha
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
