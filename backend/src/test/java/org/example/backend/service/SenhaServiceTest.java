package org.example.backend.service;

import org.example.backend.exception.CredenciaisInvalidasException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenhaServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SenhaService senhaService;

    @Test
    @DisplayName("Deve validar senha com comprimento suficiente")
    void ehValida_DeveRetornarTrueParaSenhaLonga() {
        assertTrue(senhaService.ehValida("senha123456"));
    }

    @Test
    @DisplayName("Deve retornar falso para senha curta")
    void ehValida_DeveRetornarFalseParaSenhaCurta() {
        assertFalse(senhaService.ehValida("senha12"));
    }

    @Test
    @DisplayName("Deve retornar falso para senha nula ou em branco")
    void ehValida_DeveRetornarFalseParaSenhaNulaOuBranco() {
        assertFalse(senhaService.ehValida(null));
        assertFalse(senhaService.ehValida("   "));
    }

    @Test
    @DisplayName("Deve gerar hash da senha")
    void hash_DeveRetornarHash() {
        when(passwordEncoder.encode(anyString())).thenReturn("senhaHasheada");
        String resultado = senhaService.hash("senhaOriginal");
        assertEquals("senhaHasheada", resultado);
    }

    @Test
    @DisplayName("Deve verificar se a senha coincide com o hash")
    void matchesTheHash_DeveRetornarTrueQuandoCoincide() {
        when(passwordEncoder.matches("senhaLimpa", "senhaHasheada")).thenReturn(true);
        assertTrue(senhaService.matchesTheHash("senhaLimpa", "senhaHasheada"));
    }

    @Test
    @DisplayName("Deve lançar excecao quando a senha não coincide")
    void garantirQueASenhaEstaCerta_DeveLancarExcecaoQuandoNaoCoincide() {
        when(passwordEncoder.matches("senhaLimpa", "senhaHasheada")).thenReturn(false);
        assertThrows(CredenciaisInvalidasException.class, () -> {
            senhaService.garantirQueASenhaEstaCerta("senhaLimpa", "senhaHasheada");
        });
    }
}
