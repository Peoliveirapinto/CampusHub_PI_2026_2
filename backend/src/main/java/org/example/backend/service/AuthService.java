package org.example.backend.service;

import org.example.backend.dto.UsuarioCadastroDTO;
import org.example.backend.dto.UsuarioLoginDTO;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.example.backend.exception.EmailJaExisteException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final SenhaService senhaService;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, SenhaService senhaService, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.senhaService = senhaService;
        this.jwtService = jwtService;
    }

    public void cadastrarUsuario(UsuarioCadastroDTO usuarioCadastroDTO){
        if(usuarioRepository.existsByEmail(usuarioCadastroDTO.email())){
            throw new EmailJaExisteException("usuario.email.ja-existe");
        }

        String senhaHasheada = senhaService.hash(usuarioCadastroDTO.senha());

        Usuario usuario = new Usuario(usuarioCadastroDTO.email(), senhaHasheada);

        usuarioRepository.save(usuario);
    }

    public String autenticarUsuario(UsuarioLoginDTO usuarioLoginDTO){
        Usuario usuario = usuarioRepository.findByEmail(usuarioLoginDTO.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("auth.credenciais-invalidas"));

        if (!senhaService.matchesTheHash(usuarioLoginDTO.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("auth.credenciais-invalidas");
        }

        // Retorna o token gerado pelo JwtService
        return jwtService.gerarToken(usuario);
    }
}
