package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Maquina;
import reactor.core.publisher.Flux;

public interface MaquinaService {

    Flux<Maquina> findAllMaquinas();
}
