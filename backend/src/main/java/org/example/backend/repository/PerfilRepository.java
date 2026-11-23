package org.example.backend.repository;

import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, UUID> {
    boolean existsByUsuario(Usuario usuario);
    Optional<Perfil> findFirstByUsuario(Usuario usuario);

    @Query("""
        SELECT p, (
            (CASE WHEN LOWER(p.curso) = LOWER(:curso) THEN 1 ELSE 0 END) +
            (CASE WHEN LOWER(p.disponibilidade) = LOWER(:disponibilidade) THEN 1 ELSE 0 END) +
            (CASE WHEN :especializacao IS NOT NULL AND p.especializacao IS NOT NULL AND LOWER(p.especializacao) = LOWER(:especializacao) THEN 1 ELSE 0 END) +
            (CASE WHEN LOWER(p.areasDeInteresse) LIKE LOWER(CONCAT('%', :areasDeInteresse, '%'))
                    OR LOWER(:areasDeInteresse) LIKE LOWER(CONCAT('%', p.areasDeInteresse, '%')) THEN 1 ELSE 0 END)
        )
        FROM Perfil p
        WHERE p.usuario != :usuario
          AND p.anoDeIngresso = :anoDeIngresso
          AND p.semestreDeIngresso = :semestreDeIngresso
          AND (:filtroCurso IS NULL OR LOWER(p.curso) = LOWER(:filtroCurso))
          AND (:filtroDisponibilidade IS NULL OR LOWER(p.disponibilidade) = LOWER(:filtroDisponibilidade))
        ORDER BY (
            (CASE WHEN LOWER(p.curso) = LOWER(:curso) THEN 1 ELSE 0 END) +
            (CASE WHEN LOWER(p.disponibilidade) = LOWER(:disponibilidade) THEN 1 ELSE 0 END) +
            (CASE WHEN :especializacao IS NOT NULL AND p.especializacao IS NOT NULL AND LOWER(p.especializacao) = LOWER(:especializacao) THEN 1 ELSE 0 END) +
            (CASE WHEN LOWER(p.areasDeInteresse) LIKE LOWER(CONCAT('%', :areasDeInteresse, '%'))
                    OR LOWER(:areasDeInteresse) LIKE LOWER(CONCAT('%', p.areasDeInteresse, '%')) THEN 1 ELSE 0 END)
        ) DESC, p.nome ASC
    """)
    List<Object[]> encontrarMelhoresMatchesComPontuacao(
            @Param("usuario") Usuario usuario,
            @Param("anoDeIngresso") Integer anoDeIngresso,
            @Param("semestreDeIngresso") Integer semestreDeIngresso,
            @Param("curso") String curso,
            @Param("disponibilidade") String disponibilidade,
            @Param("especializacao") String especializacao,
            @Param("areasDeInteresse") String areasDeInteresse,
            @Param("filtroCurso") String filtroCurso,
            @Param("filtroDisponibilidade") String filtroDisponibilidade
    );
}
