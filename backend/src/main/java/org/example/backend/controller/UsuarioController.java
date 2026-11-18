package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.usuario.AlterarSenhaDTO;
import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.service.CookieService;
import org.example.backend.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final CookieService cookieService;

    @GetMapping("/admin/{id}")
    public ResponseEntity<UsuarioRespostaDTO> getById(@PathVariable UUID id){
        UsuarioRespostaDTO usuarioEncontrado = usuarioService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioEncontrado);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        usuarioService.deletarPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioPatchDTO usuarioPatchDTO){
        UsuarioRespostaDTO usuarioAtualizado = usuarioService.atualizarPorId(id, usuarioPatchDTO);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioRespostaDTO> obterMeu(){
        UsuarioRespostaDTO usuarioEncontrado = usuarioService.obterMeu();

        return ResponseEntity.status(HttpStatus.OK).body(usuarioEncontrado);
    }

    @PatchMapping("/me")
    public ResponseEntity<UsuarioRespostaDTO> atualizarMeu(@RequestBody @Valid UsuarioPatchDTO usuarioPatchDTO){
        UsuarioRespostaDTO usuarioAtualizado = usuarioService.atualizarMeu(usuarioPatchDTO);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletarMeu(){
        usuarioService.deletarMeu();

        // Da logout no usuario que acabou de se auto deletar
        ResponseCookie cookie = cookieService.gerarCookieJwtLogout();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PutMapping("/me/senha")
    public ResponseEntity<Void> alterarMinhaSenha(@RequestBody @Valid AlterarSenhaDTO alterarSenhaDTO){
        usuarioService.alterarMinhaSenha(alterarSenhaDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
