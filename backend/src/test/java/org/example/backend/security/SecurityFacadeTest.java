package org.example.backend.security;

import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.TokenPayloadInvalidoException;
import org.example.backend.exception.UsuarioNaoAutenticadoException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFacadeTest {

    private UsuarioRepository usuarioRepository;
    private SecurityFacade securityFacade;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        securityFacade = new SecurityFacade(usuarioRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void obterIdDoUsuarioLogado_DeveRetornarIdQuandoPrincipalForValido() {
        UUID id = UUID.randomUUID();
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(id.toString());
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID result = securityFacade.obterIdDoUsuarioLogado();

        assertEquals(id, result);
    }

    @Test
    void obterIdDoUsuarioLogado_DeveLancarExcecaoQuandoPayloadForInvalido() {
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("nao-um-uuid");
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(TokenPayloadInvalidoException.class, () -> securityFacade.obterIdDoUsuarioLogado());
    }

    @Test
    void obterIdDoUsuarioLogado_DeveLancarExcecaoQuandoNaoAutenticado() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThrows(UsuarioNaoAutenticadoException.class, () -> securityFacade.obterIdDoUsuarioLogado());
    }

    @Test
    void obterUsuarioLogado_DeveRetornarUsuarioQuandoExistir() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario("teste@exemplo.com", "senha123");
        usuario.setId(id);

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(id.toString());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario result = securityFacade.obterUsuarioLogado();

        assertEquals(usuario, result);
    }

    @Test
    void obterUsuarioLogado_DeveLancarExcecaoQuandoUsuarioNaoExistir() {
        UUID id = UUID.randomUUID();
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(id.toString());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> securityFacade.obterUsuarioLogado());
    }
}