package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import com.caigods.biblioteca_jogos.infrasctuture.repository.JogoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JogoService {
    //Injetar via constructor
    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    @Transactional
    public Jogo salvarJogo(Jogo jogo) {

        //fazer validacoes logicas de hora, ano,
        // fazer validacao do enum statusjogo, e plataforma
        return jogoRepository.save(jogo);
    }

    public Jogo buscarPorId(Integer id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo nao encontrado"));
    }

    public List<Jogo> buscarPorTitulo(String titulo) {
        List<Jogo> resultado = jogoRepository.findByTituloContainingIgnoreCase(titulo);
        return resultado;
    }

    public List<Jogo> buscarPorPlataforma(String plataforma) {
        List<Jogo> resultado = jogoRepository.findByPlataformaContainingIgnoreCase(plataforma);
        return resultado;
    }

    public List<Jogo> buscarPorGenero(String genero) {
        List<Jogo> resultado = jogoRepository.findByGeneroContainingIgnoreCase(genero);
        return resultado;
    }

    public List<Jogo> buscarPorStatus(StatusJogo status) {
        List<Jogo> resultado = jogoRepository.findByStatusContainingIgnoreCase(status);
        return resultado;
    }

    @Transactional
    public void deletarJogoPorId(Integer id) {
        Jogo jogo = buscarPorId(id);
        jogoRepository.delete(jogo);
    }

    @Transactional
    public Jogo atualizarJogoPorId(Integer id, Jogo jogo) {
        Jogo jogoEntity = buscarPorId(id);

        //Validacao de ano nao ser maior que o atual
        if (jogo.getAnoDeLancamento() != null) {
            int anoAtual = LocalDate.now().getYear(); //pega o ano do sistema
            if (jogo.getAnoDeLancamento() > anoAtual) {
                throw new RuntimeException("Ano nao pode ser maior que " + anoAtual);
            }
            if (jogo.getAnoDeLancamento() < 1958) {
                throw new RuntimeException("Ano nao pode ser menor que 1958");
            }
        }
        //validacao para horas de jogo negativas
        if (jogo.getHorasJogadas() != null && jogo.getHorasJogadas() < 0) {
            throw new RuntimeException("Hora nao pode ser negativa");

        }
        if (jogo.getNotaPessoal() != null && jogo.getNotaPessoal() < 0 || jogo.getNotaPessoal() > 10) {
            throw new RuntimeException("Nota deve ser entre 0 e 10");
        }

        // Se o usuario enviou um novo valor, atualizamos;
        // caso contrário, mantemos o que ja estava gravado para não apagar os dados.

        if (jogo.getTitulo() != null) {
            jogoEntity.setTitulo(jogo.getTitulo());
        }
        if (jogo.getPlataforma() != null) {
            jogoEntity.setPlataforma(jogo.getPlataforma());
        }
        if (jogo.getGenero() != null) {
            jogoEntity.setGenero(jogo.getGenero());
        }
        if (jogo.getAnoDeLancamento() != null) {
            jogoEntity.setAnoDeLancamento(jogo.getAnoDeLancamento());
        }
        if (jogo.getStatus() != null) {
            jogoEntity.setStatus(jogo.getStatus());
        }
        if (jogo.getNotaPessoal() != null) {
            jogoEntity.setNotaPessoal(jogo.getNotaPessoal());
        }
        if (jogo.getHorasJogadas() != null) {
            jogoEntity.setHorasJogadas(jogo.getHorasJogadas());
        }


        return null; //retirar o null e consertar
    }


}
