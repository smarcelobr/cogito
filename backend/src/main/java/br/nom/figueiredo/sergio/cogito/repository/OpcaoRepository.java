package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Opcao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OpcaoRepository extends ReactiveCrudRepository<Opcao, Long> {
    Flux<Opcao> findAllByPerguntaId(Long perguntaId);
}
