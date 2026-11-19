package org.example.backend.service;

import io.jsonwebtoken.Claims;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // A valid HMAC-SHA key must be at least 256 bits (32 characters)
        ReflectionTestUtils.setField(jwtService, "secret", "superSecretKeyForJwtTestingThatIsLongEnough");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L); // 1 hour in ms
    }

    @Test
    @DisplayName("Deve gerar token JWT com sucesso")
    void gerarToken_DeveRetornarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setRole(Role.USER);

        String token = jwtService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = jwtService.extrairClaims(token);
        assertNotNull(claims);
        assertEquals(usuario.getId().toString(), claims.getSubject());
        assertEquals(Role.USER.name(), claims.get("role", String.class));
    }

    @Test
    @DisplayName("Deve extrair claims com sucesso")
    void extrairClaims_DeveRetornarClaimsCorretas() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setRole(Role.USER);

        String token = jwtService.gerarToken(usuario);
        Claims claims = jwtService.extrairClaims(token);

        assertNotNull(claims);
        assertEquals(usuario.getId().toString(), claims.getSubject());
    }

    @Test
    @DisplayName("Deve validar token JWT válido")
    void isTokenValido_DeveRetornarTrueParaTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setRole(Role.USER);

        String token = jwtService.gerarToken(usuario);

        assertTrue(jwtService.isTokenValido(token));
    }

    @Test
    @DisplayName("Deve retornar falso para token inválido")
    void isTokenValido_DeveRetornarFalseParaTokenInvalido() {
        assertFalse(jwtService.isTokenValido("invalid.token.string"));
    }

    @Test
    @DisplayName("Deve extrair subject do token")
    void extrairSubject_DeveRetornarSubjectCorreto() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setRole(Role.USER);

        String token = jwtService.gerarToken(usuario);
        String subject = jwtService.extrairSubject(token);

        assertEquals(usuario.getId().toString(), subject);
    }

    @Test
    @DisplayName("Deve extrair role do token")
    void extrairRole_DeveRetornarRoleCorreta() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setRole(Role.USER);

        String token = jwtService.gerarToken(usuario);
        String role = jwtService.extrairRole(token);

        assertEquals(Role.USER.name(), role);
    }
}
