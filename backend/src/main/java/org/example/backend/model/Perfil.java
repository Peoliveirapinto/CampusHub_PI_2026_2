package org.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.dto.PerfilCriarDTO;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(
        name = "perfil",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_perfil_usuario_id", columnNames = "usuario_id")
        }
)
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_perfil_usuario"),
            nullable = false,
            updatable = false)
    private Usuario usuario;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "curso", nullable = false, length = 50)
    private String curso;

    @Column(name = "especializacao", length = 100)
    private String especializacao;

    @Column(name = "ano_de_ingresso", nullable = false)
    private Integer anoDeIngresso;

    @Column(name = "semestre_de_ingresso", nullable = false)
    private Integer semestreDeIngresso;

    @Column(name = "disponibilidade", nullable = false, columnDefinition = "TEXT")
    private String disponibilidade;

    @Column(name = "contato", nullable = false, length = 100)
    private String contato;

    public Perfil(PerfilCriarDTO perfilCriarDTO, Usuario usuario){
        this.usuario = usuario;
        this.nome = perfilCriarDTO.nome();
        this.descricao = perfilCriarDTO.descricao();
        this.curso = perfilCriarDTO.curso();
        this.especializacao = perfilCriarDTO.especializacao();
        this.anoDeIngresso = perfilCriarDTO.anoDeIngresso();
        this.semestreDeIngresso = perfilCriarDTO.semestreDeIngresso();
        this.disponibilidade = perfilCriarDTO.disponibilidade();
        this.contato = perfilCriarDTO.contato();
    }
}
