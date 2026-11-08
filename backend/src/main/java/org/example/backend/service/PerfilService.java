package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.perfil.*;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.PerfilMapper;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.PerfilRepository;
import org.example.backend.security.SecurityFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioService usuarioService;
    private final PerfilMapper perfilMapper;
    private final SecurityFacade securityFacade;

    private Perfil garantirQueUsuarioTemPerfil(Usuario usuario){
        return perfilRepository.findFirstByUsuario(usuario).orElseThrow(
                () -> new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario")
        );
    }

    private void garantirQueUsuarioNaoTemPerfil(Usuario usuario){
        if(perfilRepository.existsByUsuario(usuario)){
            throw new JaExisteException("perfil.esse-usuario-ja-possui-perfil");
        }
    }

    public PerfilRespostaDTO criar(PerfilCriarDTO perfilCriarDTO){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(perfilCriarDTO.usuarioId());

        garantirQueUsuarioNaoTemPerfil(usuario);

        Perfil perfilNovo = perfilMapper.criaEntidadePeloDTO(perfilCriarDTO);
        perfilNovo.setUsuario(usuario);

        Perfil perfilSalvo = perfilRepository.save(perfilNovo);

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfilSalvo);
    }

    public PerfilRespostaDTO obterPorUsuarioid(UUID usuarioId){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilDoUsuario = garantirQueUsuarioTemPerfil(usuario);

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfilDoUsuario);
    }

    public void deletarPorUsuarioId(UUID usuarioId){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilParaDeletar = garantirQueUsuarioTemPerfil(usuario);

        perfilRepository.deleteById(perfilParaDeletar.getId());
    }

    public PerfilRespostaDTO atualizarPorUsuarioId(UUID usuarioId, PerfilPatchDTO perfilPatchDTO){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilParaAtualizar = garantirQueUsuarioTemPerfil(usuario);

        perfilMapper.atualizaEntidadePeloDTO(perfilPatchDTO, perfilParaAtualizar);

        Perfil perfilAtualizado = perfilRepository.save(perfilParaAtualizar);

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfilAtualizado);
    }

    public PerfilRespostaDTO criarMeu(PerfilCriarMeuDTO perfilCriarMeuDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        usuarioService.garantirQueUsuarioExiste(usuarioLogado.getId());
        garantirQueUsuarioNaoTemPerfil(usuarioLogado);

        Perfil perfilNovo = perfilMapper.criaEntidadePeloDTO(perfilCriarMeuDTO);
        perfilNovo.setUsuario(usuarioLogado);

        Perfil perfilSalvo = perfilRepository.save(perfilNovo);

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfilSalvo);
    }

    public PerfilRespostaDTO obterMeu(){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        usuarioService.garantirQueUsuarioExiste(usuarioLogado.getId());

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(garantirQueUsuarioTemPerfil(usuarioLogado));
    }

    public PerfilRespostaDTO atualizarMeu(PerfilPatchDTO perfilPatchDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        usuarioService.garantirQueUsuarioExiste(usuarioLogado.getId());
        Perfil perfilParaAtualizar = garantirQueUsuarioTemPerfil(usuarioLogado);

        perfilMapper.atualizaEntidadePeloDTO(perfilPatchDTO, perfilParaAtualizar);

        Perfil perfilAtualizado = perfilRepository.save(perfilParaAtualizar);

        return perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfilAtualizado);
    }
}
