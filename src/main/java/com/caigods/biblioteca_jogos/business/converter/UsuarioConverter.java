package com.caigods.biblioteca_jogos.business.converter;

import com.caigods.biblioteca_jogos.dto.in.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.dto.out.UsuarioResponseDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public List<UsuarioResponseDTO> toListResponseDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(this::toResponseDTO).toList();
    }
}
