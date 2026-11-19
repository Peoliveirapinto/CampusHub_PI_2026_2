package org.example.backend.repository;

import jakarta.persistence.PersistenceContext;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import jakarta.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use H2 or a configured DB
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void save_DeveSalvarUsuarioEPermitirBuscaPorId() {
        Usuario usuario = new Usuario("teste@exemplo.com", "senha123", Role.USER);
        Usuario salvo = usuarioRepository.save(usuario);

        assertNotNull(salvo.getId());
        Optional<Usuario> recuperado = usuarioRepository.findById(salvo.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("teste@exemplo.com", recuperado.get().getEmail());
    }

    @Test
    void existsByEmail_DeveRetornarTrueQuandoEmailExiste() {
        String email = "existe@exemplo.com";
        Usuario usuario = new Usuario(email, "senha123", Role.USER);
        usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.existsByEmail(email));
    }

    @Test
    void existsByEmail_DeveRetornarFalseQuandoEmailNaoExiste() {
        assertFalse(usuarioRepository.existsByEmail("nao@exemplo.com"));
    }

    @Test
    void findByEmail_DeveRetornarUsuarioQuandoEmailExiste() {
        String email = "busca@exemplo.com";
        Usuario usuario = new Usuario(email, "senha123", Role.USER);
        usuarioRepository.save(usuario);

        Optional<Usuario> recuperado = usuarioRepository.findByEmail(email);
        assertTrue(recuperado.isPresent());
        assertEquals(email, recuperado.get().getEmail());
    }

    @Test
    void findByEmail_DeveRetornarVazioQuandoEmailNaoExiste() {
        Optional<Usuario> recuperado = usuarioRepository.findByEmail("inexistente@exemplo.com");
        assertFalse(recuperado.isPresent());
    }
}
