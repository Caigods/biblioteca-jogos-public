package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.JogoService;
import com.caigods.biblioteca_jogos.dto.in.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.out.JogoResponseDTO;
import com.caigods.biblioteca_jogos.dto.JogoUpdateDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Jogos", description = "Cadastro e gerenciamento da biblioteca de jogos")
@Validated
@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    @Operation(summary = "Salvar jogo", description = "Cadastra um novo jogo para o usuario autenticado")
    @ApiResponse(responseCode = "201", description = "Jogo cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "409", description = "Jogo ja cadastrado nessa plataforma")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<JogoResponseDTO> salvarJogo(
            @RequestBody @Valid JogoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jogoService.salvarJogo(dto, userDetails.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Listar jogos", description = "Lista todos os jogos do usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Lista feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> listarJogos(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listaJogos(userDetails.getUsername()));
    }

    @GetMapping("/quantidade/total")
    @Operation(summary = "Contar jogos", description = "Retorna a quantidade total de jogos do usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Quantidade retornada com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Long> listarQtdJogos(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarQtdJogos(userDetails.getUsername()));
    }

    @GetMapping("/quantidade/plataformas")
    @Operation(summary = "Contar jogos por plataforma", description = "Retorna a quantidade de jogos do usuario autenticado por plataforma")
    @ApiResponse(responseCode = "200", description = "Quantidade retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Plataforma invalida")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Long> listarQtdJogosPorPlataforma(
            @RequestParam @NotNull PlataformaJogo plataformas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarQtdPorPlataforma(plataformas, userDetails.getUsername()));
    }

    @GetMapping("/nota_pessoal_min")
    @Operation(summary = "Listar jogos por nota minima", description = "Lista jogos com nota pessoal maior ou igual a nota informada")
    @ApiResponse(responseCode = "200", description = "Lista feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Nota invalida")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> listarNotaPessoalMaiorQue(
            @RequestParam @Min(0) @Max(10) Double notaPessoal,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarNotaPessoalMinima(notaPessoal, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogo por ID", description = "Busca um jogo do usuario autenticado pelo ID")
    @ApiResponse(responseCode = "200", description = "Jogo encontrado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Jogo nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<JogoResponseDTO> buscarJogoPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorId(id, userDetails.getUsername()));
    }

    @GetMapping("/titulo")
    @Operation(summary = "Buscar jogos por titulo", description = "Busca jogos do usuario autenticado por titulo")
    @ApiResponse(responseCode = "200", description = "Busca feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Titulo invalido")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorTitulo(
            @RequestParam @NotBlank String titulo,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorTitulo(titulo, userDetails.getUsername()));
    }

    @GetMapping("/plataforma")
    @Operation(summary = "Buscar jogos por plataforma", description = "Busca jogos do usuario autenticado por plataforma")
    @ApiResponse(responseCode = "200", description = "Busca feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Plataforma invalida")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorPlataformas(
            @RequestParam @NotNull(message = "Informe a plataforma") PlataformaJogo plataformas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorPlataformas(plataformas, userDetails.getUsername()));
    }

    @GetMapping("/genero")
    @Operation(summary = "Buscar jogos por genero", description = "Busca jogos do usuario autenticado por genero")
    @ApiResponse(responseCode = "200", description = "Busca feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorGenero(
            @RequestParam String genero,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorGenero(genero, userDetails.getUsername()));
    }

    @GetMapping("/status")
    @Operation(summary = "Buscar jogos por status", description = "Busca jogos do usuario autenticado por status")
    @ApiResponse(responseCode = "200", description = "Busca feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Status invalido")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorStatus(
            @RequestParam @NotNull(message = "Status deve ser informado") StatusJogo status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorStatus(status, userDetails.getUsername()));
    }

    @GetMapping("/nota-pessoal")
    @Operation(summary = "Buscar jogos por nota pessoal", description = "Busca jogos do usuario autenticado pela nota pessoal exata")
    @ApiResponse(responseCode = "200", description = "Busca feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Nota invalida")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Nenhum jogo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorNotaPessoal(
            @RequestParam Double notaPessoal,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorNotaPessoal(notaPessoal, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogo", description = "Atualiza os dados de um jogo do usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Jogo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Jogo nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<JogoResponseDTO> atualizarJogoPorId(
            @PathVariable Integer id,
            @RequestBody JogoUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.atualizarJogoPorId(id, dto, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/adicionar-horas")
    @Operation(summary = "Adicionar horas jogadas", description = "Adiciona horas jogadas a um jogo do usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Horas adicionadas com sucesso")
    @ApiResponse(responseCode = "400", description = "Horas invalidas")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Jogo nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<JogoResponseDTO> adicionarHorasJogadasPorId(
            @PathVariable Integer id,
            @RequestParam Double horasJogadas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.adicionarHorasJogadasPorId(id, horasJogadas, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/atualizar-status")
    @Operation(summary = "Atualizar status do jogo", description = "Atualiza o status de um jogo do usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Status invalido")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Jogo nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<JogoResponseDTO> atualizarStatusPorId(
            @PathVariable Integer id,
            @RequestParam StatusJogo statusJogo,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.atualizarStatusPorId(id, statusJogo, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar jogo por ID", description = "Deleta um jogo do usuario autenticado pelo ID")
    @ApiResponse(responseCode = "204", description = "Jogo deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    @ApiResponse(responseCode = "404", description = "Jogo nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<Void> deletarJogoPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        jogoService.deletarJogoPorId(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
