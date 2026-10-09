package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.PerfilCriarDTO;
import org.example.backend.dto.PerfilPatchDTO;
import org.example.backend.dto.PerfilRespostaDTO;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.PerfilMapper;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.PerfilRepository;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper perfilMapper;

    public PerfilRespostaDTO criar(PerfilCriarDTO perfilCriarDTO){
        Usuario usuario = usuarioRepository.findById(perfilCriarDTO.usuarioId()).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", perfilCriarDTO.usuarioId())
        );

        if(perfilRepository.existsByUsuario(usuario)){
            throw new JaExisteException("perfil.esse-usuario-ja-possui-perfil");
        }

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
}
