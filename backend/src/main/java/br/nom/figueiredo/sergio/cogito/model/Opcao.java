package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;

public class Opcao {
    @Id
    private Long id;
    private Long pergunta;
    private String alternativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPergunta() {
        return pergunta;
    }

    public void setPergunta(Long pergunta) {
        this.pergunta = pergunta;
    }

    public String getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }
}
