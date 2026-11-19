package org.example.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CookieServiceTest {

    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        cookieService = new CookieService();
        ReflectionTestUtils.setField(cookieService, "jwtCookieName", "jwt");
        ReflectionTestUtils.setField(cookieService, "expiration", 3600L);
    }

    @Test
    @DisplayName("Deve gerar cookie de login com token")
    void gerarCookieJwtLogin_DeveRetornarCookieComToken() {
        String token = "test-token";
        ResponseCookie cookie = cookieService.gerarCookieJwtLogin(token);

        assertNotNull(cookie);
        assertEquals("jwt", cookie.getName());
        assertEquals(token, cookie.getValue());
        assertEquals(3600L / 1000, cookie.getMaxAge().toSeconds());
        assertTrue(cookie.isHttpOnly());
        assertEquals("Strict", cookie.getSameSite());
    }

    @Test
    @DisplayName("Deve gerar cookie de logout expirado")
    void gerarCookieJwtLogout_DeveRetornarCookieExpirao() {
        ResponseCookie cookie = cookieService.gerarCookieJwtLogout();

        assertNotNull(cookie);
        assertEquals("jwt", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge().toSeconds());
        assertTrue(cookie.isHttpOnly());
        assertEquals("Strict", cookie.getSameSite());
    }
}
