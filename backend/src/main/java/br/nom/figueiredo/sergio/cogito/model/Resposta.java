package br.nom.figueiredo.sergio.cogito.model;

public class Resposta {
    private Long pergunta;
    private Long opcao;
    private Boolean correta;
    private String explicacao;

    public Long getPergunta() {
        return pergunta;
    }

    public void setPergunta(Long pergunta) {
        this.pergunta = pergunta;
    }

    public Long getOpcao() {
        return opcao;
    }

    public void setOpcao(Long opcao) {
        this.opcao = opcao;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }
}
