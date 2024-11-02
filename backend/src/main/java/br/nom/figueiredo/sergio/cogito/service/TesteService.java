package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TesteService {

    Mono<Teste> pegaTesteDoIP(String ip);
    Mono<Teste> criarTeste(String ip);

    Mono<Teste> salvarTesteCompleto(Teste teste);

    Mono<TesteQuestao> marcarOpcao(Long testeId, Long questaoId, Long opcaoId);
    Mono<TesteQuestao> desmarcarOpcao(Long testeId, Long questaoId);

    Mono<Teste> corrigir(Long testeId);
}
