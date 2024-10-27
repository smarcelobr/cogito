package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TesteRepository extends ReactiveCrudRepository<Teste, Long> {
    Mono<Teste> findOneByIpAndStatusInOrderByDataCriacao(String ip, Collection<TesteStatus> testeStatus);
}
