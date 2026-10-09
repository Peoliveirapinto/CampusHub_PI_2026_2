package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.UsuarioPatchDTO;
import org.example.backend.dto.UsuarioRespostaDTO;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRespostaDTO getById(UUID id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", id)
        );

        return new UsuarioRespostaDTO(usuario);
    }

    public void deletarPorId(UUID id){
        if(!usuarioRepository.existsById(id)){
            throw new ResourceNotFoundException("usuario.nao-encontrado", "ID", id);
        }

        usuarioRepository.deleteById(id);
    }

    public UsuarioRespostaDTO atualizarPorId(UUID id, UsuarioPatchDTO usuarioPatchDTO){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", id)
        );

        usuarioMapper.atualizaEntidadePeloDTO(usuarioPatchDTO, usuario);

        // Atualiza o email caso o campo tenha sido mandado e o email já não pertença a outro usuário
        // Atualização do email é feita manualmente (fora do mapper) pois precisa de uma validação que requer acesso ao banco de dados
        if(usuarioPatchDTO.email() != null){
            if(usuarioRepository.existsByEmail(usuarioPatchDTO.email())){
                throw new JaExisteException("usuario.email.ja-existe");
            }
            usuario.setEmail(usuarioPatchDTO.email());
        }

        return new UsuarioRespostaDTO(usuarioRepository.save(usuario));
    }
}
