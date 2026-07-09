package com.caigods.biblioteca_jogos.infrasctuture.entity;

import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Título não pode estar vazio")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Plataforma não pode estar vazia")
    @Column(name = "plataformas", nullable = false)
    private PlataformaJogo plataformas;

    @Column(name = "genero")
    private String genero;

    @Min(value = 1958, message = "Ano de lançamento deve ser maior que 1958")
    @Column(name = "ano_lancamento")
    private Integer anoDeLancamento;

    @NotNull(message = "Status não pode estar vazio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_jogo")
    private StatusJogo status;

    @Max(value = 10, message = "Nota não pode ser maior que 10")
    @Min(value = 0, message = "Nota não pode ser negativa")
    @Column(name = "nota_pessoal", length = 4)
    private Double notaPessoal = 0.0;

    @Min(value = 0, message = "Horas jogadas não pode ser negativa")
    @Column(name = "horas_jogadas", length = 6)
    private Double horasJogadas;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    //Constructor vazio
    public Jogo() {
        //Deixar um valor por padrao
        this.horasJogadas = 0.0;
        this.notaPessoal = 0.0;

    }

    //Constructor com todos os argumentos
    public Jogo(Integer id, String titulo, PlataformaJogo plataformas,
                String genero, Integer anoDeLancamento, StatusJogo status,
                Double notaPessoal, Double horasJogadas, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.plataformas = plataformas;
        this.genero = genero;
        this.anoDeLancamento = anoDeLancamento;
        this.status = status;
        this.notaPessoal = notaPessoal;
        this.horasJogadas = horasJogadas;
        this.usuario = usuario;

    }

    //GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnoDeLancamento() {
        return anoDeLancamento;
    }

    public void setAnoDeLancamento(Integer anoDeLancamento) {
        this.anoDeLancamento = anoDeLancamento;
    }

    public StatusJogo getStatus() {
        return status;
    }

    public void setStatus(StatusJogo status) {
        this.status = status;
    }

    public Double getNotaPessoal() {
        return notaPessoal;
    }

    public void setNotaPessoal(Double notaPessoal) {
        this.notaPessoal = notaPessoal;
    }

    public Double getHorasJogadas() {
        return horasJogadas;
    }

    public void setHorasJogadas(Double horasJogadas) {
        this.horasJogadas = horasJogadas;
    }

    public PlataformaJogo getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(PlataformaJogo plataformas) {
        this.plataformas = plataformas;
    }

    public Usuario getUsuario() {return usuario;}

    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

}
