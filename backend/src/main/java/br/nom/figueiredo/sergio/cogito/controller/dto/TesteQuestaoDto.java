package br.nom.figueiredo.sergio.cogito.controller.dto;

public class TesteQuestaoDto {
    private Long id;
    private Long perguntaId;
    private Long opcaoId; /* opção escolhida */
    private Boolean correto;
    private String explicacao; /* base 64 PNG da explicacao do acerto / erro */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCorreto(Boolean correto) {
        this.correto = correto;
    }

    public Boolean isCorreto() {
        return correto;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }
}
