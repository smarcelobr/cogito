package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;

public class MaquinaIp {
    @Id
    private Long id;

    private Long maquinaId;
    private String ip;
    private String versaoIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersaoIp() {
        return versaoIp;
    }

    public void setVersaoIp(String versaoIp) {
        this.versaoIp = versaoIp;
    }

    public Long getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(Long maquinaId) {
        this.maquinaId = maquinaId;
    }
}
