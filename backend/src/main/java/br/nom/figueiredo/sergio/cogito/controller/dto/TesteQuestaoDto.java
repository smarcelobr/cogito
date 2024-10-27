package br.nom.figueiredo.sergio.cogito.controller.dto;

public class TesteQuestaoDto {
    private Long id;
    private Long perguntaId;
    private Long opcaoId; /* opção escolhida */

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
}
