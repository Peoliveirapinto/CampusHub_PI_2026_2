package org.example.backend.repository;

import org.example.backend.model.Perfil;
import org.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, UUID> {
    boolean existsByUsuario(Usuario usuario);
}
