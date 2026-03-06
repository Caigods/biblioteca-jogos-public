package com.caigods.biblioteca_jogos.business;

import com.caigods.biblioteca_jogos.exception.BadRequestException;
import com.caigods.biblioteca_jogos.exception.ConflictException;
import com.caigods.biblioteca_jogos.exception.NotFoundException;
import com.caigods.biblioteca_jogos.infrasctuture.entity.Jogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import com.caigods.biblioteca_jogos.infrasctuture.repository.JogoRepository;
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

    @InjectMocks
    private JogoService jogoService;

    private Jogo jogoValido;

    @BeforeEach
    void setUp() {
        jogoValido = new Jogo(
                1,
                "The Witcher 3",
                PlataformaJogo.PC,
                "RPG",
                2015,
                StatusJogo.ZERADO,
                9.5,
                100.0
        );
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
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);
            when(jogoRepository.saveAndFlush(jogoValido)).thenReturn(jogoValido);

            Jogo resultado = jogoService.salvarJogo(jogoValido);

            assertThat(resultado).isEqualTo(jogoValido);
            verify(jogoRepository).saveAndFlush(jogoValido);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas jogadas são negativas")
        void deveLancarExcecaoQuandoHorasNegativas() {
            jogoValido.setHorasJogadas(-1.0);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("negativas");
        }

        @Test
        @DisplayName("Deve lançar ConflictException quando jogo já existe na plataforma")
        void deveLancarExcecaoQuandoJogoJaExisteNaPlataforma() {
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(true);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("Já existe um jogo");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando ano é maior que o atual")
        void deveLancarExcecaoQuandoAnoMaiorQueAtual() {
            jogoValido.setAnoDeLancamento(Year.now().getValue() + 1);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("ano atual");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando ano é menor que 1958")
        void deveLancarExcecaoQuandoAnoMenorQue1958() {
            jogoValido.setAnoDeLancamento(1957);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("1958");
        }

        @Test
        @DisplayName("Deve aceitar ano exatamente igual a 1958")
        void deveAceitarAnoIgualA1958() {
            jogoValido.setAnoDeLancamento(1958);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);
            when(jogoRepository.saveAndFlush(any())).thenReturn(jogoValido);

            assertThatCode(() -> jogoService.salvarJogo(jogoValido)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando nota pessoal é maior que 10")
        void deveLancarExcecaoQuandoNotaMaiorQueDez() {
            jogoValido.setNotaPessoal(10.1);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("0 e 10");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando nota pessoal é negativa")
        void deveLancarExcecaoQuandoNotaNegativa() {
            jogoValido.setNotaPessoal(-0.1);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);

            assertThatThrownBy(() -> jogoService.salvarJogo(jogoValido))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("0 e 10");
        }

        @Test
        @DisplayName("Deve aceitar nota nos limites exatos (0.0 e 10.0)")
        void deveAceitarNotaNosLimites() {
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);
            when(jogoRepository.saveAndFlush(any())).thenReturn(jogoValido);

            jogoValido.setNotaPessoal(0.0);
            assertThatCode(() -> jogoService.salvarJogo(jogoValido)).doesNotThrowAnyException();

            jogoValido.setNotaPessoal(10.0);
            assertThatCode(() -> jogoService.salvarJogo(jogoValido)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve aceitar ano nulo sem lançar exceção")
        void deveAceitarAnoNulo() {
            jogoValido.setAnoDeLancamento(null);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);
            when(jogoRepository.saveAndFlush(any())).thenReturn(jogoValido);

            assertThatCode(() -> jogoService.salvarJogo(jogoValido)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve aceitar nota nula sem lançar exceção")
        void deveAceitarNotaNula() {
            jogoValido.setNotaPessoal(null);
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);
            when(jogoRepository.saveAndFlush(any())).thenReturn(jogoValido);

            assertThatCode(() -> jogoService.salvarJogo(jogoValido)).doesNotThrowAnyException();
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
            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));

            Jogo resultado = jogoService.buscarPorId(1);

            assertThat(resultado).isEqualTo(jogoValido);
        }

        @Test
        @DisplayName("Deve lançar NotFoundException quando jogo não encontrado")
        void deveLancarExcecaoQuandoJogoNaoEncontrado() {
            when(jogoRepository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jogoService.buscarPorId(99))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("não encontrado");
        }
    }

    @Nested
    @DisplayName("listaJogos")
    class ListaJogos {

        @Test
        @DisplayName("Deve retornar lista com jogos cadastrados")
        void deveRetornarListaDeJogos() {
            when(jogoRepository.findAll()).thenReturn(List.of(jogoValido));

            List<Jogo> resultado = jogoService.listaJogos();

            assertThat(resultado).hasSize(1).contains(jogoValido);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há jogos")
        void deveRetornarListaVazia() {
            when(jogoRepository.findAll()).thenReturn(List.of());

            List<Jogo> resultado = jogoService.listaJogos();

            assertThat(resultado).isEmpty();
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
            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));

            jogoService.deletarJogoPorId(1);

            verify(jogoRepository).delete(jogoValido);
        }

        @Test
        @DisplayName("Deve lançar NotFoundException ao deletar jogo inexistente")
        void deveLancarExcecaoAoDeletarJogoInexistente() {
            when(jogoRepository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jogoService.deletarJogoPorId(99))
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
        @DisplayName("Deve atualizar apenas campos não nulos mantendo os demais")
        void deveAtualizarApenasFieldsNaoNulos() {
            Jogo jogoEntity = new Jogo(1, "The Witcher 3", PlataformaJogo.PC, "RPG", 2015, StatusJogo.ZERADO, 9.5, 100.0);

            Jogo jogoUpdate = new Jogo();
            jogoUpdate.setTitulo("Cyberpunk 2077");
            jogoUpdate.setHorasJogadas(null);
            jogoUpdate.setNotaPessoal(null);

            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoEntity));
            when(jogoRepository.saveAndFlush(any())).thenReturn(jogoEntity);

            jogoService.atualizarJogoPorId(1, jogoUpdate);

            assertThat(jogoEntity.getTitulo()).isEqualTo("Cyberpunk 2077");
            assertThat(jogoEntity.getPlataformas()).isEqualTo(PlataformaJogo.PC);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException ao atualizar com ano futuro")
        void deveLancarExcecaoComAnoFuturo() {
            Jogo jogoUpdate = new Jogo();
            jogoUpdate.setAnoDeLancamento(Year.now().getValue() + 5);
            jogoUpdate.setHorasJogadas(null);
            jogoUpdate.setNotaPessoal(null);

            assertThatThrownBy(() -> jogoService.atualizarJogoPorId(1, jogoUpdate))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("ano atual");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException ao atualizar com horas negativas")
        void deveLancarExcecaoComHorasNegativas() {
            Jogo jogoUpdate = new Jogo();
            jogoUpdate.setHorasJogadas(-5.0);
            jogoUpdate.setNotaPessoal(null);

            assertThatThrownBy(() -> jogoService.atualizarJogoPorId(1, jogoUpdate))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("negativas");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException ao atualizar com nota inválida")
        void deveLancarExcecaoComNotaInvalida() {
            Jogo jogoUpdate = new Jogo();
            jogoUpdate.setNotaPessoal(11.0);

            assertThatThrownBy(() -> jogoService.atualizarJogoPorId(1, jogoUpdate))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("0 e 10");
        }
    }

    // ================================
    // ATUALIZAR STATUS
    // Agora recebe StatusJogo diretamente em vez de Jogo
    // ================================
    @Nested
    @DisplayName("atualizarStatusPorId")
    class AtualizarStatusPorId {

        @Test
        @DisplayName("Deve atualizar status com sucesso")
        void deveAtualizarStatusComSucesso() {
            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);

            jogoService.atualizarStatusPorId(1, StatusJogo.JOGANDO);

            assertThat(jogoValido.getStatus()).isEqualTo(StatusJogo.JOGANDO);
            verify(jogoRepository).save(jogoValido);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando status é nulo")
        void deveLancarExcecaoQuandoStatusNulo() {
            assertThatThrownBy(() -> jogoService.atualizarStatusPorId(1, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Status");
        }

        @Test
        @DisplayName("Deve atualizar para cada status disponível")
        void deveAtualizarParaCadaStatus() {
            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);

            for (StatusJogo status : StatusJogo.values()) {
                jogoService.atualizarStatusPorId(1, status);
                assertThat(jogoValido.getStatus()).isEqualTo(status);
            }
        }
    }

    // ================================
    // ADICIONAR HORAS
    // Agora recebe Double diretamente em vez de Jogo
    // ================================
    @Nested
    @DisplayName("adicionarHorasJogadasPorId")
    class AdicionarHorasJogadas {

        @Test
        @DisplayName("Deve somar horas corretamente ao total existente")
        void deveSomarHorasCorretamente() {
            jogoValido.setHorasJogadas(50.0);

            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);

            jogoService.adicionarHorasJogadasPorId(1, 30.0);

            assertThat(jogoValido.getHorasJogadas()).isEqualTo(80.0);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas são nulas")
        void deveLancarExcecaoQuandoHorasNulas() {
            assertThatThrownBy(() -> jogoService.adicionarHorasJogadasPorId(1, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("horas");
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando horas a adicionar são negativas")
        void deveLancarExcecaoQuandoHorasNegativas() {
            assertThatThrownBy(() -> jogoService.adicionarHorasJogadasPorId(1, -10.0))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("negativas");
        }

        @Test
        @DisplayName("Deve aceitar zero horas sem lançar exceção")
        void deveAceitarZeroHoras() {
            jogoValido.setHorasJogadas(50.0);

            when(jogoRepository.findById(1)).thenReturn(Optional.of(jogoValido));
            when(jogoRepository.save(any())).thenReturn(jogoValido);

            jogoService.adicionarHorasJogadasPorId(1, 0.0);

            assertThat(jogoValido.getHorasJogadas()).isEqualTo(50.0);
        }
    }

    // ================================
    // VERIFICAR PLATAFORMA
    // ================================
    @Nested
    @DisplayName("existeJogoNaPlataforma")
    class ExisteJogoNaPlataforma {

        @Test
        @DisplayName("Deve lançar ConflictException quando jogo já existe na plataforma")
        void deveLancarExcecaoQuandoJogoExiste() {
            when(jogoRepository.existsJogoByTituloAndPlataformas("The Witcher 3", PlataformaJogo.PC)).thenReturn(true);

            assertThatThrownBy(() -> jogoService.existeJogoNaPlataforma("The Witcher 3", PlataformaJogo.PC))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("Já existe");
        }

        @Test
        @DisplayName("Não deve lançar exceção quando jogo não existe na plataforma")
        void naoDeveLancarExcecaoQuandoJogoNaoExiste() {
            when(jogoRepository.existsJogoByTituloAndPlataformas(any(), any())).thenReturn(false);

            assertThatCode(() -> jogoService.existeJogoNaPlataforma("Novo Jogo", PlataformaJogo.PC))
                    .doesNotThrowAnyException();
        }
    }
}
