package com.caigods.biblioteca_jogos.mapper;

import com.caigods.biblioteca_jogos.dto.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.dto.UsuarioResponseDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);
}