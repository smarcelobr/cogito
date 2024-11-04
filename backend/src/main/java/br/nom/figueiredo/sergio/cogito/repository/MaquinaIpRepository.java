package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.MaquinaIp;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MaquinaIpRepository extends ReactiveCrudRepository<MaquinaIp, Long>  {

    Flux<MaquinaIp> findAllByMaquinaId(Long maquinaId);
    Flux<MaquinaIp> findDistinctByIpIn(List<String> ip);
}
