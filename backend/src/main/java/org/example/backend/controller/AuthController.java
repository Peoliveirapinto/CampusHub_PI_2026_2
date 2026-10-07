package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.UsuarioCadastroDTO;
import org.example.backend.dto.UsuarioETokenDTO;
import org.example.backend.dto.UsuarioLoginDTO;
import org.example.backend.dto.UsuarioRespostaDTO;
import org.example.backend.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro/admin")
    public ResponseEntity<UsuarioRespostaDTO> cadastrarAdmin(@RequestBody @Valid UsuarioCadastroDTO usuarioCadastroDTO){
        UsuarioRespostaDTO usuarioRespostaDTO = authService.cadastrarAdmin(usuarioCadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRespostaDTO);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioRespostaDTO> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroDTO usuarioCadastroDTO){
        UsuarioRespostaDTO usuarioRespostaDTO = authService.cadastrarUsuario(usuarioCadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRespostaDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioRespostaDTO> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO){
        // Valida as credenciais e gera o token contendo o ID e a Role do usuário
        UsuarioETokenDTO usuarioETokenDTO = authService.autenticarUsuario(usuarioLoginDTO);

        // Cria o Cookie HTTP-Only contendo o JWT
        ResponseCookie cookie = ResponseCookie.from("jwt_token", usuarioETokenDTO.token())
                .httpOnly(true)
                .secure(false) // Altere para 'true' em ambiente de Produção (exige HTTPS)
                .path("/")
                .maxAge(86400) // Validade em segundos (24 horas)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UsuarioRespostaDTO(usuarioETokenDTO.usuario()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Expira o cookie imediatamente enviando maxAge(0)
        ResponseCookie cookie = ResponseCookie.from("jwt_token", "")
                .httpOnly(true)
                .secure(false) // Altere para 'true' em Produção
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
