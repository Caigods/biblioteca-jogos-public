package com.caigods.biblioteca_jogos.infrasctuture.repository;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailContainingIgnoreCase(String email);

    boolean existsByEmail(String email);

}
