package org.example.backend.service;

import org.example.backend.dto.pareamento.PareamentoFiltroDTO;
import org.example.backend.dto.pareamento.PareamentoRespostaDTO;
import org.example.backend.dto.perfil.PerfilRespostaDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PareamentoServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PerfilMapper perfilMapper;

    @Mock
    private SecurityFacade securityFacade;

    @InjectMocks
    private PareamentoService pareamentoService;

    private Usuario usuarioLogado;
    private Perfil meuPerfil;
    private Perfil candidato1;
    private Perfil candidato2;

    @BeforeEach
    void setUp() {
        usuarioLogado = new Usuario();
        usuarioLogado.setId(UUID.randomUUID());
        usuarioLogado.setEmail("logado@imt.br");

        meuPerfil = new Perfil();
        meuPerfil.setId(UUID.randomUUID());
        meuPerfil.setUsuario(usuarioLogado);
        meuPerfil.setNome("Aluno Logado");
        meuPerfil.setCurso("Ciência da Computação");
        meuPerfil.setEspecializacao("Backend");
        meuPerfil.setAnoDeIngresso(2024);
        meuPerfil.setSemestreDeIngresso(1);
        meuPerfil.setDisponibilidade("Noturno");
        meuPerfil.setContato("11999999999");
        meuPerfil.setAreasDeInteresse("Java, Spring Boot");

        candidato1 = new Perfil();
        candidato1.setId(UUID.randomUUID());
        candidato1.setNome("Bruno Silva");

        candidato2 = new Perfil();
        candidato2.setId(UUID.randomUUID());
        candidato2.setNome("Carlos Souza");
    }

    @Test
    @DisplayName("Deve retornar matches ordenados com pontuação calculada pelo banco de dados")
    void obterPareamentoMeu_DeveRetornarMatchesComPontuacao() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuarioLogado);
        when(perfilRepository.findFirstByUsuario(usuarioLogado)).thenReturn(Optional.of(meuPerfil));

        Object[] row1 = new Object[]{candidato1, 4};
        Object[] row2 = new Object[]{candidato2, 2};
        List<Object[]> resultadosBanco = List.of(row1, row2);

        when(perfilRepository.encontrarMelhoresMatchesComPontuacao(
                eq(usuarioLogado), eq(2024), eq(1), eq("Ciência da Computação"),
                eq("Noturno"), eq("Backend"), eq("Java, Spring Boot"),
                isNull(), isNull()
        )).thenReturn(resultadosBanco);

        when(perfilMapper.toPerfilRespostaDTO(candidato1)).thenReturn(
                new PerfilRespostaDTO(candidato1.getId(), null, "Bruno Silva", null, "Ciência da Computação", "Backend", 2024, 1, "Noturno", null, null)
        );
        when(perfilMapper.toPerfilRespostaDTO(candidato2)).thenReturn(
                new PerfilRespostaDTO(candidato2.getId(), null, "Carlos Souza", null, "Engenharia", null, 2024, 1, "Noturno", null, null)
        );

        List<PareamentoRespostaDTO> matches = pareamentoService.obterPareamentoMeu(null);

        assertNotNull(matches);
        assertEquals(2, matches.size());

        assertEquals("Bruno Silva", matches.get(0).perfil().nome());
        assertEquals(4, matches.get(0).pontuacaoMatch());

        assertEquals("Carlos Souza", matches.get(1).perfil().nome());
        assertEquals(2, matches.get(1).pontuacaoMatch());
    }

    @Test
    @DisplayName("Deve passar parâmetros de filtro para a query do repositório")
    void obterPareamentoMeu_ComFiltro_DeveRepassarParaRepositorio() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuarioLogado);
        when(perfilRepository.findFirstByUsuario(usuarioLogado)).thenReturn(Optional.of(meuPerfil));

        Object[] row1 = new Object[]{candidato1, 3};
        when(perfilRepository.encontrarMelhoresMatchesComPontuacao(
                eq(usuarioLogado), eq(2024), eq(1), eq("Ciência da Computação"),
                eq("Noturno"), eq("Backend"), eq("Java, Spring Boot"),
                eq("Ciência da Computação"), eq("Noturno")
        )).thenReturn(List.<Object[]>of(row1));

        when(perfilMapper.toPerfilRespostaDTO(candidato1)).thenReturn(
                new PerfilRespostaDTO(candidato1.getId(), null, "Bruno Silva", null, "Ciência da Computação", null, 2024, 1, "Noturno", null, null)
        );

        PareamentoFiltroDTO filtro = new PareamentoFiltroDTO("Ciência da Computação", "Noturno");
        List<PareamentoRespostaDTO> matches = pareamentoService.obterPareamentoMeu(filtro);

        assertEquals(1, matches.size());
        assertEquals("Bruno Silva", matches.get(0).perfil().nome());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o usuário não tiver perfil")
    void obterPareamentoMeu_DeveLancarExcecaoQuandoUsuarioNaoTemPerfil() {
        when(securityFacade.obterUsuarioLogado()).thenReturn(usuarioLogado);
        when(perfilRepository.findFirstByUsuario(usuarioLogado)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                pareamentoService.obterPareamentoMeu(null)
        );
    }

    @Test
    @DisplayName("Deve obter pareamento por usuarioId para rota administrativa")
    void obterPareamentoPorUsuarioId_DeveRetornarMatchesComSucesso() {
        UUID outroId = UUID.randomUUID();
        when(usuarioService.garantirQueUsuarioExiste(outroId)).thenReturn(usuarioLogado);
        when(perfilRepository.findFirstByUsuario(usuarioLogado)).thenReturn(Optional.of(meuPerfil));

        Object[] row1 = new Object[]{candidato1, 3};
        when(perfilRepository.encontrarMelhoresMatchesComPontuacao(
                eq(usuarioLogado), eq(2024), eq(1), eq("Ciência da Computação"),
                eq("Noturno"), eq("Backend"), eq("Java, Spring Boot"),
                isNull(), isNull()
        )).thenReturn(List.<Object[]>of(row1));

        when(perfilMapper.toPerfilRespostaDTO(candidato1)).thenReturn(
                new PerfilRespostaDTO(candidato1.getId(), null, "Bruno Silva", null, "Ciência da Computação", null, 2024, 1, "Noturno", null, null)
        );

        List<PareamentoRespostaDTO> matches = pareamentoService.obterPareamentoPorUsuarioId(outroId, null);

        assertNotNull(matches);
        assertEquals(1, matches.size());
        verify(usuarioService).garantirQueUsuarioExiste(outroId);
    }
}
