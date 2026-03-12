package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.dto.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.dto.UsuarioResponseDTO;
import com.caigods.biblioteca_jogos.exception.ConflictException;
import com.caigods.biblioteca_jogos.exception.NotFoundException;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import com.caigods.biblioteca_jogos.infrasctuture.repository.UsuarioRepository;
import com.caigods.biblioteca_jogos.mapper.UsuarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioResponseDTO> listaUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new NotFoundException("Nenhum usuário encontrado ");
        }
        return usuarios
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }


    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        emailExiste(dto.getEmail());
        dto.setEmail(dto.getEmail().toLowerCase());
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado no id de numero " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não encontrado: " + email));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public void deletarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("Usuário nao encontrado: " + email));
        usuarioRepository.delete(usuario);
    }


    //Validações----------------------------------------------------------------

    public void emailExiste(String email) {
        boolean existe = verificarEmailExiste(email);
        if (existe) {
            throw new ConflictException("Email ja existente: " + email);
        }
    }


    public boolean verificarEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
