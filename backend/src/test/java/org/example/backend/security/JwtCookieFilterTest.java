package org.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtCookieFilterTest {

    private JwtService jwtService;
    private JwtCookieFilter jwtCookieFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        jwtService = Mockito.mock(JwtService.class);
        jwtCookieFilter = new JwtCookieFilter(jwtService);
        
        // Set the property injected by @Value
        ReflectionTestUtils.setField(jwtCookieFilter, "jwtCookieName", "jwt");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_DeveAutenticarUsuarioQuandoCookieValido() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Cookie cookie = new Cookie("jwt", "valid-token");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        
        when(jwtService.isTokenValido("valid-token")).thenReturn(true);
        when(jwtService.extrairSubject("valid-token")).thenReturn("123");
        when(jwtService.extrairRole("valid-token")).thenReturn("ADMIN");

        jwtCookieFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("123", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_DeveNaoAutenticarQuandoCookieAusente() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        when(request.getCookies()).thenReturn(null);

        jwtCookieFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_DeveNaoAutenticarQuandoTokenInvalido() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Cookie cookie = new Cookie("jwt", "invalid-token");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        
        when(jwtService.isTokenValido("invalid-token")).thenReturn(false);

        jwtCookieFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}