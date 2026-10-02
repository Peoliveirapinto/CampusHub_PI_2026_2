package org.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SenhaService {
    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
