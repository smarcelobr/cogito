package br.nom.figueiredo.sergio.cogito.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Cota {
    @Id
    private Long id;
    private String ip;
    private LocalDateTime endTime;

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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
