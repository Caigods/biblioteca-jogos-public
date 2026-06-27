package com.caigods.biblioteca_jogos.business.converter;

import com.caigods.biblioteca_jogos.dto.in.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.out.JogoResponseDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JogoConverter {

    public Jogo toEntity(JogoRequestDTO dto) {
        Jogo jogo = new Jogo();
        jogo.setTitulo(dto.getTitulo());
        jogo.setPlataformas(dto.getPlataformas());
        jogo.setGenero(dto.getGenero());
        jogo.setAnoDeLancamento(dto.getAnoDeLancamento());
        jogo.setStatus(dto.getStatus());
        jogo.setNotaPessoal(dto.getNotaPessoal());
        jogo.setHorasJogadas(dto.getHorasJogadas());
        return jogo;
    }

    public JogoResponseDTO toResponseDTO(Jogo jogo) {
        return new JogoResponseDTO(
                jogo.getId(),
                jogo.getTitulo(),
                jogo.getPlataformas(),
                jogo.getGenero(),
                jogo.getAnoDeLancamento(),
                jogo.getStatus(),
                jogo.getNotaPessoal(),
                jogo.getHorasJogadas()
        );
    }

    public List<JogoResponseDTO> toListResponseDTO(List<Jogo> jogos) {
        return jogos.stream().map(this::toResponseDTO).toList();
    }
}
