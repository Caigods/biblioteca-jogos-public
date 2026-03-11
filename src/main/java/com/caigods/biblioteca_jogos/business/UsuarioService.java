package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.dto.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.exception.ConflictException;
import com.caigods.biblioteca_jogos.exception.NotFoundException;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import com.caigods.biblioteca_jogos.infrasctuture.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Usuario salvarUsuario(UsuarioRequestDTO dto) {
        emailExiste(dto.getEmail());
        Usuario usuario = new Usuario(
                null,
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha())
        );
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado no id de numero " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email não encontrado! " + email));
    }

    @Transactional
    public void deletarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("Usuario nao encontrado: " + email));
    }


    //Validacoes----------------------------------------------------------------
    public void emailExiste(String email) {
        try {
            boolean existe = verificarEmailExiste(email);
            if (existe) {
                throw new ConflictException("Email ja existente " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email ja existente " + e.getCause());
        }
    }

    public boolean verificarEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
