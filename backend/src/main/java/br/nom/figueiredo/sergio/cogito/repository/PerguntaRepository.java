package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PerguntaRepository extends ReactiveCrudRepository<Pergunta, Long> {
}
