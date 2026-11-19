package org.example.backend.mapper;

import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.model.Usuario;
import org.example.backend.model.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    // Since MapStruct generates the implementation, we can use a helper or just use the Mapper interface 
    // if we provide the implementation. For unit tests, we can manually instantiate the implementation 
    // or use a mock/spy if needed, but usually, we just want to test the mapping logic.
    // In a standard project, we'd use the generated UsuarioMapperImpl.
    
    private final UsuarioMapper mapper = new UsuarioMapperImpl();

    @Test
    void criaUsuarioRespostaDTOPelaEntidade_DeveMapearCorretamente() {
        Usuario usuario = new Usuario("teste@exemplo.com", "senha123", Role.USER);
        usuario.setId(UUID.randomUUID());

        UsuarioRespostaDTO dto = mapper.toUsuarioRespostaDTO(usuario);

        assertNotNull(dto);
        assertEquals(usuario.getId(), dto.id());
        assertEquals(usuario.getEmail(), dto.email());
        assertEquals(usuario.getRole(), dto.role());
    }

    @Test
    void atualizaEntidadePeloDTO_DeveAtualizarApenasCamposPermitidos() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario("original@exemplo.com", "senha123", Role.USER);
        usuario.setId(id);

        // O DTO tem email e role. No entanto, o Mapper ignora email e role na atualização.
        UsuarioPatchDTO dto = new UsuarioPatchDTO("novo@exemplo.com", Role.ADMIN);

        mapper.updateFromDTO(dto, usuario);

        // Email e Role devem permanecer originais por causa do @Mapping(target = "...", ignore = true)
        assertEquals("original@exemplo.com", usuario.getEmail());
        assertEquals(Role.USER, usuario.getRole());
        // O ID também deve permanecer igual (mas ele já era igual)
        assertEquals(id, usuario.getId());
    }
}
