package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.JogoService;
import com.caigods.biblioteca_jogos.dto.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.JogoUpdateDTO;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Validated
@RestController
@RequestMapping("/jogos")
public class JogoController {
    //Injeção do Service no controller via constructor
    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    //SALVAR
    //@Valid para validar as anotações na Entity (@min,@max,@notblank...)
    @PostMapping
    public ResponseEntity<Jogo> salvarJogo(@RequestBody @Valid JogoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoService.salvarJogo(dto));
    }


    //READ------------------------------------------------------------------------------------------------
    //LISTAR TODOS OS JOGOS
    @GetMapping
    public ResponseEntity<List<Jogo>> listarJogos() {
        return ResponseEntity.ok(jogoService.listaJogos());
    }

    //LISTAR QUANTIDADE DE JOGOS CADASTRADOS
    @GetMapping("/quantidade/total")
    public ResponseEntity<Long> listarQtdJogos() {
        return ResponseEntity.ok(jogoService.listarQtdJogos());
    }

    //LISTAR QUANTIDADE DE JOGOS POR PLATAFORMA
    @GetMapping("/quantidade/plataformas")
    public ResponseEntity<Long> listarQtdJogosPorPlataforma(@RequestParam @NotNull PlataformaJogo plataformas) {
        return ResponseEntity.ok(jogoService.listarQtdPorPlataforma(plataformas));
    }

    //LISTAR NOTA MAIOR QUE
    @GetMapping("/nota_pessoal_min")
    public ResponseEntity<List<Jogo>> listarNotaPessoalMaiorQue(@RequestParam @Min(0) @Max(10) Double notaPessoal) {
        return ResponseEntity.ok(jogoService.listarNotaPessoalMinima(notaPessoal));
    }


    //BUSCAS---------------------------------------------------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarJogoPorId(@PathVariable Integer id){
        Jogo jogo =jogoService.buscarPorId(id);
        return ResponseEntity.ok(jogo);
    }

    @GetMapping("/titulo")
    public ResponseEntity<List<Jogo>> buscarPorTitulo(@RequestParam @NotBlank String titulo){
        return ResponseEntity.ok(jogoService.buscarPorTitulo(titulo));
    }

    @GetMapping("/plataforma")
    public ResponseEntity<List<Jogo>> buscarPorPlataformas(
            @RequestParam @NotNull(message = "Informe a plataforma") PlataformaJogo plataformas)
    {
        return ResponseEntity.ok(jogoService.buscarPorPlataformas(plataformas));
    }

    @GetMapping("/genero")
    public ResponseEntity<List<Jogo>>buscarPorGenero(@RequestParam String genero){
        return ResponseEntity.ok(jogoService.buscarPorGenero(genero));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Jogo>>buscarPorStatus(
            @RequestParam @NotNull(message = "Status deve ser informado") StatusJogo status)
    {
        return ResponseEntity.ok(jogoService.buscarPorStatus(status));
    }

    //Nota exata
    @GetMapping("/nota-pessoal")
    public ResponseEntity<List<Jogo>> buscarPorNotaPessoal (@RequestParam Double notaPessoal){
        return ResponseEntity.ok(jogoService.buscarPorNotaPessoal(notaPessoal));

    }

    //-----------------------------------------------------------------------------------------------------------

    //UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<Jogo> atualizarJogoPorId(
            @PathVariable Integer id, @RequestBody JogoUpdateDTO dto){
       return ResponseEntity.ok(jogoService.atualizarJogoPorId(id, dto));
    }

    //ADICIONAR HORAS
    @PatchMapping("/{id}/adicionar-horas")
    public ResponseEntity<Jogo> adicionarHorasJogadasPorID(@PathVariable Integer id,@RequestParam Double horasJogadas ){
        return ResponseEntity.ok(jogoService.adicionarHorasJogadasPorId(id, horasJogadas));
    }
    //ATUALIZAR STATUS
    @PatchMapping("/{id}/atualizar-status")
    public ResponseEntity<Jogo> atualizarStatusPorId(@PathVariable Integer id, @RequestParam StatusJogo statusJogo){
        return ResponseEntity.ok(jogoService.atualizarStatusPorId(id,statusJogo));
    }


    //------------------------------------------------------------------------------------------------------------------------------------

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJogoPorId(@PathVariable Integer id) {
        jogoService.deletarJogoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
