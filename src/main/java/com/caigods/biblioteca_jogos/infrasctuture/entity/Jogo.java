package com.caigods.biblioteca_jogos.infrasctuture.entity;

import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Título não pode estar vazio")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank(message = "Plataforma não pode estar vazio")
    @Column(name = "plataforma", nullable = false)
    private String plataforma;

    @Column(name = "genero")
    private String genero;

    @NotBlank(message = "Ano de lançamento não pode estar vazio")
    @Min(value = 1958, message = "Ano de lançamento deve ser maior que 1958")
    @Column(name = "ano de lancamento")
    private Integer anoDeLancamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status de jogo")
    private StatusJogo status;

    @Max(value = 10, message = "Nota nao pode ser maior que 10")
    @Min(value = 0, message = "Nota nao pode ser negativa")
    @Column(name = "nota pessoal")
    private Integer notaPessoal = 0;

    @Min(value = 0, message = "Horas jogadas nao pode ser negativa")
    @Column(name = "horas jogadas")
    private Double horasJogadas;


    //Constructor vazio
    public Jogo() {
        //Deixar um valor por padrao
        this.horasJogadas = 0.0;
        this.notaPessoal = 0;

    }

    //Constructor com todos os argumentos
    public Jogo(Integer id, String titulo, String plataforma,
                String genero, Integer anoDeLancamento, StatusJogo status,
                Integer notaPessoal, Double horasJogadas) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.anoDeLancamento = anoDeLancamento;
        this.status = status;
        this.notaPessoal = notaPessoal;
        this.horasJogadas = horasJogadas;

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

    public Integer getNotaPessoal() {
        return notaPessoal;
    }

    public void setNotaPessoal(Integer notaPessoal) {
        this.notaPessoal = notaPessoal;
    }

    public Double getHorasJogadas() {
        return horasJogadas;
    }

    public void setHorasJogadas(Double horasJogadas) {
        this.horasJogadas = horasJogadas;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

}
