package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Gabarito;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface GabaritoRepository extends ReactiveCrudRepository<Gabarito, Long> {
    Flux<Gabarito> findAllByPerguntaId(Long perguntaId);

}
