package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Maquina {
    @Id
    private Long id;
    private String nome;
    private List<MaquinaIp> ips;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<MaquinaIp> getIps() {
        return ips;
    }

    public void setIps(List<MaquinaIp> ips) {
        this.ips = ips;
    }

    public void withIps(List<MaquinaIp> ips) {
        this.ips = new ArrayList<>(ips);
    }
}
