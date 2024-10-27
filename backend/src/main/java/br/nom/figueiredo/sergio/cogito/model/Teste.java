package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Teste {
    @Id
    private Long id;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private TesteStatus status = TesteStatus.NOVO;
    private String ip;
    private Integer nota;

    @Transient
    private List<TesteQuestao> questoes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public TesteStatus getStatus() {
        return status;
    }

    public void setStatus(TesteStatus status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public List<TesteQuestao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<TesteQuestao> questoes) {
        this.questoes = questoes;
    }

    public void withQuestoes(List<TesteQuestao> questoes) {
        this.questoes = new ArrayList<>(questoes);
    }
}
