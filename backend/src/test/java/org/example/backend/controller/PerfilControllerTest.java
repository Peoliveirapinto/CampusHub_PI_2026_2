package org.example.backend.controller;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.example.backend.dto.perfil.*;
import org.example.backend.exception.GlobalExceptionHandler;
import org.example.backend.service.PerfilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    @Mock
    private PerfilService perfilService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PerfilController perfilController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private PerfilCriarDTO perfilCriarDTO;
    private PerfilCriarMeuDTO perfilCriarMeuDTO;
    private PerfilPatchDTO perfilPatchDTO;
    private PerfilRespostaDTO perfilRespostaDTO;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(perfilController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();

        objectMapper = JsonMapper.builder().build();

        usuarioId = UUID.randomUUID();
        perfilRespostaDTO = new PerfilRespostaDTO(usuarioId, null, "Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area");

        perfilCriarDTO = new PerfilCriarDTO(usuarioId, "Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area");
        perfilCriarMeuDTO = new PerfilCriarMeuDTO("Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area");
        perfilPatchDTO = new PerfilPatchDTO("Nome", "Desc", "Curso", "Esp", 2024, 1, "Disp", "Cont", "Area");

        lenient().when(messageSource.getMessage(anyString(), any(), anyString(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(2));
    }

    @Nested
    @DisplayName("Rotas Administrativas (/perfis/admin)")
    class RotasAdministrativas {

        @Test
        @DisplayName("Deve criar perfil com sucesso retornando 201 CREATED")
        void criar_DeveRetornar201Created() throws Exception {
            when(perfilService.criar(any(PerfilCriarDTO.class))).thenReturn(perfilRespostaDTO);

            mockMvc.perform(post("/perfis/admin")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(perfilCriarDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.nome").value("Nome"));

            verify(perfilService).criar(any(PerfilCriarDTO.class));
        }

        @Test
        @DisplayName("Deve obter perfil por usuarioId com sucesso retornando 200 OK")
        void obterPorUsuarioId_DeveRetornar200Ok() throws Exception {
            when(perfilService.obterPorUsuarioid(usuarioId)).thenReturn(perfilRespostaDTO);

            mockMvc.perform(get("/perfis/admin/{usuarioId}", usuarioId)
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.nome").value("Nome"));

            verify(perfilService).obterPorUsuarioid(usuarioId);
        }

        @Test
        @DisplayName("Deve deletar perfil por usuarioId com sucesso retornando 204 NO CONTENT")
        void deletarPorUsuarioId_DeveRetornar204NoContent() throws Exception {
            doNothing().when(perfilService).deletarPorUsuarioId(usuarioId);

            mockMvc.perform(delete("/perfis/admin/{usuarioId}", usuarioId))
                    .andExpect(status().isNoContent());

            verify(perfilService).deletarPorUsuarioId(usuarioId);
        }

        @Test
        @DisplayName("Deve atualizar perfil por usuarioId com sucesso retornando 200 OK")
        void atualizarPorUsuarioId_DeveRetornar200Ok() throws Exception {
            when(perfilService.atualizarPorUsuarioId(eq(usuarioId), any(PerfilPatchDTO.class))).thenReturn(perfilRespostaDTO);

            mockMvc.perform(patch("/perfis/admin/{usuarioId}", usuarioId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(perfilPatchDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()));

            verify(perfilService).atualizarPorUsuarioId(eq(usuarioId), any(PerfilPatchDTO.class));
        }
    }

    @Nested
    @DisplayName("Rotas do Proprio Perfil (/perfis/me)")
    class RotasDoProprioPerfil {

        @Test
        @DisplayName("Deve criar meu perfil com sucesso retornando 201 CREATED")
        void criarMeu_DeveRetornar201Created() throws Exception {
            when(perfilService.criarMeu(any(PerfilCriarMeuDTO.class))).thenReturn(perfilRespostaDTO);

            mockMvc.perform(post("/perfis/me")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(perfilCriarMeuDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()));

            verify(perfilService).criarMeu(any(PerfilCriarMeuDTO.class));
        }

        @Test
        @DisplayName("Deve obter meu perfil com sucesso retornando 200 OK")
        void obterMeu_DeveRetornar200Ok() throws Exception {
            when(perfilService.obterMeu()).thenReturn(perfilRespostaDTO);

            mockMvc.perform(get("/perfis/me")
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()));

            verify(perfilService).obterMeu();
        }

        @Test
        @DisplayName("Deve atualizar meu perfil com sucesso retornando 200 OK")
        void atualizarMeu_DeveRetornar200Ok() throws Exception {
            when(perfilService.atualizarMeu(any(PerfilPatchDTO.class))).thenReturn(perfilRespostaDTO);

            mockMvc.perform(patch("/perfis/me")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(perfilPatchDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()));

            verify(perfilService).atualizarMeu(any(PerfilPatchDTO.class));
        }

        @Test
        @DisplayName("Deve deletar meu perfil com sucesso retornando 204 NO CONTENT")
        void deletarMeu_DeveRetornar204NoContent() throws Exception {
            doNothing().when(perfilService).deletarMeu();

            mockMvc.perform(delete("/perfis/me"))
                    .andExpect(status().isNoContent());

            verify(perfilService).deletarMeu();
        }
    }
}
