package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.pareamento.PareamentoFiltroDTO;
import org.example.backend.dto.pareamento.PareamentoRespostaDTO;
import org.example.backend.service.PareamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pareamento")
public class PareamentoController {
    private final PareamentoService pareamentoService;

    @GetMapping("/me")
    public ResponseEntity<List<PareamentoRespostaDTO>> obterMeuPareamento(
            @ModelAttribute PareamentoFiltroDTO filtro
    ) {
        List<PareamentoRespostaDTO> pareamentos = pareamentoService.obterPareamentoMeu(filtro);
        return ResponseEntity.status(HttpStatus.OK).body(pareamentos);
    }

    @GetMapping("/admin/{usuarioId}")
    public ResponseEntity<List<PareamentoRespostaDTO>> obterPareamentoPorUsuarioId(
            @PathVariable UUID usuarioId,
            @ModelAttribute PareamentoFiltroDTO filtro
    ) {
        List<PareamentoRespostaDTO> pareamentos = pareamentoService.obterPareamentoPorUsuarioId(usuarioId, filtro);
        return ResponseEntity.status(HttpStatus.OK).body(pareamentos);
    }
}
