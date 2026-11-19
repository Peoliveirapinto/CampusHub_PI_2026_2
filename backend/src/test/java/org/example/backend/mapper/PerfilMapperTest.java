package org.example.backend.mapper;

import org.example.backend.dto.perfil.PerfilCriarDTO;
import org.example.backend.dto.perfil.PerfilCriarMeuDTO;
import org.example.backend.dto.perfil.PerfilPatchDTO;
import org.example.backend.dto.perfil.PerfilRespostaDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.MappingTarget;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PerfilMapperTest {

    private PerfilMapper perfilMapper;

    @BeforeEach
    void setUp() {
        UsuarioMapper usuarioMapper = new UsuarioMapperImpl();
        perfilMapper = new PerfilMapperImpl();
        
        // Injeta manualmente a dependência que o MapStruct espera (usada pelo PerfilMapper)
        ReflectionTestUtils.setField(perfilMapper, "usuarioMapper", usuarioMapper);
    }

    @Test
    void criaEntidadePeloDTO_DeveMapearCorretamente_CriaDTO() {
        PerfilCriarDTO dto = new PerfilCriarDTO(
                UUID.randomUUID(),
                "Nome Teste",
                "Descrição Teste",
                "Curso Teste",
                "Especialização Teste",
                2023,
                1,
                "Disponibilidade Teste",
                "Contato Teste",
                "Áreas Teste"
        );

        Perfil perfil = perfilMapper.criaEntidadePeloDTO(dto);

        assertNotNull(perfil);
        assertEquals("Nome Teste", perfil.getNome());
        assertEquals("Descrição Teste", perfil.getDescricao());
        assertEquals("Curso Teste", perfil.getCurso());
        assertEquals(2023, perfil.getAnoDeIngresso());
        assertEquals("Contato Teste", perfil.getContato());
    }

    @Test
    void criaEntidadePeloDTO_DeveMapearCorretamente_CriaMeuDTO() {
        PerfilCriarMeuDTO dto = new PerfilCriarMeuDTO(
                "Nome Meu",
                "Descrição Meu",
                "Curso Meu",
                "Especialização Meu",
                2023,
                1,
                "Disponibilidade Meu",
                "Contato Meu",
                "Áreas Meu"
        );

        Perfil perfil = perfilMapper.criaEntidadePeloDTO(dto);

        assertNotNull(perfil);
        assertEquals("Nome Meu", perfil.getNome());
        assertEquals("Curso Meu", perfil.getCurso());
        assertEquals(2023, perfil.getAnoDeIngresso());
    }

    @Test
    void criaPerfilRespostaDTOPelaEntidade_DeveMapearCorretamente() {
        Usuario usuario = new Usuario("teste@exemplo.com", "senha123");
        usuario.setId(UUID.randomUUID());

        Perfil perfil = new Perfil();
        perfil.setId(UUID.randomUUID());
        perfil.setUsuario(usuario);
        perfil.setNome("Nome Perfil");
        perfil.setDescricao("Descrição Perfil");
        perfil.setCurso("Curso Perfil");
        perfil.setEspecializacao("Especialização Perfil");
        perfil.setAnoDeIngresso(2024);
        perfil.setSemestreDeIngresso(2);
        perfil.setDisponibilidade("Disponível");
        perfil.setContato("123456789");
        perfil.setAreasDeInteresse("Tecnologia");

        PerfilRespostaDTO dto = perfilMapper.criaPerfilRespostaDTOPelaEntidade(perfil);

        assertNotNull(dto);
        assertEquals(perfil.getId(), dto.id());
        assertEquals("Nome Perfil", dto.nome());
        assertEquals("Curso Perfil", dto.curso());
        assertEquals(2024, dto.anoDeIngresso());
        assertNotNull(dto.usuario());
        assertEquals("teste@exemplo.com", dto.usuario().email());
    }

    @Test
    void atualizaEntidadePeloDTO_DeveAtualizarApenasCamposPermitidos() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario("original@exemplo.com", "senha123");
        usuario.setId(id);

        Perfil perfil = new Perfil();
        perfil.setId(id);
        perfil.setUsuario(usuario);
        perfil.setNome("Nome Original");
        perfil.setDescricao("Descrição Original");
        perfil.setCurso("Curso Original");
        perfil.setEspecializacao("Especialização Original");
        perfil.setAnoDeIngresso(2023);
        perfil.setSemestreDeIngresso(1);
        perfil.setDisponibilidade("Original");
        perfil.setContato("Original");
        perfil.setAreasDeInteresse("Original");

        PerfilPatchDTO dto = new PerfilPatchDTO(
                "Novo Nome",
                "Nova Descrição",
                "Novo Curso",
                "Nova Especialização",
                2024,
                2,
                "Nova Disponibilidade",
                "Novo Contato",
                "Novas Áreas"
        );

        perfilMapper.atualizaEntidadePeloDTO(dto, perfil);

        // Campos permitidos devem mudar
        assertEquals("Novo Nome", perfil.getNome());
        assertEquals("Nova Descrição", perfil.getDescricao());
        assertEquals("Novo Curso", perfil.getCurso());
        assertEquals(2024, perfil.getAnoDeIngresso());
        assertEquals("Novo Contato", perfil.getContato());

        // Campos ignorados devem permanecer iguais
        assertEquals(id, perfil.getId());
        assertEquals(usuario, perfil.getUsuario());
    }
}
