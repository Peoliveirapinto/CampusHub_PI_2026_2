package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.UsuarioCadastroDTO;
import org.example.backend.dto.UsuarioRespostaDTO;
import org.example.backend.service.AuthService;
import org.springframework.http.HttpStatus;
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
}
