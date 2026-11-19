package org.example.backend.validation;

import org.example.backend.service.SenhaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SenhaValidaValidatorTest {

    private SenhaService senhaService;
    private SenhaValidaValidator validator;

    @BeforeEach
    void setUp() {
        senhaService = Mockito.mock(SenhaService.class);
        validator = new SenhaValidaValidator();
        
        // Use reflection to inject the mocked service since it's @Autowired
        org.springframework.test.util.ReflectionTestUtils.setField(validator, "senhaService", senhaService);
    }

    @Test
    void isValid_DeveRetornarTrueParaSenhaNulaOuVazia() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @Test
    void isValid_DeveRetornarTrueQuandoSenhaServiceRetornaTrue() {
        when(senhaService.ehValida("senha123456")).thenReturn(true);
        
        assertTrue(validator.isValid("senha123456", null));
    }

    @Test
    void isValid_DeveRetornarFalseQuandoSenhaServiceRetornaFalse() {
        when(senhaService.ehValida("senhaCurta")).thenReturn(false);
        
        assertFalse(validator.isValid("senhaCurta", null));
    }
}