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
    private List<Gabarito> gabaritos = new ArrayList<>();

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

    public List<Gabarito> getGabarito() {
        return gabaritos;
    }

    public void setGabarito(List<Gabarito> gabaritos) {
        this.gabaritos = gabaritos;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public void withOpcoes(List<Opcao> listaOpcoes) {
        this.opcoes = new ArrayList<>(listaOpcoes);
    }

    public void withGabaritos(List<Gabarito> listaGabaritos) {
        this.gabaritos = new ArrayList<>(listaGabaritos);
    }
}
