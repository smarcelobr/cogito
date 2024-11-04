package br.nom.figueiredo.sergio.cogito.jobs;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class ConfereConexoesEvent extends ApplicationEvent {

    public ConfereConexoesEvent(Object source) {
        super(source);
    }

    public ConfereConexoesEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
