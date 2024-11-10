package br.nom.figueiredo.sergio.cogito.controller.dto;

public class OpcaoImg {
    private Long id;
    private String letra;
    private String base64PNG;
    private Boolean correta;
    private String explicacaoBase64PNG;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getBase64PNG() {
        return base64PNG;
    }

    public void setBase64PNG(String base64PNG) {
        this.base64PNG = base64PNG;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setExplicacaoBase64PNG(String explicacaoBase64PNG) {
        this.explicacaoBase64PNG = explicacaoBase64PNG;
    }

    public String getExplicacaoBase64PNG() {
        return explicacaoBase64PNG;
    }
}
