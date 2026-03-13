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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JogoServiceTest {

    @Mock
    private JogoRepository jogoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JogoMapper jogoMapper;

    @InjectMocks
    private JogoService jogoService;

    private Usuario usuarioValido;
    private Jogo jogoValido;
    private JogoRequestDTO dtoValido;
    private JogoResponseDTO responseValido;
    private static final String EMAIL = "caigods@gmail.com";

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario(1, "Caigods", EMAIL, "senha123");

        jogoValido = new Jogo(1, "The Witcher 3", PlataformaJogo.PC, "RPG",
                2015, StatusJogo.ZERADO, 9.5, 100.0, usuarioValido);

        dtoValido = new JogoRequestDTO("The Witcher 3", PlataformaJogo.PC, "RPG",
                2015, StatusJogo.ZERADO, 9.5, 100.0);

        responseValido = new JogoResponseDTO(1, "The Witcher 3", PlataformaJogo.PC, "RPG",
                2015, StatusJogo.ZERADO, 9.5, 100.0);
    }

    // ================================
    // SALVAR JOGO
    // ================================
    @Nested
    @DisplayName("salvarJogo")
    class SalvarJogo {

        @Test
        @DisplayName("Deve salvar jogo com dados válidos")
        void deveSalvarJogoComDadosValidos() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(any(), any(), any())).thenReturn(false);
            when(jogoMapper.toEntity(dtoValido)).thenReturn(jogoValido);
            when(jogoRepository.save(any())).thenReturn(jogoValido);
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            JogoResponseDTO resultado = jogoService.salvarJogo(dtoValido, EMAIL);

            assertThat(resultado).isEqualTo(responseValido);
            verify(jogoRepository).save(any());
        }

        @Test
        @DisplayName("Deve lançar NotFoundException quando usuário não encontrado")
        void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jogoService.salvarJogo(dtoValido, EMAIL))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Usuário não encontrado");
        }

        @Test
        @DisplayName("Deve lançar ConflictException quando jogo já existe na plataforma")
        void deveLancarExcecaoQuandoJogoJaExiste() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(any(), any(), any())).thenReturn(true);

            assertThatThrownBy(() -> jogoService.salvarJogo(dtoValido, EMAIL))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("Já existe");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas são negativas")
        void deveLancarExcecaoQuandoHorasNegativas() {
            dtoValido.setHorasJogadas(-1.0);
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(any(), any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(dtoValido, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("negativas");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando ano é maior que o atual")
        void deveLancarExcecaoQuandoAnoMaiorQueAtual() {
            dtoValido.setAnoDeLancamento(Year.now().getValue() + 1);
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(any(), any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(dtoValido, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("ano atual");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando nota é inválida")
        void deveLancarExcecaoQuandoNotaInvalida() {
            dtoValido.setNotaPessoal(11.0);
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.existsJogoByTituloAndPlataformasAndUsuario(any(), any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(dtoValido, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("0 e 10");
        }
    }

    // ================================
    // LISTAGENS
    // ================================
    @Nested
    @DisplayName("listaJogos")
    class ListaJogos {

        @Test
        @DisplayName("Deve retornar lista de jogos do usuário")
        void deveRetornarListaDeJogos() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByUsuario(usuarioValido)).thenReturn(List.of(jogoValido));
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            List<JogoResponseDTO> resultado = jogoService.listaJogos(EMAIL);

            assertThat(resultado).hasSize(1).contains(responseValido);
        }

        @Test
        @DisplayName("Deve lançar NotFoundException quando lista está vazia")
        void deveLancarExcecaoQuandoListaVazia() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByUsuario(usuarioValido)).thenReturn(List.of());

            assertThatThrownBy(() -> jogoService.listaJogos(EMAIL))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Nenhum jogo cadastrado");
        }
    }

    // ================================
    // BUSCAS
    // ================================
    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("Deve retornar jogo quando encontrado")
        void deveRetornarJogoQuandoEncontrado() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(1, usuarioValido)).thenReturn(Optional.of(jogoValido));
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            JogoResponseDTO resultado = jogoService.buscarPorId(1, EMAIL);

            assertThat(resultado).isEqualTo(responseValido);
        }

        @Test
        @DisplayName("Deve lançar NotFoundException quando jogo não encontrado")
        void deveLancarExcecaoQuandoJogoNaoEncontrado() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(99, usuarioValido)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jogoService.buscarPorId(99, EMAIL))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Jogo não encontrado");
        }
    }

    // ================================
    // DELETAR
    // ================================
    @Nested
    @DisplayName("deletarJogoPorId")
    class DeletarJogoPorId {

        @Test
        @DisplayName("Deve deletar jogo quando encontrado")
        void deveDeletarJogoQuandoEncontrado() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(1, usuarioValido)).thenReturn(Optional.of(jogoValido));

            jogoService.deletarJogoPorId(1, EMAIL);

            verify(jogoRepository).delete(jogoValido);
        }

        @Test
        @DisplayName("Deve lançar NotFoundException ao deletar jogo inexistente")
        void deveLancarExcecaoAoDeletarJogoInexistente() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(99, usuarioValido)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jogoService.deletarJogoPorId(99, EMAIL))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    // ================================
    // ATUALIZAR
    // ================================
    @Nested
    @DisplayName("atualizarJogoPorId")
    class AtualizarJogoPorId {

        @Test
        @DisplayName("Deve atualizar apenas campos não nulos")
        void deveAtualizarApenasFieldsNaoNulos() {
            JogoUpdateDTO dto = new JogoUpdateDTO();
            dto.setTitulo("Cyberpunk 2077");

            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(1, usuarioValido)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            jogoService.atualizarJogoPorId(1, dto, EMAIL);

            assertThat(jogoValido.getTitulo()).isEqualTo("Cyberpunk 2077");
            assertThat(jogoValido.getPlataformas()).isEqualTo(PlataformaJogo.PC);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException ao atualizar com ano futuro")
        void deveLancarExcecaoComAnoFuturo() {
            JogoUpdateDTO dto = new JogoUpdateDTO();
            dto.setAnoDeLancamento(Year.now().getValue() + 1);

            assertThatThrownBy(() -> jogoService.atualizarJogoPorId(1, dto, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("ano atual");
        }
    }

    // ================================
    // ATUALIZAR STATUS
    // ================================
    @Nested
    @DisplayName("atualizarStatusPorId")
    class AtualizarStatusPorId {

        @Test
        @DisplayName("Deve atualizar status com sucesso")
        void deveAtualizarStatusComSucesso() {
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(1, usuarioValido)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            jogoService.atualizarStatusPorId(1, StatusJogo.JOGANDO, EMAIL);

            assertThat(jogoValido.getStatus()).isEqualTo(StatusJogo.JOGANDO);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando status é nulo")
        void deveLancarExcecaoQuandoStatusNulo() {
            assertThatThrownBy(() -> jogoService.atualizarStatusPorId(1, null, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Status");
        }
    }

    // ================================
    // ADICIONAR HORAS
    // ================================
    @Nested
    @DisplayName("adicionarHorasJogadasPorId")
    class AdicionarHorasJogadas {

        @Test
        @DisplayName("Deve somar horas corretamente")
        void deveSomarHorasCorretamente() {
            jogoValido.setHorasJogadas(50.0);
            when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioValido));
            when(jogoRepository.findByIdAndUsuario(1, usuarioValido)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);
            when(jogoMapper.toResponseDTO(jogoValido)).thenReturn(responseValido);

            jogoService.adicionarHorasJogadasPorId(1, 30.0, EMAIL);

            assertThat(jogoValido.getHorasJogadas()).isEqualTo(80.0);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas são nulas")
        void deveLancarExcecaoQuandoHorasNulas() {
            assertThatThrownBy(() -> jogoService.adicionarHorasJogadasPorId(1, null, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("horas");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas são negativas")
        void deveLancarExcecaoQuandoHorasNegativas() {
            assertThatThrownBy(() -> jogoService.adicionarHorasJogadasPorId(1, -10.0, EMAIL))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("negativas");
        }
    }
}