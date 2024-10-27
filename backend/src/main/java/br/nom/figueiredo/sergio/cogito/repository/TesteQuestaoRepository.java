package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TesteQuestaoRepository extends ReactiveCrudRepository<TesteQuestao, Long> {
}
