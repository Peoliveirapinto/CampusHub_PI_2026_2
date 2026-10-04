package org.example.backend.service;

import org.example.backend.dto.UsuarioCadastroDTO;
import org.example.backend.exception.EmailJaExisteException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final SenhaService senhaService;

    public AuthService(UsuarioRepository usuarioRepository, SenhaService senhaService) {
        this.usuarioRepository = usuarioRepository;
        this.senhaService = senhaService;
    }

    public void cadastrarUsuario(UsuarioCadastroDTO usuarioDTO){
        if(usuarioRepository.existsByEmail(usuarioDTO.email())){
            throw new EmailJaExisteException("usuario.email.ja-existe");
        }

        String senhaHasheada = senhaService.hash(usuarioDTO.senha());

        Usuario usuario = new Usuario(usuarioDTO.email(), senhaHasheada);

        usuarioRepository.save(usuario);
    }
}
