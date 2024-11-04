package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.IptableRule;
import br.nom.figueiredo.sergio.cogito.model.Maquina;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IptablesService {
    Flux<IptableRule> getIptablesForwardRules();

    Flux<IptableRule> addDropRule(Maquina maquina);

    Mono<Void> deleteDropRule(Maquina maquina);
}
