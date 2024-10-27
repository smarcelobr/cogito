package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TesteQuestaoRepository extends ReactiveCrudRepository<TesteQuestao, Long> {
    Flux<TesteQuestao> findAllByTesteIdOrderById(Long testeId);
}
