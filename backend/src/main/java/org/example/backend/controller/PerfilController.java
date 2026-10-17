package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.perfil.*;
import org.example.backend.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfis")
public class PerfilController {
    private final PerfilService perfilService;

    @PostMapping("/admin")
    public ResponseEntity<PerfilRespostaDTO> criar(@RequestBody @Valid PerfilCriarDTO perfilCriarDTO){
        PerfilRespostaDTO perfilSalvo = perfilService.criar(perfilCriarDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @GetMapping("/admin/{usuarioId}")
    public ResponseEntity<PerfilRespostaDTO> obterPorUsuarioId(@PathVariable UUID usuarioId){
        PerfilRespostaDTO perfilDoUsuario = perfilService.obterPorUsuarioid(usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(perfilDoUsuario);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        perfilService.deletar(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/admin/{usuarioId}")
    public ResponseEntity<PerfilRespostaDTO> atualizarPorUsuarioId(@PathVariable UUID usuarioId, @RequestBody PerfilPatchDTO perfilPatchDTO){
        PerfilRespostaDTO perfilAtualizado =  perfilService.atualizarPorUsuarioId(usuarioId, perfilPatchDTO);

        return ResponseEntity.status(HttpStatus.OK).body(perfilAtualizado);
    }

    @PostMapping("/me")
    public ResponseEntity<PerfilRespostaDTO> criarMeu(@RequestBody @Valid PerfilCriarMeuDTO perfilCriarMeuDTO){
        PerfilRespostaDTO perfilSalvo = perfilService.criarMeu(perfilCriarMeuDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }
}
