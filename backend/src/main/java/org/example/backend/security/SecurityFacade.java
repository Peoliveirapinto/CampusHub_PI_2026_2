package org.example.backend.security;

import lombok.RequiredArgsConstructor;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.TokenPayloadInvalidoException;
import org.example.backend.exception.UsuarioNaoAutenticadoException;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityFacade {
    private final UsuarioRepository usuarioRepository;

    public UUID obterIdDoUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof String usuarioIdStr){
            try {
                return UUID.fromString(usuarioIdStr);
            }
            catch (IllegalArgumentException e){
                throw new TokenPayloadInvalidoException("auth.token-payload-invalido");
            }
        }

        throw new UsuarioNaoAutenticadoException("auth.usuario-nao-autenticado");
    }

    public Usuario obterUsuarioLogado(){
        UUID idDoUsuarioLogado = obterIdDoUsuarioLogado();

        return usuarioRepository.findById(idDoUsuarioLogado).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", idDoUsuarioLogado)
        );
    }
}
