package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.List;

public class TesteQuestao {
    @Id
    private Long id;
    private Long testeId;
    private Integer peso;
    private Long perguntaId;
    private Long opcaoId;

    @Transient
    private Pergunta pergunta;

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
        return this.pergunta.getOpcoes().stream()
                .filter(opcao -> opcao.getId().equals(this.opcaoId))
                .findFirst().orElse(null);
    }

    public List<Gabarito> getGabarito() {
        return this.pergunta.getGabarito().stream()
                .filter(Gabarito::getCorreta)
                .toList();
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }
}
