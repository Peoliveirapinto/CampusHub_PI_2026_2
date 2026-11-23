package org.example.backend.dto.pareamento;

import org.example.backend.dto.perfil.PerfilRespostaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PareamentoDTOTest {

    @Test
    @DisplayName("Deve validar getters, equals, hashCode e toString de PareamentoRespostaDTO")
    void testPareamentoRespostaDTO() {
        UUID id1 = UUID.randomUUID();
        PerfilRespostaDTO perfil1 = new PerfilRespostaDTO(
                id1, null, "Aluno 1", "Desc", "Curso", "Esp", 2024, 1, "Noturno", "123", "Java"
        );
        PerfilRespostaDTO perfil2 = new PerfilRespostaDTO(
                id1, null, "Aluno 1", "Desc", "Curso", "Esp", 2024, 1, "Noturno", "123", "Java"
        );
        PerfilRespostaDTO perfilDiferente = new PerfilRespostaDTO(
                UUID.randomUUID(), null, "Aluno 2", "Desc", "Curso", "Esp", 2024, 1, "Noturno", "123", "Python"
        );

        PareamentoRespostaDTO dto1 = new PareamentoRespostaDTO(perfil1, 3);
        PareamentoRespostaDTO dto2 = new PareamentoRespostaDTO(perfil2, 3);
        PareamentoRespostaDTO dtoDiferente = new PareamentoRespostaDTO(perfilDiferente, 1);
        PareamentoRespostaDTO dtoDiferentePontos = new PareamentoRespostaDTO(perfil1, 2);

        // Getters
        assertEquals(perfil1, dto1.perfil());
        assertEquals(3, dto1.pontuacaoMatch());

        // Equals
        assertEquals(dto1, dto1);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dtoDiferente);
        assertNotEquals(dto1, dtoDiferentePontos);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, "outra-string");

        // HashCode
        assertEquals(dto1.hashCode(), dto2.hashCode());

        // ToString
        assertNotNull(dto1.toString());
        assertTrue(dto1.toString().contains("Aluno 1"));
        assertTrue(dto1.toString().contains("3"));
    }

    @Test
    @DisplayName("Deve validar getters, equals, hashCode e toString de PareamentoFiltroDTO")
    void testPareamentoFiltroDTO() {
        PareamentoFiltroDTO filtro1 = new PareamentoFiltroDTO("Curso A", "Noturno");
        PareamentoFiltroDTO filtro2 = new PareamentoFiltroDTO("Curso A", "Noturno");
        PareamentoFiltroDTO filtroDiferente = new PareamentoFiltroDTO("Curso B", "Diurno");
        PareamentoFiltroDTO filtroComNulos = new PareamentoFiltroDTO(null, null);

        // Getters
        assertEquals("Curso A", filtro1.curso());
        assertEquals("Noturno", filtro1.disponibilidade());
        assertNull(filtroComNulos.curso());
        assertNull(filtroComNulos.disponibilidade());

        // Equals
        assertEquals(filtro1, filtro1);
        assertEquals(filtro1, filtro2);
        assertNotEquals(filtro1, filtroDiferente);
        assertNotEquals(filtro1, filtroComNulos);
        assertNotEquals(filtro1, null);
        assertNotEquals(filtro1, "string-qualquer");

        // HashCode
        assertEquals(filtro1.hashCode(), filtro2.hashCode());

        // ToString
        assertNotNull(filtro1.toString());
        assertTrue(filtro1.toString().contains("Curso A"));
        assertTrue(filtro1.toString().contains("Noturno"));
    }
}
