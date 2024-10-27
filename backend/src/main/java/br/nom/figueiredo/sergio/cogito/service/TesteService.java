package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import reactor.core.publisher.Mono;

public interface TesteService {

    Mono<Teste> pegaTesteDoIP(String ip);
    Mono<Teste> criarTeste(String ip);

    Mono<Teste> salvarTesteCompleto(Teste teste);
}
