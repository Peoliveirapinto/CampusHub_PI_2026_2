package org.example.backend.controller;

import org.junit.jupiter.api.Nested;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.example.backend.dto.auth.*;
import org.example.backend.dto.usuario.UsuarioCadastroDTO;
import org.example.backend.dto.usuario.UsuarioLoginDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.GlobalExceptionHandler;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.example.backend.service.AuthService;
import org.example.backend.service.CookieService;
import org.example.backend.service.SenhaService;
import org.example.backend.validation.SenhaValidaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private CookieService cookieService;

    @Mock
    private SenhaService senhaService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UsuarioCadastroDTO cadastroDTO;
    private UsuarioLoginDTO loginDTO;
    private UsuarioRespostaDTO usuarioRespostaDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(new jakarta.validation.ConstraintValidatorFactory() {
            @Override
            public <T extends jakarta.validation.ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
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
            public void releaseInstance(jakarta.validation.ConstraintValidator<?, ?> instance) {
            }
        });
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .setValidator(validator)
                .build();

        objectMapper = JsonMapper.builder().build();

        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setEmail("teste@exemplo.com");
        usuario.setRole(Role.USER);

        usuarioRespostaDTO = new UsuarioRespostaDTO(usuario.getId(), usuario.getEmail(), usuario.getRole());

        cadastroDTO = new UsuarioCadastroDTO("teste@exemplo.com", "senha123");
        loginDTO = new UsuarioLoginDTO("teste@exemplo.com", "senha123");

        lenient().when(messageSource.getMessage(anyString(), any(), anyString(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(2));
    }

    @Nested
    @DisplayName("Rotas de Cadastro")
    class RotasCadastro {

        @Test
        @DisplayName("Deve cadastrar admin com sucesso retornando 201 CREATED")
        void cadastrarAdmin_DeveRetornar201Created() throws Exception {
            when(authService.cadastrarAdmin(any(UsuarioCadastroDTO.class))).thenReturn(usuarioRespostaDTO);

            mockMvc.perform(post("/auth/admin/cadastro/admin")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(cadastroDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(usuario.getId().toString()))
                    .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                    .andExpect(jsonPath("$.role").value(usuario.getRole().name()));

            verify(authService).cadastrarAdmin(any(UsuarioCadastroDTO.class));
        }

        @Test
        @DisplayName("Deve cadastrar usuario com sucesso retornando 201 CREATED")
        void cadastrarUsuario_DeveRetornar201Created() throws Exception {
            when(authService.cadastrarUsuario(any(UsuarioCadastroDTO.class))).thenReturn(usuarioRespostaDTO);

            mockMvc.perform(post("/auth/cadastro")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(cadastroDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(usuario.getId().toString()))
                    .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                    .andExpect(jsonPath("$.role").value(usuario.getRole().name()));

            verify(authService).cadastrarUsuario(any(UsuarioCadastroDTO.class));
        }
    }

    @Nested
    @DisplayName("Rotas de Login e Logout")
    class RotasLoginLogout {

        @Test
        @DisplayName("Deve autenticar usuario com sucesso retornando 200 OK e cookie")
        void login_DeveRetornar200OkECookie() throws Exception {
            UsuarioETokenDTO tokenDTO = new UsuarioETokenDTO(usuario, "token-jwt");
            when(authService.autenticarUsuario(any(UsuarioLoginDTO.class))).thenReturn(tokenDTO);
            
            ResponseCookie cookie = ResponseCookie.from("jwt", "token-jwt")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            
            when(cookieService.gerarCookieJwtLogin("token-jwt")).thenReturn(cookie);
            when(usuarioMapper.criaUsuarioRespostaDTOPelaEntidade(usuario)).thenReturn(usuarioRespostaDTO);

            mockMvc.perform(post("/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk())
                    .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                    .andExpect(cookie().exists("jwt"))
                    .andExpect(jsonPath("$.id").value(usuario.getId().toString()));

            verify(authService).autenticarUsuario(any(UsuarioLoginDTO.class));
            verify(cookieService).gerarCookieJwtLogin("token-jwt");
        }

        @Test
        @DisplayName("Deve realizar logout com sucesso retornando 204 NO CONTENT e cookie")
        void logout_DeveRetornar204NoContentECookie() throws Exception {
            ResponseCookie cookie = ResponseCookie.from("jwt", "")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();

            when(cookieService.gerarCookieJwtLogout()).thenReturn(cookie);

            mockMvc.perform(post("/auth/logout"))
                    .andExpect(status().isNoContent())
                    .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                    .andExpect(cookie().exists("jwt"))
                    .andExpect(cookie().maxAge("jwt", 0));

            verify(cookieService).gerarCookieJwtLogout();
        }
    }
}
