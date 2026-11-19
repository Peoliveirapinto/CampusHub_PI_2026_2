package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.usuario.AlterarSenhaDTO;
import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.exception.BadRequestException;
import org.example.backend.exception.JaExisteException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.mapper.UsuarioMapper;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.example.backend.security.SecurityFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final SecurityFacade securityFacade;
    private final SenhaService senhaService;

    public Usuario garantirQueUsuarioExiste(UUID id){
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("usuario.nao-encontrado", "ID", id)
        );
    }

    @Transactional(readOnly = true)
    public UsuarioRespostaDTO obterPorId(UUID id){
        Usuario usuario = garantirQueUsuarioExiste(id);

        return usuarioMapper.toUsuarioRespostaDTO(usuario);
    }

    @Transactional
    public void deletarPorId(UUID id){
        garantirQueUsuarioExiste(id);

        usuarioRepository.deleteById(id);
    }

    @Transactional
    public UsuarioRespostaDTO atualizarPorId(UUID id, UsuarioPatchDTO usuarioPatchDTO){
        Usuario usuario = garantirQueUsuarioExiste(id);

        usuarioMapper.updateFromDTO(usuarioPatchDTO, usuario);

        // Atualiza o email caso o campo tenha sido mandado e o email já não pertença a outro usuário
        // Atualização do email é feita manualmente (fora do mapper) pois precisa de uma validação que requer acesso ao banco de dados
        if(usuarioPatchDTO.email() != null){
            if(usuarioRepository.existsByEmail(usuarioPatchDTO.email())){
                throw new JaExisteException("usuario.email.ja-existe");
            }
            usuario.setEmail(usuarioPatchDTO.email());
        }

        // Não precisa fazer o save explicito pois o @Transactional ja atualiza automaticamente
        // Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioRespostaDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioRespostaDTO obterMeu(){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        return usuarioMapper.toUsuarioRespostaDTO(usuarioLogado);
    }

    @Transactional
    public UsuarioRespostaDTO atualizarMeu(UsuarioPatchDTO usuarioPatchDTO){
        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        usuarioMapper.updateFromDTO(usuarioPatchDTO, usuarioLogado);

        if(usuarioPatchDTO.email() != null){
            if(usuarioRepository.existsByEmail(usuarioPatchDTO.email())){
                throw new JaExisteException("usuario.email.ja-existe");
            }
            usuarioLogado.setEmail(usuarioPatchDTO.email());
        }

        // Não precisa fazer o save explicito pois o @Transactional ja atualiza automaticamente
        // Usuario usuarioSalvo = usuarioRepository.save(usuarioLogado);

        return usuarioMapper.toUsuarioRespostaDTO(usuarioLogado);
    }

    @Transactional
    public void deletarMeu(){
        Usuario usuarioParaDeletar = securityFacade.obterUsuarioLogado();

        usuarioRepository.deleteById(usuarioParaDeletar.getId());
    }

    @Transactional
    public void alterarMinhaSenha(AlterarSenhaDTO alterarSenhaDTO){
        if(!alterarSenhaDTO.novaSenha().equals(alterarSenhaDTO.confirmacaoNovaSenha())){
            throw new BadRequestException("usuario.senha-nova.nao-sao-iguais");
        }

        Usuario usuarioLogado = securityFacade.obterUsuarioLogado();

        senhaService.garantirQueASenhaEstaCerta(alterarSenhaDTO.senhaAtual(), usuarioLogado.getSenha());

        String senhaHasheada = senhaService.hash(alterarSenhaDTO.novaSenha());
        usuarioLogado.setSenha(senhaHasheada);

        // Não precisa fazer o save explicito pois o @Transactional ja atualiza automaticamente
        // usuarioRepository.save(usuarioLogado);
    }
}
