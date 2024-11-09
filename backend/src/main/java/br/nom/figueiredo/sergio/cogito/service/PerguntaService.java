package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PerguntaService {

    Flux<Pergunta> getRandom(String ip, int quantidade);
    Mono<Pergunta> getPerguntaCompleta(Long id);
}
