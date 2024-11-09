package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PerguntaService {

    Flux<Pergunta> getRandom(String ip, int quantidade);

    /**
     * Retorna uma pergunta e suas opções.
     * @param id identifica a pergunta
     * @param rndSeed semente aleatória para misturar as opções.
     * @return pergunta e opções carregadas.
     */
    Mono<Pergunta> getPerguntaCompleta(Long id, Long rndSeed);
}
