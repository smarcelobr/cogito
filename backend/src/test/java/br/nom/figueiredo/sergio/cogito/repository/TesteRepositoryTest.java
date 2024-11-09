package br.nom.figueiredo.sergio.cogito.repository;

import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class TesteRepositoryTest {

    @Autowired
    private TesteRepository testeRepository;
    @Autowired
    private TesteQuestaoRepository testeQuestaoRepository;

    @Test
    public void testQuery() {
        Mono<Teste> source = testeRepository.findByIpAndStatusInOrderByDataCriacao("192.162.2.10", List.of(TesteStatus.NOVO, TesteStatus.EM_ANDAMENTO))
                .next();

        StepVerifier
                .create(source)
                .expectNextMatches(teste -> LocalDateTime.of(2024, 10, 27, 10, 17, 9).isEqual(teste.getDataCriacao()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testInsert() {
        TesteQuestao questao1 = new TesteQuestao();
        questao1.setPerguntaId(100L);
        questao1.setOpcaoId(100L);

        TesteQuestao questao2 = new TesteQuestao();
        questao2.setPerguntaId(101L);
        questao2.setOpcaoId(104L);

        Teste testeExample = new Teste();
        LocalDateTime dataCriacao = LocalDateTime.now();
        String ip = "192.162.2.11";
        testeExample.setDataCriacao(dataCriacao);
        testeExample.setDataConclusao(null);
        testeExample.setIp(ip);
        testeExample.setNota(null);
        testeExample.setStatus(TesteStatus.NOVO);
        testeExample.withQuestoes(List.of(questao1, questao2));
        Mono<Teste> salvo = testeRepository.save(testeExample)
                .delayUntil(teste -> Flux.fromIterable(testeExample.getQuestoes())
                        .doOnNext(questao -> questao.setTesteId(teste.getId()))
                        .flatMap(questao -> this.testeQuestaoRepository.save(questao))
                        .collectList()
                        .doOnNext(teste::withQuestoes));

        StepVerifier
                .create(salvo)
                .expectNextMatches(teste -> ip.equals(teste.getIp()) &&
                                            TesteStatus.NOVO.equals(teste.getStatus()) &&
                                            teste.getQuestoes().size() == 2 &&
                                            dataCriacao.isEqual(teste.getDataCriacao()))
                .expectComplete()
                .verify();


    }

}