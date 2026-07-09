package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.UsuarioService;
import com.caigods.biblioteca_jogos.dto.UsuarioLoginDTO;
import com.caigods.biblioteca_jogos.dto.in.UsuarioRequestDTO;
import com.caigods.biblioteca_jogos.dto.out.UsuarioResponseDTO;
import com.caigods.biblioteca_jogos.infrasctuture.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuario", description = "Cadastro, login e gerenciamento de usuarios")
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
    @Operation(summary = "Salvar usuario", description = "Cadastra um novo usuario")
    @ApiResponse(responseCode = "200", description = "Usuario salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @ApiResponse(responseCode = "409", description = "Email ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.salvarUsuario(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica o usuario e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Login feito com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public String login(@RequestBody UsuarioLoginDTO usuarioLogindto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioLogindto.getEmail(),
                        usuarioLogindto.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }


    @GetMapping("/{email}")
    @Operation(summary = "Buscar usuario por email", description = "Busca um usuario pelo email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listaUsuarios());
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar usuario por email", description = "Deleta um usuario pelo email")
    @ApiResponse(responseCode = "200", description = "Usuario deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email){
       usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
