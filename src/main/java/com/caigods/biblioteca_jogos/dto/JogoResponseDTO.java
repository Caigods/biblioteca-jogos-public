package com.caigods.biblioteca_jogos.dto;

import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.PlataformaJogo;
import com.caigods.biblioteca_jogos.infrasctuture.entity.enums.StatusJogo;

public class JogoResponseDTO {
    private Integer id;
    private String titulo;
    private PlataformaJogo plataformas;
    private String genero;
    private Integer anoDeLancamento;
    private StatusJogo status;
    private Double notaPessoal;
    private Double horasJogadas;

    public JogoResponseDTO() {
    }

    public JogoResponseDTO(Integer id, String titulo, PlataformaJogo plataformas, String genero, Integer anoDeLancamento, StatusJogo status, Double notaPessoal, Double horasJogadas) {
        this.id = id;
        this.titulo = titulo;
        this.plataformas = plataformas;
        this.genero = genero;
        this.anoDeLancamento = anoDeLancamento;
        this.status = status;
        this.notaPessoal = notaPessoal;
        this.horasJogadas = horasJogadas;
    }

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

    public PlataformaJogo getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(PlataformaJogo plataformas) {
        this.plataformas = plataformas;
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
}
