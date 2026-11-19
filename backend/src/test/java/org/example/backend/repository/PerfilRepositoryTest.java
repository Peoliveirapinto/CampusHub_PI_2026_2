package org.example.backend.repository;

import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.example.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PerfilRepositoryTest {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void save_DeveSalvarPerfilEPermitirBuscaPorId() {
        Usuario usuario = new Usuario("teste@exemplo.com", "senha123");
        usuarioRepository.save(usuario);

        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNome("Nome Perfil");
        perfil.setDescricao("Descrição Perfil");
        perfil.setCurso("Curso Perfil");
        perfil.setAnoDeIngresso(2024);
        perfil.setSemestreDeIngresso(1);
        perfil.setDisponibilidade("Disponível");
        perfil.setContato("123456789");
        perfil.setAreasDeInteresse("Tecnologia");

        Perfil salvo = perfilRepository.save(perfil);

        assertNotNull(salvo.getId());
        Optional<Perfil> recuperado = perfilRepository.findById(salvo.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("Nome Perfil", recuperado.get().getNome());
    }

    @Test
    void existsByUsuario_DeveRetornarTrueQuandoUsuarioTemPerfil() {
        Usuario usuario = new Usuario("existe@exemplo.com", "senha123");
        usuarioRepository.save(usuario);

        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNome("Perfil");
        perfil.setDescricao("Desc");
        perfil.setCurso("Curso");
        perfil.setAnoDeIngresso(2023);
        perfil.setSemestreDeIngresso(1);
        perfil.setDisponibilidade("Sim");
        perfil.setContato("999");
        perfil.setAreasDeInteresse("Interesse");
        
        perfilRepository.save(perfil);

        assertTrue(perfilRepository.existsByUsuario(usuario));
    }

    @Test
    void existsByUsuario_DeveRetornarFalseQuandoUsuarioNaoTemPerfil() {
        Usuario usuario = new Usuario("nao@exemplo.com", "senha123");
        usuarioRepository.save(usuario);
        // Não salvar perfil

        assertFalse(perfilRepository.existsByUsuario(usuario));
    }

    @Test
    void findFirstByUsuario_DeveRetornarOPrimeiroPerfilDoUsuario() {
        Usuario usuario = new Usuario("busca@exemplo.com", "senha123");
        usuarioRepository.save(usuario);

        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setNome("Perfil 1");
        perfil.setDescricao("Desc 1");
        perfil.setCurso("Curso 1");
        perfil.setAnoDeIngresso(2023);
        perfil.setSemestreDeIngresso(1);
        perfil.setDisponibilidade("Sim");
        perfil.setContato("999");
        perfil.setAreasDeInteresse("Interesse");

        perfilRepository.save(perfil);

        Optional<Perfil> recuperado = perfilRepository.findFirstByUsuario(usuario);
        assertTrue(recuperado.isPresent());
        assertEquals("Perfil 1", recuperado.get().getNome());
    }
}
