package br.nom.figueiredo.sergio.cogito.controller.dto;

import java.util.List;

public class PerguntaDto {
    private Long id;
    private String disciplina;
    private String enunciadoLatex;
    private List<OpcaoDto> opcoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getEnunciadoLatex() {
        return enunciadoLatex;
    }

    public void setEnunciadoLatex(String enunciadoLatex) {
        this.enunciadoLatex = enunciadoLatex;
    }

    public List<OpcaoDto> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoDto> opcoes) {
        this.opcoes = opcoes;
    }
}
