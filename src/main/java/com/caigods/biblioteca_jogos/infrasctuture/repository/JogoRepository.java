package com.caigods.biblioteca_jogos.infrasctuture.repository;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    List<Jogo> findByPlataformas(PlataformaJogo plataformas);

    List<Jogo> findByStatus(StatusJogo status);

    List<Jogo> findByGeneroContainingIgnoreCase (String genero);

    List<Jogo> findByTituloContainingIgnoreCase(String titulo);

    List<Jogo> findByNotaPessoalGreaterThanEqual(Double notaPessoal);

    List<Jogo> findByNotaPessoal(Double notaPessoal);

    Long countByPlataformas (PlataformaJogo plataformas);

    Boolean existsJogoByTituloAndPlataformas(String titulo, PlataformaJogo plataformas);


}
