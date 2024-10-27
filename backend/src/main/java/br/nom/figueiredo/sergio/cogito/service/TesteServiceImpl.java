package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import br.nom.figueiredo.sergio.cogito.repository.TesteQuestaoRepository;
import br.nom.figueiredo.sergio.cogito.repository.TesteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TesteServiceImpl implements TesteService {

    private final PerguntaService perguntaService;
    private final TesteRepository testeRepository;
    private final TesteQuestaoRepository testeQuestaoRepository;

    public TesteServiceImpl(PerguntaService perguntaService, TesteRepository testeRepository, TesteQuestaoRepository testeQuestaoRepository) {
        this.perguntaService = perguntaService;
        this.testeRepository = testeRepository;
        this.testeQuestaoRepository = testeQuestaoRepository;
    }

    @Transactional
    @Override
    public Mono<Teste> pegaTesteDoIP(String ip) {
        return testeRepository
                .findOneByIpAndStatusInOrderByDataCriacao(ip, List.of(TesteStatus.NOVO, TesteStatus.EM_ANDAMENTO))
                .delayUntil(teste -> this.testeQuestaoRepository.findAllByTesteIdOrderById(teste.getId())
                        .collectList()
                        .doOnNext(teste::withQuestoes))
                .switchIfEmpty(this.criarTeste(ip));
    }

    @Transactional
    @Override
    public Mono<Teste> criarTeste(String ip) {
        return this.perguntaService.getPerguntas(5)
                .map(this::criaQuestao)
                .collectList()
                .map(questoes -> {
                    Teste teste = new Teste();
                    teste.setDataCriacao(LocalDateTime.now());
                    teste.withQuestoes(questoes);
                    teste.setStatus(TesteStatus.NOVO);
                    teste.setIp(ip);
                    return teste;
                })
                .flatMap(this::salvarTesteCompleto);
    }

    @Transactional
    @Override
    public Mono<Teste> salvarTesteCompleto(Teste testeCompleto) {
        return testeRepository.save(testeCompleto)
                .delayUntil(teste -> Flux.fromIterable(testeCompleto.getQuestoes())
                        .doOnNext(questao -> questao.setTesteId(teste.getId()))
                        .flatMap(this.testeQuestaoRepository::save)
                        .collectList()
                        .doOnNext(teste::withQuestoes));
    }

    private TesteQuestao criaQuestao(Pergunta pergunta) {
        TesteQuestao questao = new TesteQuestao();
        questao.setPerguntaId(pergunta.getId());
        return questao;
    }
}
