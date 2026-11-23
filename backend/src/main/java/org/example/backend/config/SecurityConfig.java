package org.example.backend.config;

import org.example.backend.model.enums.Role;
import org.example.backend.security.JwtCookieFilter;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtCookieFilter jwtCookieFilter;
    private final HandlerExceptionResolver resolver;

    public SecurityConfig(
            JwtCookieFilter jwtCookieFilter,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ){
        this.jwtCookieFilter = jwtCookieFilter;
        this.resolver = resolver;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                // 1. Configura o CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. Desabilita o CSRF nativo (já prevenido pelo cookie SameSite=Strict)
                .csrf(AbstractHttpConfigurer::disable)
                // 3. Define que a API é Stateless (sem sessão no servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 4. Regras de autorização de rotas
                .authorizeHttpRequests(auth -> auth
                        // 1. Rotas públicas
                        .requestMatchers(HttpMethod.POST, "/auth/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()

                        // 2. Rotas protegidas
                        .requestMatchers("/*/me", "/*/me/**").authenticated()

                        // 3. Rotas restritas
                        .requestMatchers("/*/admin", "/*/admin/**").hasRole(Role.ADMIN.name())

                        // 4. Outras rotas são restritas a admins
                        .anyRequest().hasRole(Role.ADMIN.name())
                // 5. Redireciona os erros de sem autenticação e sem autorização pro GlobalExceptionHandler
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                resolver.resolveException(request, response, null, authException)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                resolver.resolveException(request, response, null, accessDeniedException)
                        ))
                // 6. Injeta o filtro de Cookie ANTES do filtro padrão de autenticação do Spring
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
