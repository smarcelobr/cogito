package br.nom.figueiredo.sergio.cogito.controller.dto;

public class MarcarOpcaoRequest {
    private Long questaoId;
    private Long opcaoId;

    public Long getQuestaoId() {
        return questaoId;
    }

    public void setQuestaoId(Long questaoId) {
        this.questaoId = questaoId;
    }

    public Long getOpcaoId() {
        return opcaoId;
    }

    public void setOpcaoId(Long opcaoId) {
        this.opcaoId = opcaoId;
    }
}
