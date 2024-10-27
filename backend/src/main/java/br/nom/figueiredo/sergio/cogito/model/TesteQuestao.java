package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;

public class TesteQuestao {
    @Id
    private Long id;
    private Long testeId;
    private Long perguntaId;
    private Long opcaoId;

    private Pergunta pergunta;
    private Opcao opcao;
    private Gabarito gabarito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTesteId() {
        return testeId;
    }

    public void setTesteId(Long testeId) {
        this.testeId = testeId;
    }

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public Long getOpcaoId() {
        return opcaoId;
    }

    public void setOpcaoId(Long opcaoId) {
        this.opcaoId = opcaoId;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public Opcao getOpcao() {
        return opcao;
    }

    public void setOpcao(Opcao opcao) {
        this.opcao = opcao;
    }

    public Gabarito getGabarito() {
        return gabarito;
    }

    public void setGabarito(Gabarito gabarito) {
        this.gabarito = gabarito;
    }

}
