package org.example.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    private SecretKey getChaveAssinatura() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Gera o token inserindo o id e a Role
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("role", usuario.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getChaveAssinatura())
                .compact();
    }

    // Valida o token e extrai todos os dados (claims)
    public Claims extrairClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getChaveAssinatura())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValido(String token) {
        Claims claims = extrairClaims(token);
        if (claims == null) return false;

        Date dataExpiracao = claims.getExpiration();
        return dataExpiracao != null && dataExpiracao.after(new Date());
    }

    public String extrairSubject(String token) {
        Claims claims = extrairClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String extrairRole(String token) {
        Claims claims = extrairClaims(token);
        return claims != null ? claims.get("role", String.class) : null;
    }
}
