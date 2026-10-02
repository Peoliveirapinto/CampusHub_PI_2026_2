package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.UsuarioCadastroDTO;
import org.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastraUsuario(@RequestBody @Valid UsuarioCadastroDTO usuarioDTO){
        authService.cadastrarUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
