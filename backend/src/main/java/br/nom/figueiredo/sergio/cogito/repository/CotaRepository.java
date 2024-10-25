package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Cota;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CotaRepository extends ReactiveCrudRepository<Cota, Long> {
}
