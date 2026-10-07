package org.example.backend.controller;

import org.example.backend.dto.UsuarioRespostaDTO;
import org.example.backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> getById(@PathVariable UUID id){
        UsuarioRespostaDTO usuarioRespostaDTO = usuarioService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioRespostaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        usuarioService.deletarPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
