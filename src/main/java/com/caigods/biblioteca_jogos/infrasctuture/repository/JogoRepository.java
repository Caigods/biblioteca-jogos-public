package com.caigods.biblioteca_jogos.infrasctuture.repository;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    List<Jogo> findByPlataformaContainingIgnoreCase(String plataforma);

    List<Jogo> findByStatusContainingIgnoreCase(StatusJogo status);

    List<Jogo> findByGeneroContainingIgnoreCase (String genero);

    List<Jogo> findByTituloContainingIgnoreCase(String titulo);

}
