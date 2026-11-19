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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public PerfilRespostaDTO criar(PerfilCriarDTO perfilCriarDTO){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(perfilCriarDTO.usuarioId());

        garantirQueUsuarioNaoTemPerfil(usuario);

        Perfil perfilNovo = perfilMapper.toEntity(perfilCriarDTO);
        perfilNovo.setUsuario(usuario);

        Perfil perfilSalvo = perfilRepository.save(perfilNovo);

        return perfilMapper.toPerfilRespostaDTO(perfilSalvo);
    }

    @Transactional(readOnly = true)
    public PerfilRespostaDTO obterPorUsuarioid(UUID usuarioId){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilDoUsuario = garantirQueUsuarioTemPerfil(usuario);

        return perfilMapper.toPerfilRespostaDTO(perfilDoUsuario);
    }

    @Transactional
    public void deletarPorUsuarioId(UUID usuarioId){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilParaDeletar = garantirQueUsuarioTemPerfil(usuario);

        perfilRepository.deleteById(perfilParaDeletar.getId());
    }

    @Transactional
    public PerfilRespostaDTO atualizarPorUsuarioId(UUID usuarioId, PerfilPatchDTO perfilPatchDTO){
        Usuario usuario = usuarioService.garantirQueUsuarioExiste(usuarioId);

        Perfil perfilParaAtualizar = garantirQueUsuarioTemPerfil(usuario);

        perfilMapper.updateFromDTO(perfilPatchDTO, perfilParaAtualizar);

        // Não precisa fazer o save explicito pois o @Transactional ja atualiza automaticamente
        // Perfil perfilAtualizado = perfilRepository.save(perfilParaAtualizar);

        return perfilMapper.toPerfilRespostaDTO(perfilParaAtualizar);
    }

    @Transactional
    public PerfilRespostaDTO criarMeu(PerfilCriarMeuDTO perfilCriarMeuDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        usuarioService.garantirQueUsuarioExiste(usuarioLogado.getId());
        garantirQueUsuarioNaoTemPerfil(usuarioLogado);

        Perfil perfilNovo = perfilMapper.toEntity(perfilCriarMeuDTO);
        perfilNovo.setUsuario(usuarioLogado);

        Perfil perfilSalvo = perfilRepository.save(perfilNovo);

        return perfilMapper.toPerfilRespostaDTO(perfilSalvo);
    }

    @Transactional(readOnly = true)
    public PerfilRespostaDTO obterMeu(){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        return perfilMapper.toPerfilRespostaDTO(garantirQueUsuarioTemPerfil(usuarioLogado));
    }

    @Transactional
    public PerfilRespostaDTO atualizarMeu(PerfilPatchDTO perfilPatchDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        Perfil perfilParaAtualizar = garantirQueUsuarioTemPerfil(usuarioLogado);

        perfilMapper.updateFromDTO(perfilPatchDTO, perfilParaAtualizar);

        // Não precisa fazer o save explicito pois o @Transactional ja atualiza automaticamente
        // Perfil perfilAtualizado = perfilRepository.save(perfilParaAtualizar);

        return perfilMapper.toPerfilRespostaDTO(perfilParaAtualizar);
    }

    @Transactional
    public void deletarMeu(){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        Perfil perfilParaDeletar = garantirQueUsuarioTemPerfil(usuarioLogado);

        perfilRepository.deleteById(perfilParaDeletar.getId());
    }
}
