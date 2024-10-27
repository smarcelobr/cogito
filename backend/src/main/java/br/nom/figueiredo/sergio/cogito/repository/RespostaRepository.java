package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Resposta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RespostaRepository extends ReactiveCrudRepository<Resposta, Long> {
    Flux<Resposta> findAllByPergunta(Long perguntaId);

}
