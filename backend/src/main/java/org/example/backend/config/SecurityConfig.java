package org.example.backend.config;

import org.example.backend.model.enums.Role;
import org.example.backend.security.JwtCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtCookieFilter jwtCookieFilter;

    public SecurityConfig(JwtCookieFilter jwtCookieFilter){
        this.jwtCookieFilter = jwtCookieFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                // 1. Desabilita o CSRF nativo (já prevenido pelo cookie SameSite=Strict)
                .csrf(AbstractHttpConfigurer::disable)
                // 2. Define que a API é Stateless (sem sessão no servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. Regras de autorização de rotas
                .authorizeHttpRequests(auth -> auth
                        // 1. Rotas públicas
                        .requestMatchers(HttpMethod.POST, "/auth/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()

                        // 2. Rotas protegidas


                        // 3. Rotas restritas
                        .requestMatchers(HttpMethod.POST, "/auth/cadastro/admin").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole(Role.ADMIN.name())

                        // 4. Outras rotas requerem estar logado
                        .anyRequest().hasRole(Role.ADMIN.name())
                )
                // 4. Injeta o filtro de Cookie ANTES do filtro padrão de autenticação do Spring
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
