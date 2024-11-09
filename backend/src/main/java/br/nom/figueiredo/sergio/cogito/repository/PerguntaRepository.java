package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PerguntaRepository extends ReactiveCrudRepository<Pergunta, Long> {

    Flux<Pergunta> findAllBy(Pageable pageable);

    /**
     * Retorna as perguntas em ordem de questões com maior taxa de erro primeiro.
     *
     * @param ip IP de quem fará o teste
     * @param rndSeed semente aleatória
     * @param pageable paginação
     * @return perguntas sorteadas
     */
    @Query("""
            select p.* from pergunta p
            join (select g.pergunta_id,
                count(tq.id) as total_testes,
                sum(IF(ifnull(tq.opcao_id,0) = g.opcao_id, 1, 0)) as total_acertos,
                round(sum(IF(ifnull(tq.opcao_id,0) = g.opcao_id, 1, 0))/count(tq.id),1) as taxa
              from gabarito g
              left join teste_questao tq
                  join teste t
                      on tq.teste_id = t.id
                      and t.status = 'CORRIGIDO'
                  on g.pergunta_id = tq.pergunta_id
            where
                g.correta = 1
                and (t.id is null or t.ip =:ip)
            group by g.pergunta_id, g.opcao_id) rnd
            on rnd.pergunta_id = p.id
            order by rnd.taxa, rand(:rndSeed)
            LIMIT :#{pageable.offset},:#{pageable.pageSize}""")
    Flux<Pergunta> findRandom(String ip, Integer rndSeed, Pageable pageable);

}
