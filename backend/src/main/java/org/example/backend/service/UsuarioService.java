package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
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

    public Usuario garantirQueUsuarioExiste(UUID id){
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", id)
        );
    }

    public UsuarioRespostaDTO obterPorId(UUID id){
        Usuario usuario = garantirQueUsuarioExiste(id);

        return usuarioMapper.criaUsuarioRespostaDTOPelaEntidade(usuario);
    }

    public void deletarPorId(UUID id){
        garantirQueUsuarioExiste(id);

        usuarioRepository.deleteById(id);
    }

    public UsuarioRespostaDTO atualizarPorId(UUID id, UsuarioPatchDTO usuarioPatchDTO){
        Usuario usuario = garantirQueUsuarioExiste(id);

        usuarioMapper.atualizaEntidadePeloDTO(usuarioPatchDTO, usuario);

        // Atualiza o email caso o campo tenha sido mandado e o email já não pertença a outro usuário
        // Atualização do email é feita manualmente (fora do mapper) pois precisa de uma validação que requer acesso ao banco de dados
        if(usuarioPatchDTO.email() != null){
            if(usuarioRepository.existsByEmail(usuarioPatchDTO.email())){
                throw new JaExisteException("usuario.email.ja-existe");
            }
            usuario.setEmail(usuarioPatchDTO.email());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.criaUsuarioRespostaDTOPelaEntidade(usuarioSalvo);
    }
}
