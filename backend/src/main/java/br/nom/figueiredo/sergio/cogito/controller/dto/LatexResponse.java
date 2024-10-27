package br.nom.figueiredo.sergio.cogito.controller.dto;

public class LatexResponse {
    private String text;
    private String base64PNG;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBase64PNG() {
        return base64PNG;
    }

    public void setBase64PNG(String base64PNG) {
        this.base64PNG = base64PNG;
    }
}
