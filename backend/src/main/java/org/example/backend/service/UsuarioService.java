package org.example.backend.service;

import org.example.backend.dto.UsuarioRespostaDTO;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
}
