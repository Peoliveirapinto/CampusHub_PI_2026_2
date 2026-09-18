package org.example.backend.service;

import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getById(UUID id){
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com id: " + id)
        );
    }
}
