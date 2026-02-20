package com.caigods.biblioteca_jogos.controller;

import com.caigods.biblioteca_jogos.business.JogoService;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {
    //Injecao do Service no controller via constructor
    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    //SALVAR
    //@Valid para validar as anotacoes na Entity (@min,@max,@notblank...)
    @PostMapping
    public ResponseEntity<Jogo> salvarJogo(@RequestBody @Valid Jogo jogo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoService.salvarJogo(jogo));
    }


//LISTAS------------------------------------------------------------------------------------------------
    //LISTAR TODOS OS JOGOS
    @GetMapping
    public ResponseEntity<List<Jogo>> listarJogos (){
        return ResponseEntity.ok(jogoService.listaJogos());
    }
//LISTAR QUANTIDADE DE JOGOS CADASTRADOS
    @GetMapping("/quantidade/total")
    public ResponseEntity<Long> listarQtdJogos (){
        return ResponseEntity.ok(jogoService.listarQtdJogos());
    }
//LISTAR QUANTIDADE DE JOGOS POR PLATAFORMA
    @GetMapping("/quantidade/plataforma")
    public ResponseEntity<Long> listarQtdJogosPorPlataforma (@RequestParam @Valid PlataformaJogo plataformas){
        return ResponseEntity.ok(jogoService.listarQtdPorPlataforma(plataformas));
    }
    //LISTAR NOTA MAIOR QUE
    @GetMapping("/nota_pessoal")
    public ResponseEntity<List<Jogo>> listarNotaPessalMaiorQue (@RequestParam @Valid Double notaPessoal){
        return ResponseEntity.ok(jogoService.listarNotaPessoalMinima(notaPessoal));
    }


    //BUSCAS---------------------------------------------------------------------------------------------

}
