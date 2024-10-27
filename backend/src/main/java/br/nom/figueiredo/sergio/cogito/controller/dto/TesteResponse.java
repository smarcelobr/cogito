package br.nom.figueiredo.sergio.cogito.controller.dto;

import java.util.List;

public class TesteResponse {
    private Long id;
    private List<TesteQuestaoDto> perguntas;
    private Integer nota;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
