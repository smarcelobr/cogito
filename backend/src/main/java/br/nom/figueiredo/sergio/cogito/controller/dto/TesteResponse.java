package br.nom.figueiredo.sergio.cogito.controller.dto;

import br.nom.figueiredo.sergio.cogito.model.TesteStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TesteResponse {
    private Long id;
    private List<TesteQuestaoDto> perguntas = new ArrayList<>();
    private Integer nota;
    private TesteStatus status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TesteQuestaoDto> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<TesteQuestaoDto> perguntas) {
        this.perguntas = perguntas;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public TesteStatus getStatus() {
        return status;
    }

    public void setStatus(TesteStatus status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
}
