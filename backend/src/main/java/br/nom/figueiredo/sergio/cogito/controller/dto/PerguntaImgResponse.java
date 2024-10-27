package br.nom.figueiredo.sergio.cogito.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class PerguntaImgResponse {
    private Long id;
    private String disciplina;
    private String base64PNG;
    private List<OpcaoImg> opcoes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBase64PNG() {
        return base64PNG;
    }

    public void setBase64PNG(String base64PNG) {
        this.base64PNG = base64PNG;
    }

    public List<OpcaoImg> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoImg> opcoes) {
        this.opcoes = opcoes;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}
