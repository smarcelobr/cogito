package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.jobs.ConfereConexoesEvent;
import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import br.nom.figueiredo.sergio.cogito.repository.OpcaoRepository;
import br.nom.figueiredo.sergio.cogito.repository.TesteQuestaoRepository;
import br.nom.figueiredo.sergio.cogito.repository.TesteRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TesteServiceImpl implements TesteService {

    private final PerguntaService perguntaService;
    private final TesteRepository testeRepository;
    private final TesteQuestaoRepository testeQuestaoRepository;
    private final OpcaoRepository opcaoRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TesteServiceImpl(PerguntaService perguntaService,
                            TesteRepository testeRepository,
                            TesteQuestaoRepository testeQuestaoRepository,
                            OpcaoRepository opcaoRepository,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.perguntaService = perguntaService;
        this.testeRepository = testeRepository;
        this.testeQuestaoRepository = testeQuestaoRepository;
        this.opcaoRepository = opcaoRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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
        return this.perguntaService.getRandom(ip, 214, 5)
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

    @Transactional
    @Override
    public Mono<TesteQuestao> marcarOpcao(Long testeId, Long questaoId, Long opcaoId) {
        // verifica se o teste está em preenchimento
        return getTesteEQuestao(testeId, questaoId).flatMap((tupla) -> {
            final TesteQuestao questao = tupla.getT2();
            return this.opcaoRepository.findById(opcaoId)
                    .switchIfEmpty(Mono.error(new CogitoServiceException(
                            String.format("Opção %d não existe!", opcaoId))))
                    .filter(opcao -> opcao.getPerguntaId().equals(questao.getPerguntaId()))
                    .switchIfEmpty(Mono.error(new CogitoServiceException(
                            String.format("Opção %d não é válida para a pergunta %d!",
                                    opcaoId, questao.getPerguntaId()))))
                    .flatMap(opcao -> {
                        questao.setOpcaoId(opcao.getId());
                        return this.testeQuestaoRepository.save(questao);
                    });
        });
    }

    @Transactional
    @Override
    public Mono<TesteQuestao> desmarcarOpcao(Long testeId, Long questaoId) {
        // verifica se o teste está em preenchimento
        return getTesteEQuestao(testeId, questaoId)
                .flatMap((tupla) -> {
            final TesteQuestao questao = tupla.getT2();
            questao.setOpcaoId(null);
            return this.testeQuestaoRepository.save(questao);
        });
    }

    @Transactional
    @Override
    public Mono<Teste> corrigir(Long testeId) {
        return this.testeRepository.findById(testeId)
                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Teste %d não encontrado.", testeId))))
                .filter(teste -> List.of(TesteStatus.NOVO, TesteStatus.EM_ANDAMENTO).contains(teste.getStatus()))
                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Teste %d já corrigido ou cancelado.", testeId))))
                .delayUntil(teste -> this.testeQuestaoRepository.findAllByTesteIdOrderById(teste.getId())
                        .delayUntil(testeQuestao -> this.perguntaService.getPerguntaCompleta(testeQuestao.getPerguntaId())
                                .doOnNext(testeQuestao::setPergunta))
                        .collectList()
                        .doOnNext(teste::withQuestoes))
                .flatMap(this::corrigirTeste);
    }

    private Mono<Teste> corrigirTeste(Teste teste) {
        BigDecimal pesoTotal = teste.getQuestoes().stream()
                .map(questao-> new BigDecimal(questao.getPeso()))// pega o peso da questão correta
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nota = teste.getQuestoes().stream()
                .filter(questao->questao.getOpcaoId().equals(questao.getGabarito().get(0).getOpcaoId())) // questão correta?
                .map(questao-> new BigDecimal(questao.getPeso()).setScale(2, RoundingMode.HALF_UP))// pega o peso da questão correta
                .reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), (notaParcial, peso) ->
                        notaParcial.add(peso.divide(pesoTotal, RoundingMode.HALF_UP)));

        teste.setNota(nota.min(BigDecimal.ONE).multiply(BigDecimal.TEN).intValue());
        teste.setDataConclusao(LocalDateTime.now());
        teste.setStatus(TesteStatus.CORRIGIDO);
        return this.testeRepository.save(teste)
                .doOnNext(testeSalvo ->
                        applicationEventPublisher.publishEvent(new ConfereConexoesEvent(testeSalvo)));
    }

    private Mono<Tuple2<Teste, TesteQuestao>> getTesteEQuestao(Long testeId, Long questaoId) {
        return Mono.zip(this.testeRepository.findById(testeId)
                        .switchIfEmpty(Mono.error(new CogitoServiceException(
                                String.format("Teste %d não existe!", testeId))))
                        .filter(teste -> List.of(TesteStatus.NOVO,
                                TesteStatus.EM_ANDAMENTO).contains(teste.getStatus()))
                        .switchIfEmpty(Mono.error(new CogitoServiceException(
                                String.format("Teste %d já foi concluído!", testeId))))
                        .flatMap(teste -> {
                            // altera o teste para EM_ANDAMENTO, caso seja novo.
                            if (teste.getStatus() == TesteStatus.NOVO) {
                                teste.setStatus(TesteStatus.EM_ANDAMENTO);
                                return this.testeRepository.save(teste);
                            } else {
                                return Mono.just(teste);
                            }
                        })
                , this.testeQuestaoRepository.findById(questaoId)
                        .switchIfEmpty(Mono.error(new CogitoServiceException(
                                String.format("Questão %d não existe!", questaoId))))

        );
    }

    private TesteQuestao criaQuestao(Pergunta pergunta) {
        TesteQuestao questao = new TesteQuestao();
        questao.setPerguntaId(pergunta.getId());
        return questao;
    }
}
