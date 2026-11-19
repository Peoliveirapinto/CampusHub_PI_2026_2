package org.example.backend.service;

import org.example.backend.dto.usuario.AlterarSenhaDTO;
import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.BadRequestException;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.example.backend.repository.UsuarioRepository;
import org.example.backend.security.SecurityFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private SecurityFacade securityFacade;

    @Mock
    private SenhaService senhaService;

    @InjectMocks
    private UsuarioService usuarioService;

    private UUID usuarioId;
    private Usuario usuario;
    private UsuarioRespostaDTO usuarioRespostaDTO;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("teste@exemplo.com");
        usuario.setRole(Role.USER);
        usuario.setSenha("senha123");

        usuarioRespostaDTO = new UsuarioRespostaDTO(usuarioId, usuario.getEmail(), usuario.getRole());
    }

    @Test
    @DisplayName("Deve garantir que usuario existe")
    void garantirQueUsuarioExiste_DeveRetornarUsuario() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.garantirQueUsuarioExiste(usuarioId);
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
    }

    @Test
    @DisplayName("Deve lancar excecao se usuario nao existir na validacao")
    void garantirQueUsuarioExiste_DeveLancarExcecao() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.garantirQueUsuarioExiste(usuarioId);
        });
    }

    @Test
    @DisplayName("Deve obter usuario por ID com sucesso")
    void obterPorId_DeveRetornarUsuarioDTO() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(usuarioRespostaDTO);

        UsuarioRespostaDTO resultado = usuarioService.obterPorId(usuarioId);

        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.id());
        assertEquals(usuario.getEmail(), resultado.email());
        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioMapper).toUsuarioRespostaDTO(usuario);
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existir")
    void obterPorId_DeveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.obterPorId(usuarioId);
        });
    }

    @Test
    @DisplayName("Deve deletar usuario por ID com sucesso")
    void deletarPorId_DeveDeletarComSucesso() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        usuarioService.deletarPorId(usuarioId);

        verify(usuarioRepository).deleteById(usuarioId);
    }

    @Test
    @DisplayName("Deve atualizar usuario por ID com sucesso")
    void atualizarPorId_DeveAtualizarComSucesso() {
        UsuarioPatchDTO patchDTO = new UsuarioPatchDTO("novo@exemplo.com", Role.USER);
        
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("novo@exemplo.com")).thenReturn(false);
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(usuarioRespostaDTO);

        usuarioService.atualizarPorId(usuarioId, patchDTO);

        assertEquals("novo@exemplo.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar email que ja existe")
    void atualizarPorId_DeveLancarExcecaoQuandoEmailJaExiste() {
        UsuarioPatchDTO patchDTO = new UsuarioPatchDTO("email_ja_existe@exemplo.com", Role.USER);
        
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("email_ja_existe@exemplo.com")).thenReturn(true);

        assertThrows(JaExisteException.class, () -> {
            usuarioService.atualizarPorId(usuarioId, patchDTO);
        });
    }

    @Test
    @DisplayName("Deve obter dados do usuario logado")
    void obterMeu_DeveRetornarUsuarioLogado() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(usuarioRespostaDTO);

        UsuarioRespostaDTO resultado = usuarioService.obterMeu();

        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.email());
        verify(securityFacade).obterUsuarioLogado();
    }

    @Test
    @DisplayName("Deve atualizar dados do usuario logado")
    void atualizarMeu_DeveAtualizarDadosLogado() {
        UsuarioPatchDTO patchDTO = new UsuarioPatchDTO("meu@novo.com", Role.USER);
        
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(usuarioRepository.existsByEmail("meu@novo.com")).thenReturn(false);
        when(usuarioMapper.toUsuarioRespostaDTO(usuario)).thenReturn(usuarioRespostaDTO);

        usuarioService.atualizarMeu(patchDTO);

        assertEquals("meu@novo.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve deletar o proprio usuario")
    void deletarMeu_DeveDeletarUsuarioLogado() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);

        usuarioService.deletarMeu();

        verify(usuarioRepository).deleteById(usuario.getId());
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso")
    void alterarMinhaSenha_DeveAlterarComSucesso() {
        AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaAtual", "novaSenha123", "novaSenha123");
        
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        doNothing().when(senhaService).garantirQueASenhaEstaCerta(anyString(), anyString());
        when(senhaService.hash("novaSenha123")).thenReturn("senhaHasheada");

        usuarioService.alterarMinhaSenha(alterarSenhaDTO);

        verify(senhaService).hash("novaSenha123");
        assertNotEquals("senha123", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lancar excecao quando senhas novas nao coincidem")
    void alterarMinhaSenha_DeveLancarExcecaoQuandoSenhasNaoCoincidem() {
        AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaAtual", "novaSenha123", "outraSenha456");

        assertThrows(BadRequestException.class, () -> {
            usuarioService.alterarMinhaSenha(alterarSenhaDTO);
        });
    }

    @Test
    @DisplayName("Deve lancar excecao quando senha atual incorreta")
    void alterarMinhaSenha_DeveLancarExcecaoQuandoSenhaAtualIncorreta() {
        AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaIncorreta", "novaSenha123", "novaSenha123");
        
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        doThrow(new CredenciaisInvalidasException("auth.credenciais-invalidas"))
            .when(senhaService).garantirQueASenhaEstaCerta(anyString(), anyString());

        assertThrows(CredenciaisInvalidasException.class, () -> {
            usuarioService.alterarMinhaSenha(alterarSenhaDTO);
        });
    }
}
