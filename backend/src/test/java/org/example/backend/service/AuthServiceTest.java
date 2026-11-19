package org.example.backend.service;

import org.example.backend.dto.auth.UsuarioETokenDTO;
import org.example.backend.dto.usuario.UsuarioCadastroDTO;
import org.example.backend.dto.usuario.UsuarioLoginDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.example.backend.exception.JaExisteException;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.example.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SenhaService senhaService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private UsuarioCadastroDTO cadastroDTO;
    private UsuarioLoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(java.util.UUID.randomUUID());
        usuario.setEmail("teste@exemplo.com");
        usuario.setSenha("senha123");
        usuario.setRole(Role.USER);

        cadastroDTO = new UsuarioCadastroDTO("teste@exemplo.com", "senha123");
        loginDTO = new UsuarioLoginDTO("teste@exemplo.com", "senha123");
    }

    @Test
    @DisplayName("Deve cadastrar admin com sucesso")
    void cadastrarAdmin_DeveCriarAdminComSucesso() {
        when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(false);
        when(senhaService.hash(cadastroDTO.senha())).thenReturn("senhaHasheada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(new UsuarioRespostaDTO(usuario.getId(), usuario.getEmail(), usuario.getRole()));

        UsuarioRespostaDTO resultado = authService.cadastrarAdmin(cadastroDTO);

        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.email());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando admin ja existe")
    void cadastrarAdmin_DeveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(true);

        assertThrows(JaExisteException.class, () -> {
            authService.cadastrarAdmin(cadastroDTO);
        });
    }

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso")
    void cadastrarUsuario_DeveCriarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(false);
        when(senhaService.hash(cadastroDTO.senha())).thenReturn("senhaHasheada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(new UsuarioRespostaDTO(usuario.getId(), usuario.getEmail(), usuario.getRole()));

        UsuarioRespostaDTO resultado = authService.cadastrarUsuario(cadastroDTO);

        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.email());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario ja existe")
    void cadastrarUsuario_DeveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.existsByEmail(cadastroDTO.email())).thenReturn(true);

        assertThrows(JaExisteException.class, () -> {
            authService.cadastrarUsuario(cadastroDTO);
        });
    }

    @Test
    @DisplayName("Deve autenticar usuario com sucesso")
    void autenticarUsuario_DeveAutenticarComSucesso() {
        when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(usuario));
        doNothing().when(senhaService).garantirQueASenhaEstaCerta(anyString(), anyString());
        when(jwtService.gerarToken(usuario)).thenReturn("token-jwt");

        UsuarioETokenDTO resultado = authService.autenticarUsuario(loginDTO);

        assertNotNull(resultado);
        assertEquals("token-jwt", resultado.token());
        assertEquals(usuario.getId(), resultado.usuario().getId());
        verify(senhaService).garantirQueASenhaEstaCerta(loginDTO.senha(), usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lancar excecao quando credenciais invalidas")
    void autenticarUsuario_DeveLancarExcecaoQuandoCredenciaisInvalidas() {
        when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class, () -> {
            authService.autenticarUsuario(loginDTO);
        });
    }
}
