package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.pareamento.PareamentoFiltroDTO;
import org.example.backend.dto.pareamento.PareamentoRespostaDTO;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.PerfilMapper;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.PerfilRepository;
import org.example.backend.security.SecurityFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PareamentoService {
    private final PerfilRepository perfilRepository;
    private final UsuarioService usuarioService;
    private final PerfilMapper perfilMapper;
    private final SecurityFacade securityFacade;

    @Transactional(readOnly = true)
    public List<PareamentoRespostaDTO> obterPareamentoMeu(PareamentoFiltroDTO filtro) {
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        Perfil meuPerfil = perfilRepository.findFirstByUsuario(usuarioLogado).orElseThrow(
                () -> new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario")
        );

        return buscarMatches(meuPerfil, filtro);
    }

    @Transactional(readOnly = true)
    public List<PareamentoRespostaDTO> obterPareamentoPorUsuarioId(UUID usuarioId, PareamentoFiltroDTO filtro) {
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilAlvo = perfilRepository.findFirstByUsuario(usuario).orElseThrow(
                () -> new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario")
        );

        return buscarMatches(perfilAlvo, filtro);
    }

    private List<PareamentoRespostaDTO> buscarMatches(Perfil perfilOrigem, PareamentoFiltroDTO filtro) {
        String filtroCurso = (filtro != null && filtro.curso() != null && !filtro.curso().isBlank())
                ? filtro.curso().trim()
                : null;
        String filtroDisp = (filtro != null && filtro.disponibilidade() != null && !filtro.disponibilidade().isBlank())
                ? filtro.disponibilidade().trim()
                : null;

        List<Object[]> resultados = perfilRepository.encontrarMelhoresMatchesComPontuacao(
                perfilOrigem.getUsuario(),
                perfilOrigem.getAnoDeIngresso(),
                perfilOrigem.getSemestreDeIngresso(),
                perfilOrigem.getCurso(),
                perfilOrigem.getDisponibilidade(),
                perfilOrigem.getEspecializacao(),
                perfilOrigem.getAreasDeInteresse(),
                filtroCurso,
                filtroDisp
        );

        return resultados.stream()
                .map(row -> new PareamentoRespostaDTO(
                        perfilMapper.toPerfilRespostaDTO((Perfil) row[0]),
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }
}
