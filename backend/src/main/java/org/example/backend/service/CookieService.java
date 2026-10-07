package org.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Value("${api.security.token.cookie-name}")
    private String jwtCookieName;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    public ResponseCookie gerarCookieJwtLogin(String token){
        return ResponseCookie.from(jwtCookieName, token)
                .httpOnly(true)
                .secure(false) // Altere para 'true' em ambiente de Produção (exige HTTPS)
                .path("/")
                .maxAge(expiration / 1000) // Dividi por 1000 pq a validade é em segundos
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie gerarCookieJwtLogout(){
        // Expira o cookie imediatamente enviando maxAge(0)
        return ResponseCookie.from(jwtCookieName, "")
                .httpOnly(true)
                .secure(false) // Altere para 'true' em Produção
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
