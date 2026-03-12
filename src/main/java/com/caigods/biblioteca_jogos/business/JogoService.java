package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.dto.JogoRequestDTO;
import com.caigods.biblioteca_jogos.dto.JogoResponseDTO;
import com.caigods.biblioteca_jogos.dto.JogoUpdateDTO;
import com.caigods.biblioteca_jogos.exception.BadRequestException;
import com.caigods.biblioteca_jogos.exception.ConflictException;
import com.caigods.biblioteca_jogos.exception.NotFoundException;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Usuario;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import com.caigods.biblioteca_jogos.infrasctuture.repository.JogoRepository;
import com.caigods.biblioteca_jogos.infrasctuture.repository.UsuarioRepository;
import com.caigods.biblioteca_jogos.mapper.JogoMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoMapper jogoMapper;

    public JogoService(JogoRepository jogoRepository, UsuarioRepository usuarioRepository, JogoMapper jogoMapper) {
        this.jogoRepository = jogoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jogoMapper = jogoMapper;
    }

    // Busca o usuário pelo email — usado em todos os métodos
    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + email));
    }

    // LISTAGENS
    public List<JogoResponseDTO> listaJogos(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByUsuario(usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo cadastrado");
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    public long listarQtdJogos(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        return jogoRepository.countByUsuario(usuario);
    }

    public Long listarQtdPorPlataforma(PlataformaJogo plataformas, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        return jogoRepository.countByPlataformasAndUsuario(plataformas, usuario);
    }

    public List<JogoResponseDTO> listarNotaPessoalMinima(Double notaPessoal, String email) {
        validarNotaPessoal(notaPessoal);
        Usuario usuario = buscarUsuarioPorEmail(email);
        return jogoRepository.findByNotaPessoalGreaterThanEqualAndUsuario(notaPessoal, usuario).stream().map(jogoMapper::toResponseDTO).toList();
    }

    // SALVAR JOGO
    @Transactional
    public JogoResponseDTO salvarJogo(JogoRequestDTO dto, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        existeJogoNaPlataforma(dto.getTitulo(), dto.getPlataformas(), usuario);
        validarAnoLancamento(dto.getAnoDeLancamento());
        validarNotaPessoal(dto.getNotaPessoal());
        validarHorasNegativas(dto.getHorasJogadas());

        Jogo jogo = jogoMapper.toEntity(dto);
        jogo.setUsuario(usuario);
        return jogoMapper.toResponseDTO(jogoRepository.save(jogo));
    }

    // BUSCAS
    public JogoResponseDTO buscarPorId(Integer id, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        return jogoMapper.toResponseDTO(jogoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new NotFoundException("Jogo não encontrado")));
    }

    public List<JogoResponseDTO> buscarPorTitulo(String titulo, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByTituloContainingIgnoreCaseAndUsuario(titulo, usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o título: " + titulo);
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    public List<JogoResponseDTO> buscarPorPlataformas(PlataformaJogo plataformas, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByPlataformasAndUsuario(plataformas, usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado para a plataforma: " + plataformas);
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    public List<JogoResponseDTO> buscarPorGenero(String genero, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByGeneroContainingIgnoreCaseAndUsuario(genero, usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o gênero: " + genero);
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    public List<JogoResponseDTO> buscarPorStatus(StatusJogo status, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByStatusAndUsuario(status, usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com o status: " + status);
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    public List<JogoResponseDTO> buscarPorNotaPessoal(Double notaPessoal, String email) {
        validarNotaPessoal(notaPessoal);
        Usuario usuario = buscarUsuarioPorEmail(email);
        List<Jogo> jogos = jogoRepository.findByNotaPessoalAndUsuario(notaPessoal, usuario);
        if (jogos.isEmpty()) {
            throw new NotFoundException("Nenhum jogo encontrado com a nota: " + notaPessoal);
        }
        return jogos.stream().map(jogoMapper::toResponseDTO).toList();
    }

    // DELETAR
    @Transactional
    public void deletarJogoPorId(Integer id, String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        Jogo jogo = buscarEntityPorId(id, usuario);
        jogoRepository.delete(jogo);
    }

    // ATUALIZAR
    @Transactional
    public JogoResponseDTO atualizarJogoPorId(Integer id, JogoUpdateDTO dto, String email) {
        validarAnoLancamento(dto.getAnoDeLancamento());
        validarHorasNegativas(dto.getHorasJogadas());
        validarNotaPessoal(dto.getNotaPessoal());
        Usuario usuario = buscarUsuarioPorEmail(email);
        Jogo jogoEntity = buscarEntityPorId(id, usuario);

        if (dto.getTitulo() != null) jogoEntity.setTitulo(dto.getTitulo());
        if (dto.getPlataformas() != null) jogoEntity.setPlataformas(dto.getPlataformas());
        if (dto.getGenero() != null) jogoEntity.setGenero(dto.getGenero());
        if (dto.getAnoDeLancamento() != null) jogoEntity.setAnoDeLancamento(dto.getAnoDeLancamento());
        if (dto.getStatus() != null) jogoEntity.setStatus(dto.getStatus());
        if (dto.getNotaPessoal() != null) jogoEntity.setNotaPessoal(dto.getNotaPessoal());
        if (dto.getHorasJogadas() != null) jogoEntity.setHorasJogadas(dto.getHorasJogadas());

        return jogoMapper.toResponseDTO(jogoRepository.save(jogoEntity));
    }

    @Transactional
    public JogoResponseDTO atualizarStatusPorId(Integer id, StatusJogo statusJogo, String email) {
        if (statusJogo == null) {
            throw new BadRequestException("Status é obrigatório para esta operação");
        }
        Usuario usuario = buscarUsuarioPorEmail(email);
        Jogo jogoEntity = buscarEntityPorId(id, usuario);
        jogoEntity.setStatus(statusJogo);
        return jogoMapper.toResponseDTO(jogoRepository.save(jogoEntity));
    }

    @Transactional
    public JogoResponseDTO adicionarHorasJogadasPorId(Integer id, Double horasJogadas, String email) {
        if (horasJogadas == null) {
            throw new BadRequestException("Você precisa informar quantas horas deseja adicionar.");
        }
        validarHorasNegativas(horasJogadas);
        Usuario usuario = buscarUsuarioPorEmail(email);
        Jogo jogoEntity = buscarEntityPorId(id, usuario);
        jogoEntity.setHorasJogadas(jogoEntity.getHorasJogadas() + horasJogadas);
        return jogoMapper.toResponseDTO(jogoRepository.save(jogoEntity));
    }

    // VALIDAÇÕES
    private Jogo buscarEntityPorId(Integer id, Usuario usuario) {
        return jogoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new NotFoundException("Jogo não encontrado"));
    }


    private void validarHorasNegativas(Double horasJogadas) {
        if (horasJogadas == null) return;
        if (horasJogadas < 0) {
            throw new BadRequestException("Horas não podem ser negativas");
        }
    }

    private void validarAnoLancamento(Integer anoDeLancamento) {
        if (anoDeLancamento == null) return;
        int anoAtual = Year.now().getValue();
        if (anoDeLancamento > anoAtual) {
            throw new BadRequestException("O ano de lançamento não pode ser maior que o ano atual: " + anoAtual);
        }
        if (anoDeLancamento < 1958) {
            throw new BadRequestException("Ano não pode ser menor que 1958");
        }
    }

    private void validarNotaPessoal(Double notaPessoal) {
        if (notaPessoal == null) return;
        if (notaPessoal < 0.0 || notaPessoal > 10.0) {
            throw new BadRequestException("Nota deve ser entre 0 e 10");
        }
    }

    public boolean verificaJogoNaPlataformaExiste(String titulo, PlataformaJogo plataformaJogo, Usuario usuario) {
        return jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(titulo, plataformaJogo, usuario);
    }

    public void existeJogoNaPlataforma(String titulo, PlataformaJogo plataformaJogo, Usuario usuario) {
        if (verificaJogoNaPlataformaExiste(titulo, plataformaJogo, usuario)) {
            throw new ConflictException("Já existe um jogo com esse título nessa plataforma");
        }
    }
}