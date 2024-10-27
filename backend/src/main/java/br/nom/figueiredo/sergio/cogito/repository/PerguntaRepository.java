package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PerguntaRepository extends ReactiveCrudRepository<Pergunta, Long> {

    Flux<Pergunta> findAllBy(Pageable pageable);

}
