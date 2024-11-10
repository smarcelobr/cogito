package br.nom.figueiredo.sergio.cogito.controller.dto;

public class OpcaoDto {
    private Long id;
    private String alternativaLatex;
    private Boolean correta;
    private String explicacaoLatex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlternativaLatex() {
        return alternativaLatex;
    }

    public void setAlternativaLatex(String alternativa) {
        this.alternativaLatex = alternativa;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public String getExplicacaoLatex() {
        return explicacaoLatex;
    }

    public void setExplicacaoLatex(String explicacaoLatex) {
        this.explicacaoLatex = explicacaoLatex;
    }
}
