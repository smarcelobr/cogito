package br.nom.figueiredo.sergio.cogito.controller.dto;

public class PerguntaCloneRequest {
    /*
    * id da pergunta origem para clonar
    */
    private Long origem;

    public Long getOrigem() {
        return origem;
    }

    public void setOrigem(Long origem) {
        this.origem = origem;
    }
}
