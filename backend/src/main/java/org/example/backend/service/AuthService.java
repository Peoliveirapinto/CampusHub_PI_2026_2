package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.auth.*;
import org.example.backend.dto.usuario.UsuarioCadastroDTO;
import org.example.backend.dto.usuario.UsuarioLoginDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.CredenciaisInvalidasException;
import org.example.backend.exception.JaExisteException;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final SenhaService senhaService;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRespostaDTO cadastrarAdmin(UsuarioCadastroDTO usuarioCadastroDTO){
        if(usuarioRepository.existsByEmail(usuarioCadastroDTO.email())){
            throw new JaExisteException("usuario.email.ja-existe");
        }

        String senhaHasheada = senhaService.hash(usuarioCadastroDTO.senha());

        Usuario usuarioSalvo = usuarioRepository.save(new Usuario(usuarioCadastroDTO.email(), senhaHasheada, Role.ADMIN));

        return usuarioMapper.criaUsuarioRespostaDTOPelaEntidade(usuarioSalvo);
    }

    public UsuarioRespostaDTO cadastrarUsuario(UsuarioCadastroDTO usuarioCadastroDTO){
        if(usuarioRepository.existsByEmail(usuarioCadastroDTO.email())){
            throw new JaExisteException("usuario.email.ja-existe");
        }

        String senhaHasheada = senhaService.hash(usuarioCadastroDTO.senha());

        Usuario usuarioSalvo = usuarioRepository.save(new Usuario(usuarioCadastroDTO.email(), senhaHasheada));

        return usuarioMapper.criaUsuarioRespostaDTOPelaEntidade(usuarioSalvo);
    }

    public UsuarioETokenDTO autenticarUsuario(UsuarioLoginDTO usuarioLoginDTO){
        Usuario usuario = usuarioRepository.findByEmail(usuarioLoginDTO.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("auth.credenciais-invalidas"));

        if (!senhaService.matchesTheHash(usuarioLoginDTO.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("auth.credenciais-invalidas");
        }

        // Retorna o usuario e o token gerado pelo JwtService
        return new UsuarioETokenDTO(usuario, jwtService.gerarToken(usuario));
    }
}
