package com.caigods.biblioteca_jogos.mapper;

import com.caigods.biblioteca_jogos.dto.JogoRequestDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JogoMapper {

    @Mapping(target = "id", ignore = true)
    Jogo toEntity(JogoRequestDTO dto);
}