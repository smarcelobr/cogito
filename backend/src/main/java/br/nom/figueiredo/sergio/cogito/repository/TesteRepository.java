package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TesteRepository extends ReactiveCrudRepository<Teste, Long> {
    Mono<Teste> findOneByIpAndStatusInOrderByDataCriacao(String ip, Collection<TesteStatus> testeStatus);

    @Query("SELECT T.* FROM teste T " +
           "JOIN maquina_ip MIP on MIP.ip = T.ip " +
           "WHERE T.status = 'CORRIGIDO' " +
           "  AND T.nota IS NOT NULL " +
           "  AND T.data_conclusao IS NOT NULL " +
           "  AND DATE_ADD(T.data_conclusao, INTERVAL (18*T.nota) MINUTE) > :dataValidade " +
           "  AND MIP.maquina_id = :maquinaId")
    Flux<Teste> findCorrigidosByMaquinaAndValidade(Long maquinaId, LocalDateTime dataValidade);
}
