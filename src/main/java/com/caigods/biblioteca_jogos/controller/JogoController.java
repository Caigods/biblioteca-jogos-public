package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.JogoService;
import com.caigods.biblioteca_jogos.dto.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.JogoResponseDTO;
import com.caigods.biblioteca_jogos.dto.JogoUpdateDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
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

@Validated
@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    public ResponseEntity<JogoResponseDTO> salvarJogo(
            @RequestBody @Valid JogoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jogoService.salvarJogo(dto, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<JogoResponseDTO>> listarJogos(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listaJogos(userDetails.getUsername()));
    }

    @GetMapping("/quantidade/total")
    public ResponseEntity<Long> listarQtdJogos(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarQtdJogos(userDetails.getUsername()));
    }

    @GetMapping("/quantidade/plataformas")
    public ResponseEntity<Long> listarQtdJogosPorPlataforma(
            @RequestParam @NotNull PlataformaJogo plataformas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarQtdPorPlataforma(plataformas, userDetails.getUsername()));
    }

    @GetMapping("/nota_pessoal_min")
    public ResponseEntity<List<JogoResponseDTO>> listarNotaPessoalMaiorQue(
            @RequestParam @Min(0) @Max(10) Double notaPessoal,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.listarNotaPessoalMinima(notaPessoal, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> buscarJogoPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorId(id, userDetails.getUsername()));
    }

    @GetMapping("/titulo")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorTitulo(
            @RequestParam @NotBlank String titulo,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorTitulo(titulo, userDetails.getUsername()));
    }

    @GetMapping("/plataforma")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorPlataformas(
            @RequestParam @NotNull(message = "Informe a plataforma") PlataformaJogo plataformas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorPlataformas(plataformas, userDetails.getUsername()));
    }

    @GetMapping("/genero")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorGenero(
            @RequestParam String genero,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorGenero(genero, userDetails.getUsername()));
    }

    @GetMapping("/status")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorStatus(
            @RequestParam @NotNull(message = "Status deve ser informado") StatusJogo status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorStatus(status, userDetails.getUsername()));
    }

    @GetMapping("/nota-pessoal")
    public ResponseEntity<List<JogoResponseDTO>> buscarPorNotaPessoal(
            @RequestParam Double notaPessoal,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.buscarPorNotaPessoal(notaPessoal, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> atualizarJogoPorId(
            @PathVariable Integer id,
            @RequestBody JogoUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.atualizarJogoPorId(id, dto, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/adicionar-horas")
    public ResponseEntity<JogoResponseDTO> adicionarHorasJogadasPorId(
            @PathVariable Integer id,
            @RequestParam Double horasJogadas,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.adicionarHorasJogadasPorId(id, horasJogadas, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/atualizar-status")
    public ResponseEntity<JogoResponseDTO> atualizarStatusPorId(
            @PathVariable Integer id,
            @RequestParam StatusJogo statusJogo,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(jogoService.atualizarStatusPorId(id, statusJogo, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJogoPorId(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        jogoService.deletarJogoPorId(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}