package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.exception.ConflictException;
import com.caigods.biblioteca_jogos.exception.BadRequestException;
import com.caigods.biblioteca_jogos.exception.NotFoundException;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import com.caigods.biblioteca_jogos.infrasctuture.repository.JogoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.Year;
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
    public List<Jogo> listarNotaPessoalMinima(Double notaPessoal) {
        validarNotaPessoal(notaPessoal);
        return jogoRepository.findByNotaPessoalGreaterThanEqual(notaPessoal);
    }
    //-----------------------------------------------------------------------------------------


    //SALVAR JOGO
    @Transactional
    public Jogo salvarJogo(Jogo jogo) {
        //validação de horas já está validado para começar em zero no constructor da entity
        validarHorasNegativas(jogo.getHorasJogadas());
        existeJogoNaPlataforma(jogo.getTitulo(),jogo.getPlataformas());
        //Validação do ano
        validarAnoLancamento(jogo.getAnoDeLancamento());

        //validação Nota pessoal
        validarNotaPessoal(jogo.getNotaPessoal());

        return jogoRepository.saveAndFlush(jogo);

    }



    // BUSCAS---------------------------------------------------------------------------------
    public Jogo buscarPorId(Integer id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jogo não encontrado"));
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

    public List<Jogo> buscarPorNotaPessoal(Double notaPessoal) {
        validarNotaPessoal(notaPessoal);
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


        //Validação de ano não ser maior que o atual
        validarAnoLancamento(jogo.getAnoDeLancamento());
        //validação para horas de jogo negativas
        validarHorasNegativas(jogo.getHorasJogadas());
        //validação para nota pessoal entre 0 e 10
        validarNotaPessoal(jogo.getNotaPessoal());


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
    public Jogo atualizarStatusPorId(Integer id, StatusJogo statusJogo) {
        if (statusJogo == null) {
            throw new BadRequestException("Status é obrigatório para esta operação");
        }
        Jogo jogoEntity = buscarPorId(id);
        jogoEntity.setStatus(statusJogo);
        return jogoRepository.save(jogoEntity);
    }

    @Transactional
    public Jogo adicionarHorasJogadasPorId(Integer id, Double horasJogadas) {

        if (horasJogadas == null) {
            throw new BadRequestException("Você precisa informar quantas horas deseja adicionar.");
        }
        //Validar horas negativas
        validarHorasNegativas(horasJogadas);

        Jogo jogoEntity = buscarPorId(id);
        Double jogoHorasAntiga = jogoEntity.getHorasJogadas();
        Double jogoHorasNova = horasJogadas;

        jogoEntity.setHorasJogadas(jogoHorasNova + jogoHorasAntiga);
        return jogoRepository.save(jogoEntity);
    }


    //MÉTODOS VALIDAÇÕES-------------------------------------------------------------------------------------------------------------------------------------------
    private void validarHorasNegativas(Double horasJogadas) {
        if (horasJogadas == null) {
            return;
        }
        if (horasJogadas < 0) {
            throw new BadRequestException("Horas nao podem ser negativas");
        }
    }

    private void validarAnoLancamento(Integer anoDeLancamento) {
        if (anoDeLancamento == null) {
            return;
        }

        int anoAtual = Year.now().getValue();
        if ( anoDeLancamento > anoAtual) {
            throw new BadRequestException("O ano de lançamento não pode ser maior que o ano atual: " + anoDeLancamento);
        }
        if (anoDeLancamento < 1958) {
            throw new BadRequestException("Ano não pode ser menor que 1958");
        }
    }

    private void validarNotaPessoal(Double notaPessoal) {
        if (notaPessoal == null){
            return;
        }
        if (notaPessoal < 0.0 || notaPessoal > 10.0) {
            throw new BadRequestException("Nota deve ser entre 0 e 10");
        }

    }

    public boolean verificaJogoNaPlataformaExiste(String titulo, PlataformaJogo plataformaJogo) {
        return jogoRepository.existsJogoByTituloAndPlataformas(titulo, plataformaJogo);
    }

    public void existeJogoNaPlataforma(String titulo, PlataformaJogo plataformaJogo) {
        if (verificaJogoNaPlataformaExiste(titulo, plataformaJogo)) {
            throw new ConflictException("Já existe um jogo com esse título nessa plataforma");
        }
    }


}
