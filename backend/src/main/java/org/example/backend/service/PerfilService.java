package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.PerfilCriarDTO;
import org.example.backend.dto.PerfilCriarMeuDTO;
import org.example.backend.dto.PerfilPatchDTO;
import org.example.backend.dto.PerfilRespostaDTO;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.PerfilMapper;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.PerfilRepository;
import org.example.backend.repository.UsuarioRepository;
import org.example.backend.security.SecurityFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper perfilMapper;
    private final SecurityFacade securityFacade;

    private void garanteQueUsuarioNaoTemPerfil(Usuario usuario){
        if(perfilRepository.existsByUsuario(usuario)){
            throw new JaExisteException("perfil.esse-usuario-ja-possui-perfil");
        }
    }

    public PerfilRespostaDTO criar(PerfilCriarDTO perfilCriarDTO){
        Usuario usuario = usuarioRepository.findById(perfilCriarDTO.usuarioId()).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", perfilCriarDTO.usuarioId())
        );

        garanteQueUsuarioNaoTemPerfil(usuario);

        Perfil perfilSalvo = perfilRepository.save(new Perfil(perfilCriarDTO, usuario));

        return new PerfilRespostaDTO(perfilSalvo);
    }

    public PerfilRespostaDTO obterPorUsuarioid(UUID usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", usuarioId)
        );

        var perfisEncontrados = perfilRepository.findByUsuario(usuario);

        if(perfisEncontrados == null || perfisEncontrados.isEmpty()){
            throw new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario");
        }

        return new PerfilRespostaDTO(perfisEncontrados.getFirst());
    }

    public void deletar(UUID id){
        if(!perfilRepository.existsById(id)){
            throw new ResourceNotFoundException("perfil.nao-encontrado", "ID", id);
        }

        perfilRepository.deleteById(id);
    }

    public PerfilRespostaDTO atualizarPorUsuarioId(UUID usuarioId, PerfilPatchDTO perfilPatchDTO){
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", usuarioId)
        );

        var perfisEncontrados = perfilRepository.findByUsuario(usuario);

        if(perfisEncontrados == null || perfisEncontrados.isEmpty()){
            throw new ResourceNotFoundException("perfil.nao-encontrado-para-esse-usuario");
        }

        Perfil perfilParaAtualizar = perfisEncontrados.getFirst();
        perfilMapper.atualizaEntidadePeloDTO(perfilPatchDTO, perfilParaAtualizar);

        return new PerfilRespostaDTO(perfilRepository.save(perfilParaAtualizar));
    }

    public PerfilRespostaDTO criarMeu(PerfilCriarMeuDTO perfilCriarMeuDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        garanteQueUsuarioNaoTemPerfil(usuarioLogado);

        Perfil perfilNovo = perfilMapper.criaEntidadePeloDTO(perfilCriarMeuDTO);
        perfilNovo.setUsuario(usuarioLogado);

        Perfil perfilSalvo = perfilRepository.save(perfilNovo);

        return new PerfilRespostaDTO(perfilSalvo);
    }
}
