package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import com.caigods.biblioteca_jogos.infrasctuture.repository.JogoRepository;
import jakarta.transaction.Transactional;
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


    //LISTAGENS-----------------------------------------------------------------------------
    //Listar tudo
    public List<Jogo> listaJogos() {
        return jogoRepository.findAll();
    }

    //Listar QUANTIDADE de jogos cadastrados
    public long listarQtdJogos() {
        return jogoRepository.count();
    }

    // Listar QUANTIDADE Jogos por plataforma
    public Long listarQtdPorPlataforma(PlataformaJogo plataformas) {
        return jogoRepository.countByPlataformas(plataformas);
    }

    //Listar Notas MAIOR OU IGUAL
    public List<Jogo> listarNotaPessoalMinima (Double notaPessoal){
        return jogoRepository.findByNotaPessoalGreaterThanEqual(notaPessoal);
    }
    //-----------------------------------------------------------------------------------------


    //SALVAR JOGO
    @Transactional
    public Jogo salvarJogo(Jogo jogo) {
        //validação de horas já está validado para começar em zero no constructor da entity
        if (jogo.getHorasJogadas() < 0) {
            throw new RuntimeException("Hora nao pode ser negativa");
        }


        //Validação do ano
        int anoAtual = LocalDate.now().getYear(); //pega o ano do sistema
        if (jogo.getAnoDeLancamento() != null) {
            if (jogo.getAnoDeLancamento() > anoAtual) {
                throw new RuntimeException("Ano não pode ser maior que " + anoAtual);
            }
            if (jogo.getAnoDeLancamento() < 1958) {
                throw new RuntimeException("Ano não pode ser menor que 1958");
            }
        }

        //Nao permite títulos duplicados na MESMA PLATAFORMA
        if (jogoRepository.existsJogoByTituloAndPlataformas(jogo.getTitulo(), jogo.getPlataformas())) {
            throw new RuntimeException("Já existe um jogo com esse título nessa plataforma");
        }
        //validacao Nota pessoal
        if (jogo.getNotaPessoal() < 0.0 || jogo.getNotaPessoal() > 10.0) {
            throw new RuntimeException("Nota deve ser entre 0 e 10");
        }

        return jogoRepository.saveAndFlush(jogo);

    }


    // BUSCAS---------------------------------------------------------------------------------
    public Jogo buscarPorId(Integer id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
    }

    public List<Jogo> buscarPorTitulo(String titulo) {
        return jogoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Jogo> buscarPorPlataformas(PlataformaJogo plataformas) {
        return jogoRepository.findByPlataformas(plataformas);
    }

    public List<Jogo> buscarPorGenero(String genero) {
        return jogoRepository.findByGeneroContainingIgnoreCase(genero);
    }

    public List<Jogo> buscarPorStatus(StatusJogo status) {
        return jogoRepository.findByStatus(status);
    }

    public List<Jogo> buscarPorNotaPessoal(Double notaPessoal){
        return jogoRepository.findByNotaPessoal(notaPessoal);
    }

    //--------------------------------------------------------------------------------------


    // DELETAR JOGO
    @Transactional
    public void deletarJogoPorId(Integer id) {
        Jogo jogo = buscarPorId(id);
        jogoRepository.delete(jogo);
    }


    //ATUALIZAR JOGO----------------------------------------------------------------------
    @Transactional
    public Jogo atualizarJogoPorId(Integer id, Jogo jogo) {


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

        //validação para horas de jogo negativas
        if (jogo.getHorasJogadas() != null && jogo.getHorasJogadas() < 0) {
            throw new RuntimeException("Hora nao pode ser negativa");

        }
        if (jogo.getNotaPessoal() < 0.0 || jogo.getNotaPessoal() > 10.0) {
            throw new RuntimeException("Nota deve ser entre 0 e 10");
        }


        Jogo jogoEntity = buscarPorId(id);
        // Se o usuário enviou um novo valor, atualizamos;
        // caso contrário, mantemos o que já estava gravado para não apagar os dados.

        if (jogo.getTitulo() != null) {
            jogoEntity.setTitulo(jogo.getTitulo());
        }
        if (jogo.getPlataformas() != null) {
            jogoEntity.setPlataformas(jogo.getPlataformas());
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


        return jogoRepository.saveAndFlush(jogoEntity);
    }

    //Atualizar apenas status / Jogando, zerado, dropado, queue
    @Transactional
    public Jogo atualizarStatusPorId(Integer id, Jogo jogo) {
        Jogo jogoEntity = buscarPorId(id);
        jogoEntity.setStatus(jogo.getStatus());
        return jogoRepository.save(jogoEntity);
    }

    @Transactional
    public Jogo adicionarHorasJogadasPorID(Integer id, Jogo jogo) {
        if (jogo.getHorasJogadas() < 0) {
            throw new RuntimeException("Horas nao podem ser negativas");
        }
        Jogo jogoEntity = buscarPorId(id);
        Double jogoHorasAntiga = jogoEntity.getHorasJogadas();
        Double jogoHorasNova = jogo.getHorasJogadas();

        jogoEntity.setHorasJogadas(jogoHorasNova + jogoHorasAntiga);
        return jogoRepository.save(jogoEntity);
    }


}
