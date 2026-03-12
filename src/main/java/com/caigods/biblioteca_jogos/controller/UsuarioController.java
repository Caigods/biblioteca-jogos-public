package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.UsuarioService;
import com.caigods.biblioteca_jogos.dto.UsuarioLoginDTO;
import com.caigods.biblioteca_jogos.dto.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.dto.UsuarioResponseDTO;
import com.caigods.biblioteca_jogos.infrasctuture.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;



    public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager= authenticationManager;
        this.jwtUtil= jwtUtil;

    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.salvarUsuario(dto));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioLoginDTO usuarioLogindto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioLogindto.getEmail(),
                        usuarioLogindto.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }


    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listaUsuarios());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email){
       usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
