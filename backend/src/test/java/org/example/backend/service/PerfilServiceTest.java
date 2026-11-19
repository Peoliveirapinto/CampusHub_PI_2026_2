package org.example.backend.service;

import org.example.backend.dto.perfil.*;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.PerfilMapper;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.PerfilRepository;
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
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PerfilMapper perfilMapper;

    @Mock
    private SecurityFacade securityFacade;

    @InjectMocks
    private PerfilService perfilService;

    private UUID usuarioId;
    private Usuario usuario;
    private Perfil perfil;
    private PerfilRespostaDTO perfilRespostaDTO;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("teste@exemplo.com");

        perfil = new Perfil();
        perfil.setId(UUID.randomUUID());
        perfil.setUsuario(usuario);
        perfil.setNome("Nome Teste");
        perfil.setDescricao("Descricao Teste");
        perfil.setCurso("Curso Teste");
        perfil.setAnoDeIngresso(2024);
        perfil.setSemestreDeIngresso(1);
        perfil.setDisponibilidade("Disponivel");
        perfil.setContato("123456789");
        perfil.setAreasDeInteresse("Area Teste");

        perfilRespostaDTO = new PerfilRespostaDTO(
                perfil.getId(),
                null, // UsuarioRespostaDTO would be mocked/set if needed
                perfil.getNome(),
                perfil.getDescricao(),
                perfil.getCurso(),
                perfil.getEspecializacao(),
                perfil.getAnoDeIngresso(),
                perfil.getSemestreDeIngresso(),
                perfil.getDisponibilidade(),
                perfil.getContato(),
                perfil.getAreasDeInteresse()
        );
    }

    @Test
    @DisplayName("Deve criar perfil com sucesso")
    void criar_DeveCriarPerfilComSucesso() {
        PerfilCriarDTO perfilCriarDTO = new PerfilCriarDTO(
                usuarioId, "Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area"
        );

        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenReturn(usuario);
        when(perfilRepository.existsByUsuario(usuario)).thenReturn(false);
        when(perfilMapper.toEntity(perfilCriarDTO)).thenReturn(perfil);
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.criar(perfilCriarDTO);

        assertNotNull(resultado);
        assertEquals(perfil.getNome(), resultado.nome());
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario ja tem perfil")
    void criar_DeveLancarExcecaoQuandoUsuarioJaTemPerfil() {
        PerfilCriarDTO perfilCriarDTO = new PerfilCriarDTO(
                usuarioId, "Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area"
        );

        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenReturn(usuario);
        when(perfilRepository.existsByUsuario(usuario)).thenReturn(true);

        assertThrows(JaExisteException.class, () -> {
            perfilService.criar(perfilCriarDTO);
        });
    }

    @Test
    @DisplayName("Deve obter perfil por usuario com sucesso")
    void obterPorUsuarioid_DeveObterPerfilComSucesso() {
        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.obterPorUsuarioid(usuarioId);

        assertNotNull(resultado);
        assertEquals(perfil.getNome(), resultado.nome());
        verify(perfilRepository).findFirstByUsuario(usuario);
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe")
    void obterPorUsuarioid_DeveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            perfilService.obterPorUsuarioid(usuarioId);
        });
    }

    @Test
    @DisplayName("Deve deletar perfil por usuario com sucesso")
    void deletarPorUsuarioId_DeveDeletarPerfilComSucesso() {
        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));

        perfilService.deletarPorUsuarioId(usuarioId);

        verify(perfilRepository).deleteById(perfil.getId());
    }

    @Test
    @DisplayName("Deve atualizar perfil por usuario com sucesso")
    void atualizarPorUsuarioId_DeveAtualizarPerfilComSucesso() {
        PerfilPatchDTO perfilPatchDTO = new PerfilPatchDTO(
                "Novo Nome", "Nova Desc", "Novo Curso", "Esp", 2025, 1, "Disp", "Cont", "Area"
        );

        when(usuarioService.garantirQueUsuarioExiste(usuarioId)).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.atualizarPorUsuarioId(usuarioId, perfilPatchDTO);

        assertNotNull(resultado);
        verify(perfilMapper).updateFromDTO(perfilPatchDTO, perfil);
        verify(perfilRepository).save(perfil);
    }

    @Test
    @DisplayName("Deve criar meu perfil com sucesso")
    void criarMeu_DeveCriarPerfilComSucesso() {
        PerfilCriarMeuDTO perfilCriarMeuDTO = new PerfilCriarMeuDTO(
                "Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area"
        );

        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.garantirQueUsuarioExiste(usuario.getId())).thenReturn(usuario);
        when(perfilRepository.existsByUsuario(usuario)).thenReturn(false);
        when(perfilMapper.toEntity(perfilCriarMeuDTO)).thenReturn(perfil);
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.criarMeu(perfilCriarMeuDTO);

        assertNotNull(resultado);
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Deve obter meu perfil com sucesso")
    void obterMeu_DeveObterPerfilComSucesso() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.garantirQueUsuarioExiste(usuario.getId())).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.obterMeu();

        assertNotNull(resultado);
        verify(perfilRepository).findFirstByUsuario(usuario);
    }

    @Test
    @DisplayName("Deve atualizar meu perfil com sucesso")
    void atualizarMeu_DeveAtualizarPerfilComSucesso() {
        PerfilPatchDTO perfilPatchDTO = new PerfilPatchDTO(
                "Novo Nome", "Nova Desc", "Novo Curso", "Esp", 2025, 1, "Disp", "Cont", "Area"
        );

        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.garantirQueUsuarioExiste(usuario.getId())).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);
        when(perfilMapper.toPerfilRespostaDTO(perfil)).thenReturn(perfilRespostaDTO);

        PerfilRespostaDTO resultado = perfilService.atualizarMeu(perfilPatchDTO);

        assertNotNull(resultado);
        verify(perfilMapper).updateFromDTO(perfilPatchDTO, perfil);
        verify(perfilRepository).save(perfil);
    }

    @Test
    @DisplayName("Deve deletar meu perfil com sucesso")
    void deletarMeu_DeveDeletarPerfilComSucesso() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuario);
        when(perfilRepository.findFirstByUsuario(usuario)).thenReturn(Optional.of(perfil));

        perfilService.deletarMeu();

        verify(perfilRepository).deleteById(perfil.getId());
    }
}
