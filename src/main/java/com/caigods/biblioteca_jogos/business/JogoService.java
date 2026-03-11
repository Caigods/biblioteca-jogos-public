package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.dto.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.JogoUpdateDTO;
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
    public Jogo salvarJogo(JogoRequestDTO dto) {
        Jogo jogo = new Jogo(
                null,
                dto.getTitulo(),
                dto.getPlataformas(),
                dto.getGenero(),
                dto.getAnoDeLancamento(),
                dto.getStatus(),
                dto.getNotaPessoal(),
                dto.getHorasJogadas()
        );
        existeJogoNaPlataforma(jogo.getTitulo(), jogo.getPlataformas());
        validarAnoLancamento(jogo.getAnoDeLancamento());
        validarNotaPessoal(jogo.getNotaPessoal());
        validarHorasNegativas(jogo.getHorasJogadas());
        return jogoRepository.save(jogo);
    }


    // BUSCAS---------------------------------------------------------------------------------
    public Jogo buscarPorId(Integer id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Jogo não encontrado"));
    }

    public List<Jogo> buscarPorTitulo(String titulo) {
        List<Jogo> jogos = jogoRepository.findByTituloContainingIgnoreCase(titulo);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o título: " + titulo);
        }
        return jogos;
    }

    public List<Jogo> buscarPorPlataformas(PlataformaJogo plataformas) {
        List<Jogo> jogos = jogoRepository.findByPlataformas(plataformas);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado para a plataforma: " + plataformas);
        }
        return jogos;
    }

    public List<Jogo> buscarPorGenero(String genero) {
        List<Jogo> jogos = jogoRepository.findByGeneroContainingIgnoreCase(genero);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o gênero: " + genero);
        }
        return jogos;
    }

    public List<Jogo> buscarPorStatus(StatusJogo status) {
        List<Jogo> jogos = jogoRepository.findByStatus(status);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o status: " + status);
        }
        return jogos;
    }

    public List<Jogo> buscarPorNotaPessoal(Double notaPessoal) {
        validarNotaPessoal(notaPessoal);
        List<Jogo> jogos = jogoRepository.findByNotaPessoal(notaPessoal);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com a nota: " + notaPessoal);
        }
        return jogos;
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
    public Jogo atualizarJogoPorId(Integer id, JogoUpdateDTO dto) {


        //Validação de ano não ser maior que o atual
        validarAnoLancamento(dto.getAnoDeLancamento());
        //validação para horas de jogo negativas
        validarHorasNegativas(dto.getHorasJogadas());
        //validação para nota pessoal entre 0 e 10
        validarNotaPessoal(dto.getNotaPessoal());


        Jogo jogoEntity = buscarPorId(id);
        // Se o usuário enviou um novo valor, atualizamos;
        // caso contrário, mantemos o que já estava gravado para não apagar os dados.

        if (dto.getTitulo() != null) {
            jogoEntity.setTitulo(dto.getTitulo());
        }
        if (dto.getPlataformas() != null) {
            jogoEntity.setPlataformas(dto.getPlataformas());
        }
        if (dto.getGenero() != null) {
            jogoEntity.setGenero(dto.getGenero());
        }
        if (dto.getAnoDeLancamento() != null) {
            jogoEntity.setAnoDeLancamento(dto.getAnoDeLancamento());
        }
        if (dto.getStatus() != null) {
            jogoEntity.setStatus(dto.getStatus());
        }
        if (dto.getNotaPessoal() != null) {
            jogoEntity.setNotaPessoal(dto.getNotaPessoal());
        }
        if (dto.getHorasJogadas() != null) {
            jogoEntity.setHorasJogadas(dto.getHorasJogadas());
        }


        return jogoRepository.save(jogoEntity);
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
        if (anoDeLancamento > anoAtual) {
            throw new BadRequestException("O ano de lançamento não pode ser maior que o ano atual: " + anoAtual);
        }
        if (anoDeLancamento < 1958) {
            throw new BadRequestException("Ano não pode ser menor que 1958");
        }
    }

    private void validarNotaPessoal(Double notaPessoal) {
        if (notaPessoal == null) {
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
