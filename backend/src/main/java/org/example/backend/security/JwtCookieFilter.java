package org.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.service.JwtService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {
    @Value("${api.security.token.cookie-name}")
    private String jwtCookieName;

    private final JwtService jwtService;

    public JwtCookieFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Extrai a string do token contida nos Cookies da requisição
        String token = extrairTokenDoCookie(request);

        // 2. Valida a integridade e expiração do token
        if (token != null && jwtService.isTokenValido(token)) {
            // O Subject agora contém o ID do usuário (ex: "1")
            String userId = jwtService.extrairSubject(token);
            String role = jwtService.extrairRole(token);

            if (userId != null && role != null) {
                // Cria a autoridade com o prefixo "ROLE_" exigido pelo Spring Security (ex: "ROLE_ADMIN")
                var authority = new SimpleGrantedAuthority("ROLE_" + role);

                // Passa o userId (String) como o 'Principal' (identificador) da autenticação
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(authority)
                );

                // Define a requisição atual como AUTENTICADA no contexto da thread
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua a execução da cadeia de filtros
        filterChain.doFilter(request, response);
    }

    private String extrairTokenDoCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (jwtCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
