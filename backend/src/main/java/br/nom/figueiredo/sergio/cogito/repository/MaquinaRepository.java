package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Maquina;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MaquinaRepository extends ReactiveCrudRepository<Maquina, Long>  {

}
