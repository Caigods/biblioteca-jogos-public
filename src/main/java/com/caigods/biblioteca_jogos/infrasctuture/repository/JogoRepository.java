package com.caigods.biblioteca_jogos.infrasctuture.repository;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    List<Jogo> findByUsuario(Usuario usuario);

    List<Jogo> findByPlataformasAndUsuario(PlataformaJogo plataformas, Usuario usuario);

    List<Jogo> findByStatusAndUsuario(StatusJogo status, Usuario usuario);

    List<Jogo> findByGeneroContainingIgnoreCaseAndUsuario(String genero, Usuario usuario);

    List<Jogo> findByTituloContainingIgnoreCaseAndUsuario(String titulo, Usuario usuario);

    List<Jogo> findByNotaPessoalGreaterThanEqualAndUsuario(Double notaPessoal, Usuario usuario);

    List<Jogo> findByNotaPessoalAndUsuario(Double notaPessoal, Usuario usuario);

    Long countByPlataformasAndUsuario(PlataformaJogo plataformas, Usuario usuario);

    Long countByUsuario(Usuario usuario);

    Boolean existsJogoByTituloAndPlataformasAndUsuario(String titulo, PlataformaJogo plataformas, Usuario usuario);

    Optional<Jogo> findByIdAndUsuario(Integer id, Usuario usuario);
}
