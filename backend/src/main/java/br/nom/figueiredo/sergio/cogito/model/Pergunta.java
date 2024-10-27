package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

public class Pergunta {
    @Id
    private long id;
    private String disciplina;
    private String questao;
    @Transient
    private List<Opcao> opcoes = new ArrayList<>();
    @Transient
    private List<Resposta> respostas = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}
