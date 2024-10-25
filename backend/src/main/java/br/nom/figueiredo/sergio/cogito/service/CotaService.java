package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Cota;
import reactor.core.publisher.Flux;

public interface CotaService {
    Flux<Cota> findAll();
}
