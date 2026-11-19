package org.example.backend.controller;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import org.example.backend.dto.usuario.AlterarSenhaDTO;
import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.BadRequestException;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.example.backend.exception.GlobalExceptionHandler;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.enums.Role;
import org.example.backend.service.CookieService;
import org.example.backend.service.SenhaService;
import org.example.backend.service.UsuarioService;
import org.example.backend.validation.SenhaValidaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CookieService cookieService;

    @Mock
    private SenhaService senhaService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID usuarioId;
    private UsuarioRespostaDTO usuarioRespostaDTO;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(new ConstraintValidatorFactory() {
            @Override
            public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
                if (key.equals(SenhaValidaValidator.class)) {
                    SenhaValidaValidator customValidator = new SenhaValidaValidator();
                    ReflectionTestUtils.setField(customValidator, "senhaService", senhaService);
                    return (T) customValidator;
                }
                try {
                    return key.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void releaseInstance(ConstraintValidator<?, ?> instance) {
            }
        });
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .setValidator(validator)
                .build();

        objectMapper = JsonMapper.builder().build();

        usuarioId = UUID.randomUUID();
        usuarioRespostaDTO = new UsuarioRespostaDTO(usuarioId, "teste@exemplo.com", Role.USER);

        lenient().when(messageSource.getMessage(anyString(), any(), anyString(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(2));
    }

    @Nested
    @DisplayName("Rotas Administrativas (/usuarios/admin)")
    class RotasAdministrativas {

        @Test
        @DisplayName("Deve obter usuario por ID com sucesso retornando 200 OK")
        void getById_DeveRetornar200EUsuarioRespostaDTO() throws Exception {
            when(usuarioService.obterPorId(usuarioId)).thenReturn(usuarioRespostaDTO);

            mockMvc.perform(get("/usuarios/admin/{id}", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.email").value("teste@exemplo.com"))
                    .andExpect(jsonPath("$.role").value("USER"));

            verify(usuarioService).obterPorId(usuarioId);
        }

        @Test
        @DisplayName("Deve retornar 404 NOT FOUND ao buscar usuario por ID inexistente")
        void getById_DeveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
            when(usuarioService.obterPorId(usuarioId))
                    .thenThrow(new ResourceNotFoundException("usuario.nao-encontrado"));

            mockMvc.perform(get("/usuarios/admin/{id}", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(usuarioService).obterPorId(usuarioId);
        }

        @Test
        @DisplayName("Deve deletar usuario por ID com sucesso retornando 204 NO CONTENT")
        void deletar_DeveRetornar204NoContent() throws Exception {
            doNothing().when(usuarioService).deletarPorId(usuarioId);

            mockMvc.perform(delete("/usuarios/admin/{id}", usuarioId))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(usuarioService).deletarPorId(usuarioId);
        }

        @Test
        @DisplayName("Deve retornar 404 NOT FOUND ao tentar deletar usuario por ID inexistente")
        void deletar_DeveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
            doThrow(new ResourceNotFoundException("usuario.nao-encontrado"))
                    .when(usuarioService).deletarPorId(usuarioId);

            mockMvc.perform(delete("/usuarios/admin/{id}", usuarioId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(usuarioService).deletarPorId(usuarioId);
        }

        @Test
        @DisplayName("Deve atualizar usuario por ID com sucesso retornando 200 OK")
        void atualizar_DeveRetornar200EUsuarioRespostaDTOAtualizado() throws Exception {
            UsuarioPatchDTO patchDTO = new UsuarioPatchDTO("atualizado@exemplo.com", Role.ADMIN);
            UsuarioRespostaDTO usuarioAtualizadoDTO = new UsuarioRespostaDTO(usuarioId, "atualizado@exemplo.com", Role.ADMIN);

            when(usuarioService.atualizarPorId(eq(usuarioId), any(UsuarioPatchDTO.class)))
                    .thenReturn(usuarioAtualizadoDTO);

            mockMvc.perform(patch("/usuarios/admin/{id}", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(patchDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.email").value("atualizado@exemplo.com"))
                    .andExpect(jsonPath("$.role").value("ADMIN"));

            verify(usuarioService).atualizarPorId(eq(usuarioId), any(UsuarioPatchDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 BAD REQUEST ao tentar atualizar por ID com email invalido")
        void atualizar_DeveRetornar400QuandoEmailInvalido() throws Exception {
            UsuarioPatchDTO patchDTOInvalido = new UsuarioPatchDTO("email-invalido", Role.ADMIN);

            mockMvc.perform(patch("/usuarios/admin/{id}", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(patchDTOInvalido)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).atualizarPorId(any(), any());
        }
    }

    @Nested
    @DisplayName("Rotas do Proprio Usuario (/usuarios/me)")
    class RotasDoProprioUsuario {

        @Test
        @DisplayName("Deve obter dados do usuario autenticado com sucesso retornando 200 OK")
        void obterMeu_DeveRetornar200EUsuarioRespostaDTO() throws Exception {
            when(usuarioService.obterMeu()).thenReturn(usuarioRespostaDTO);

            mockMvc.perform(get("/usuarios/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.email").value("teste@exemplo.com"))
                    .andExpect(jsonPath("$.role").value("USER"));

            verify(usuarioService).obterMeu();
        }

        @Test
        @DisplayName("Deve atualizar dados do usuario autenticado com sucesso retornando 200 OK")
        void atualizarMeu_DeveRetornar200EUsuarioRespostaDTOAtualizado() throws Exception {
            UsuarioPatchDTO patchDTO = new UsuarioPatchDTO("novo_email@exemplo.com", null);
            UsuarioRespostaDTO usuarioAtualizadoDTO = new UsuarioRespostaDTO(usuarioId, "novo_email@exemplo.com", Role.USER);

            when(usuarioService.atualizarMeu(any(UsuarioPatchDTO.class)))
                    .thenReturn(usuarioAtualizadoDTO);

            mockMvc.perform(patch("/usuarios/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(patchDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                    .andExpect(jsonPath("$.email").value("novo_email@exemplo.com"))
                    .andExpect(jsonPath("$.role").value("USER"));

            verify(usuarioService).atualizarMeu(any(UsuarioPatchDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 BAD REQUEST ao tentar atualizar os proprios dados com email invalido")
        void atualizarMeu_DeveRetornar400QuandoEmailInvalido() throws Exception {
            UsuarioPatchDTO patchDTOInvalido = new UsuarioPatchDTO("email_invalido_sem_arroba", Role.USER);

            mockMvc.perform(patch("/usuarios/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(patchDTOInvalido)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).atualizarMeu(any());
        }

        @Test
        @DisplayName("Deve deletar a propria conta com sucesso retornando 204 NO CONTENT e cookie de logout")
        void deletarMeu_DeveRetornar204NoContentECookieLogout() throws Exception {
            ResponseCookie cookieLogout = ResponseCookie.from("jwt", "")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();

            doNothing().when(usuarioService).deletarMeu();
            when(cookieService.gerarCookieJwtLogout()).thenReturn(cookieLogout);

            mockMvc.perform(delete("/usuarios/me"))
                    .andExpect(status().isNoContent())
                    .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                    .andExpect(cookie().exists("jwt"))
                    .andExpect(cookie().maxAge("jwt", 0))
                    .andExpect(content().string(""));

            verify(usuarioService).deletarMeu();
            verify(cookieService).gerarCookieJwtLogout();
        }
    }

    @Nested
    @DisplayName("Alteracao de Senha (/usuarios/me/senha)")
    class AlteracaoDeSenha {

        @Test
        @DisplayName("Deve alterar senha com sucesso retornando 200 OK")
        void alterarMinhaSenha_DeveRetornar200OkQuandoDadosValidos() throws Exception {
            AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaAtual123", "novaSenha123", "novaSenha123");

            lenient().when(senhaService.ehValida(anyString())).thenReturn(true);
            doNothing().when(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));

            mockMvc.perform(put("/usuarios/me/senha")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(alterarSenhaDTO)))
                    .andExpect(status().isOk());

            verify(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 BAD REQUEST ao enviar campos de senha em branco")
        void alterarMinhaSenha_DeveRetornar400QuandoCamposObrigatoriosEmBranco() throws Exception {
            AlterarSenhaDTO alterarSenhaDTOInvalido = new AlterarSenhaDTO("", "", "");

            mockMvc.perform(put("/usuarios/me/senha")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(alterarSenhaDTOInvalido)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).alterarMinhaSenha(any());
        }

        @Test
        @DisplayName("Deve retornar 400 BAD REQUEST quando o servico lancar BadRequestException")
        void alterarMinhaSenha_DeveRetornar400QuandoServiceLancarBadRequestException() throws Exception {
            AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaAtual123", "novaSenha123", "novaSenhaDiferente");

            lenient().when(senhaService.ehValida(anyString())).thenReturn(true);
            doThrow(new BadRequestException("usuario.senhas-nao-coincidem"))
                    .when(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));

            mockMvc.perform(put("/usuarios/me/senha")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(alterarSenhaDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));

            verify(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 401 UNAUTHORIZED quando a senha atual for incorreta")
        void alterarMinhaSenha_DeveRetornar401QuandoServiceLancarCredenciaisInvalidasException() throws Exception {
            AlterarSenhaDTO alterarSenhaDTO = new AlterarSenhaDTO("senhaIncorreta", "novaSenha123", "novaSenha123");

            lenient().when(senhaService.ehValida(anyString())).thenReturn(true);
            doThrow(new CredenciaisInvalidasException("auth.credenciais-invalidas"))
                    .when(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));

            mockMvc.perform(put("/usuarios/me/senha")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(alterarSenhaDTO)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401));

            verify(usuarioService).alterarMinhaSenha(any(AlterarSenhaDTO.class));
        }
    }
}
