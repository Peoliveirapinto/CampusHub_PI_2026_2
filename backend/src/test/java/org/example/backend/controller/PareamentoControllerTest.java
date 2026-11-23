package org.example.backend.controller;

import org.example.backend.dto.pareamento.PareamentoFiltroDTO;
import org.example.backend.dto.pareamento.PareamentoRespostaDTO;
import org.example.backend.dto.perfil.PerfilRespostaDTO;
import org.example.backend.exception.GlobalExceptionHandler;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.service.PareamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PareamentoControllerTest {

    @Mock
    private PareamentoService pareamentoService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PareamentoController pareamentoController;

    private MockMvc mockMvc;
    private PareamentoRespostaDTO pareamentoRespostaDTO;
    private UUID perfilId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pareamentoController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();

        perfilId = UUID.randomUUID();
        PerfilRespostaDTO perfilDTO = new PerfilRespostaDTO(
                perfilId, null, "Aluno Match", "Descricao", "Ciência da Computação",
                "Backend", 2024, 1, "Noturno", "11999999999", "Java, Spring Boot"
        );

        pareamentoRespostaDTO = new PareamentoRespostaDTO(
                perfilDTO,
                4
        );

        lenient().when(messageSource.getMessage(any(), any(), any(), any(Locale.class)))
                .thenAnswer(inv -> inv.getArgument(2));
    }

    @Test
    @DisplayName("Deve retornar 200 OK com lista de matches do usuário logado")
    void obterMeuPareamento_DeveRetornar200Ok() throws Exception {
        when(pareamentoService.obterPareamentoMeu(any(PareamentoFiltroDTO.class)))
                .thenReturn(List.of(pareamentoRespostaDTO));

        mockMvc.perform(get("/pareamento/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].perfil.id").value(perfilId.toString()))
                .andExpect(jsonPath("$[0].perfil.nome").value("Aluno Match"))
                .andExpect(jsonPath("$[0].pontuacaoMatch").value(4));

        verify(pareamentoService).obterPareamentoMeu(any(PareamentoFiltroDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 200 OK passando query parameters de filtro")
    void obterMeuPareamento_ComFiltro_DeveRetornar200Ok() throws Exception {
        when(pareamentoService.obterPareamentoMeu(any(PareamentoFiltroDTO.class)))
                .thenReturn(List.of(pareamentoRespostaDTO));

        mockMvc.perform(get("/pareamento/me")
                        .param("curso", "Ciência da Computação")
                        .param("disponibilidade", "Noturno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].perfil.nome").value("Aluno Match"))
                .andExpect(jsonPath("$[0].pontuacaoMatch").value(4));

        verify(pareamentoService).obterPareamentoMeu(any(PareamentoFiltroDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND quando usuário logado não tem perfil")
    void obterMeuPareamento_SemPerfil_DeveRetornar404() throws Exception {
        when(pareamentoService.obterPareamentoMeu(any(PareamentoFiltroDTO.class)))
                .thenThrow(new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario"));

        mockMvc.perform(get("/pareamento/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 OK na rota administrativa com usuarioId")
    void obterPareamentoPorUsuarioId_DeveRetornar200Ok() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        when(pareamentoService.obterPareamentoPorUsuarioId(eq(usuarioId), any(PareamentoFiltroDTO.class)))
                .thenReturn(List.of(pareamentoRespostaDTO));

        mockMvc.perform(get("/pareamento/admin/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].perfil.nome").value("Aluno Match"))
                .andExpect(jsonPath("$[0].pontuacaoMatch").value(4));

        verify(pareamentoService).obterPareamentoPorUsuarioId(eq(usuarioId), any(PareamentoFiltroDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 404 NOT FOUND na rota administrativa quando perfil do usuarioId não existir")
    void obterPareamentoPorUsuarioId_SemPerfil_DeveRetornar404() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        when(pareamentoService.obterPareamentoPorUsuarioId(eq(usuarioId), any(PareamentoFiltroDTO.class)))
                .thenThrow(new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario"));

        mockMvc.perform(get("/pareamento/admin/{usuarioId}", usuarioId))
                .andExpect(status().isNotFound());
    }
}
