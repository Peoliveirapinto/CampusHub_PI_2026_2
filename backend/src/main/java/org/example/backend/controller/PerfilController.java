package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.PerfilCriarDTO;
import org.example.backend.dto.PerfilRespostaDTO;
import org.example.backend.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfis")
public class PerfilController {
    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService){
        this.perfilService = perfilService;
    }

    @PostMapping()
    public ResponseEntity<PerfilRespostaDTO> criar(@RequestBody @Valid PerfilCriarDTO perfilCriarDTO){
        PerfilRespostaDTO perfilSalvo = perfilService.criar(perfilCriarDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }
}
