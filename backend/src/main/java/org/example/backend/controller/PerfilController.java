package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.PerfilCriarDTO;
import org.example.backend.dto.PerfilPatchDTO;
import org.example.backend.dto.PerfilRespostaDTO;
import org.example.backend.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/perfis")
public class PerfilController {
    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService){
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity<PerfilRespostaDTO> criar(
            @RequestBody @Valid PerfilCriarDTO perfilCriarDTO,
           Authentication authentication) {
       garantirDonoOuAdmin(authentication, perfilCriarDTO.usuarioId());
       PerfilRespostaDTO perfilSalvo = perfilService.criar(perfilCriarDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<PerfilRespostaDTO> obterPorUsuarioId(@PathVariable UUID usuarioId){
        PerfilRespostaDTO perfilDoUsuario = perfilService.obterPorUsuarioid(usuarioId);

        return ResponseEntity.status(HttpStatus.OK).body(perfilDoUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        perfilService.deletar(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{usuarioId}")
    public ResponseEntity<PerfilRespostaDTO> atualizarPorUsuarioId(
            @PathVariable UUID usuarioId,
            @RequestBody PerfilPatchDTO perfilPatchDTO,
            Authentication authentication) {
        garantirDonoOuAdmin(authentication, usuarioId);
       return ResponseEntity.ok(perfilService.atualizarPorUsuarioId(usuarioId, perfilPatchDTO));
    }

        private void garantirDonoOuAdmin(Authentication authentication, UUID usuarioId) {
        boolean admin = authentication.getAuthorities().stream()
             .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
     boolean dono = authentication.getName().equals(usuarioId.toString());
     if (!admin && !dono) {
         throw new ResponseStatusException(HttpStatus.FORBIDDEN);
     }
    }
}
